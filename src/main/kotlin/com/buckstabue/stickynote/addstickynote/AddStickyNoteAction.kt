package com.buckstabue.stickynote.addstickynote

import com.buckstabue.stickynote.ComponentManager
import com.buckstabue.stickynote.FileBoundStickyNote
import com.buckstabue.stickynote.StickyNote
import com.buckstabue.stickynote.StickyNoteInteractor
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import org.apache.log4j.Level

class AddStickyNoteAction : AnAction() {
    companion object {
        private val logger = Logger.getInstance(AddStickyNoteAction::class.java).also {
            it.setLevel(Level.DEBUG)
        }
    }

    override fun actionPerformed(event: AnActionEvent) {
        logger.debug("new event from place = ${event.place}")
        val project = event.project
        if (project == null) {
            logger.error("Project is null")
            return
        }
        val stickyNote = extractStickyNote(event)
        if (stickyNote == null) {
            logger.error("Could not extract sticky note")
            return
        }
        val stickyNoteInteractor = extractStickyNoteInteractor(project)
        stickyNoteInteractor.addStickyNote(stickyNote)
        logger.debug("Sticky note successfully added $stickyNote")
    }

    private fun extractStickyNote(event: AnActionEvent): StickyNote? {
        val editor = CommonDataKeys.EDITOR.getData(event.dataContext)
        if (editor == null) {
            logger.error("Editor is null")
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
        return FileBoundStickyNote(
            fileUrl = currentFile.url,
            lineNumber = currentLineNumber,
            description = "Test description"
        )
    }

    private fun extractStickyNoteInteractor(project: Project): StickyNoteInteractor {
        val projectComponent = ComponentManager.getProjectComponent(project)
        return projectComponent.stickyNoteInteractor()
    }

}
