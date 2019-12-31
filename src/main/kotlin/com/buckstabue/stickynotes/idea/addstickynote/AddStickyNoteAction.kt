package com.buckstabue.stickynotes.idea.addstickynote

import com.buckstabue.stickynotes.AppInjector
import com.buckstabue.stickynotes.FileBoundStickyNote
import com.buckstabue.stickynotes.FileLocation
import com.buckstabue.stickynotes.NonBoundStickyNote
import com.buckstabue.stickynotes.StickyNote
import com.buckstabue.stickynotes.idea.IdeaFileLocation
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import kotlinx.coroutines.launch

class AddStickyNoteAction : AnAction() {
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

        val createStickyNoteViewModel = askUserToEnterStickyNoteDescription(canBindToCode, project)
        if (createStickyNoteViewModel == null) {
            logger.debug("User cancelled sticky note description input")
            // user cancelled
            return
        }

        val stickyNote = createStickyNote(fileLocation, createStickyNoteViewModel)
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
        }
    }

    private fun createStickyNote(
        fileLocation: FileLocation?,
        createStickyNoteViewModel: CreateStickyNoteViewModel
    ): StickyNote? {
        return if (fileLocation == null || !createStickyNoteViewModel.bindToCode) {
            NonBoundStickyNote(
                description = createStickyNoteViewModel.description
            )
        } else {
            FileBoundStickyNote(
                fileLocation = fileLocation,
                description = createStickyNoteViewModel.description
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

    private fun askUserToEnterStickyNoteDescription(
        canBindToCode: Boolean,
        project: Project
    ): CreateStickyNoteViewModel? {
        val addStickyNoteDialog = AddStickyNoteDialog(
            canBindToCode = canBindToCode,
            project = project
        )
        return if (addStickyNoteDialog.showAndGet()) {
            addStickyNoteDialog.getViewModel()
        } else {
            null
        }
    }
}
