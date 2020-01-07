package com.buckstabue.stickynotes.idea.addstickynote

import com.buckstabue.stickynotes.FileBoundStickyNote
import com.buckstabue.stickynotes.FileLocation
import com.buckstabue.stickynotes.NonBoundStickyNote
import com.buckstabue.stickynotes.StickyNote
import com.buckstabue.stickynotes.base.di.AppInjector
import com.buckstabue.stickynotes.idea.IdeaFileLocation
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import kotlinx.coroutines.launch
import javax.swing.Icon

// we add @JvmOverloads to make it possible to create this action by reflection with the default constructor
class AddStickyNoteAction @JvmOverloads constructor(
    private val codeBindingEnabledByDefaultWhenPossible: Boolean = true,
    text: String? = null,
    icon: Icon? = null
) : AnAction(text, null, icon), DumbAware {
    companion object {
        private val logger = Logger.getInstance(AddStickyNoteAction::class.java)
    }

    override fun actionPerformed(event: AnActionEvent) {
        logger.debug("new event from place = ${event.place}")
        val project = event.project
        if (project == null) {
            logger.error("Project is null")
            return
        }
        val fileLocation = extractEditorCaretLocation(event, project)
        val canBindToCode = fileLocation != null

        val createStickyNoteResult = askUserToFillStickyNoteSettings(canBindToCode, project)
        if (createStickyNoteResult == null) {
            logger.debug("User cancelled sticky note description input")
            // user cancelled
            return
        }

        val stickyNote = createStickyNote(fileLocation, createStickyNoteResult)
        if (stickyNote == null) {
            logger.debug("Sticky note creation cancelled")
            return
        }
        val projectComponent = AppInjector.getProjectComponent(project)
        val stickyNoteInteractor = projectComponent.stickyNoteInteractor()
        val projectScope = projectComponent.projectScope()

        projectScope.launch {
            stickyNoteInteractor.addStickyNote(stickyNote)
            logger.debug("Sticky note successfully added $stickyNote")
            if (createStickyNoteResult.isSetActive) {
                stickyNoteInteractor.setStickyNoteActive(stickyNote)
                logger.debug("Sticky note was set active right after adding")
            }
        }
    }

    private fun createStickyNote(
        fileLocation: FileLocation?,
        createStickyNoteResult: CreateStickyNoteResult
    ): StickyNote? {
        return if (fileLocation == null || !createStickyNoteResult.isBindToCodeChecked) {
            NonBoundStickyNote(
                description = createStickyNoteResult.description,
                boundBranchName = createStickyNoteResult.branchNameBoundTo
            )
        } else {
            FileBoundStickyNote(
                fileLocation = fileLocation,
                description = createStickyNoteResult.description,
                boundBranchName = createStickyNoteResult.branchNameBoundTo
            )
        }
    }

    private fun extractEditorCaretLocation(event: AnActionEvent, project: Project): FileLocation? {
        val editor = CommonDataKeys.EDITOR.getData(event.dataContext)
        if (editor == null) {
            logger.debug("Could not extract editor from event")
            return null
        }
        val currentDocument = editor.document
        val currentLineNumber = editor.caretModel.logicalPosition.line
        val currentFile = FileDocumentManager.getInstance()
            .getFile(currentDocument)
        if (currentFile == null) {
            logger.error("Could not extract virtual file from document")
            return null
        }
        return IdeaFileLocation(
            project = project,
            file = currentFile,
            lineNumber = currentLineNumber
        )
    }

    private fun askUserToFillStickyNoteSettings(
        canBindToCode: Boolean,
        project: Project
    ): CreateStickyNoteResult? {
        val vcsService = AppInjector.getProjectComponent(project).vcsService()

        val addStickyNoteDialog = AddStickyNoteDialog(
            initialViewModel = CreateStickyNoteViewModel(
                description = "",
                isCodeBindingChecked = canBindToCode && codeBindingEnabledByDefaultWhenPossible,
                isCodeBindingCheckboxEnabled = canBindToCode,
                branchNameBoundTo = vcsService.getCurrentBranchName(),
                isBranchBindingChecked = false,
                isSetActive = false
            ),
            project = project
        )
        return if (addStickyNoteDialog.showAndGet()) {
            addStickyNoteDialog.getResult()
        } else {
            null
        }
    }
}
