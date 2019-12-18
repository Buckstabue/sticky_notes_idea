package com.buckstabue.stickynote.addstickynote

import com.buckstabue.stickynote.AppInjector
import com.buckstabue.stickynote.FileBoundStickyNote
import com.buckstabue.stickynote.FileLocation
import com.buckstabue.stickynote.NonBoundStickyNote
import com.buckstabue.stickynote.StickyNote
import com.buckstabue.stickynote.idea.IdeaFileLocation
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import kotlinx.coroutines.launch
import javax.swing.JOptionPane

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
        val description = askUserToEnterStickyNoteDescription()
        if (description == null) {
            logger.debug("User cancelled sticky note description input")
            // user cancelled
            return
        }
        val fileLocation = extractEditorCaretLocation(event, project)

        val stickyNote = createStickyNote(fileLocation, description)
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
        description: String
    ): StickyNote? {
        return if (fileLocation == null) {
            NonBoundStickyNote(
                description = description
            )
        } else {
            FileBoundStickyNote(
                fileLocation = fileLocation,
                description = description,
                isArchived = false
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

    private fun askUserToEnterStickyNoteDescription(): String? {
        return JOptionPane.showInputDialog(null, "Description:", "New Sticky Note", JOptionPane.QUESTION_MESSAGE)
    }
}
