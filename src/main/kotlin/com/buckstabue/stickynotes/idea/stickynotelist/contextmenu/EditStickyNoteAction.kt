package com.buckstabue.stickynotes.idea.stickynotelist.contextmenu

import com.buckstabue.stickynotes.base.di.AppInjector
import com.buckstabue.stickynotes.idea.MainScope
import com.buckstabue.stickynotes.idea.stickynotelist.panel.StickyNoteViewModel
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbAware
import kotlinx.coroutines.launch
import javax.swing.JList
import javax.swing.JOptionPane

class EditStickyNoteAction(
    private val stickyNoteJList: JList<StickyNoteViewModel>
) : AnAction("Edit"), DumbAware {
    companion object {
        private val logger = Logger.getInstance(EditStickyNoteAction::class.java)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val selectedStickyNotes = stickyNoteJList.selectedValuesList.map { it.stickyNote }
        if (selectedStickyNotes.isEmpty()) {
            logger.error("Sticky Notes list is empty, nothing to remove")
            return
        }
        if (selectedStickyNotes.size > 1) {
            logger.error("Cannot edit multiple sticky notes")
            return
        }
        val editedStickyNote = selectedStickyNotes.first()
        val editedStickyNoteIndex = stickyNoteJList.selectedIndex

        val project = e.project
        if (project == null) {
            logger.error("project is null")
            return
        }
        val newDescription = askUserToEnterStickyNoteDescription(editedStickyNote.description)
        if (newDescription == null) {
            logger.debug("User cancelled editing description")
            return
        }

        val projectComponent = AppInjector.getProjectComponent(project)
        val stickyNoteInteractor = projectComponent.stickyNoteInteractor()
        val projectScope = projectComponent.projectScope()

        projectScope.launch {
            stickyNoteInteractor.updateStickyNoteDescription(editedStickyNote, newDescription)
            MainScope().launch {
                stickyNoteJList.selectedIndex = editedStickyNoteIndex
            }
        }
    }

    private fun askUserToEnterStickyNoteDescription(oldDescription: String): String? {
        return JOptionPane.showInputDialog(
            null,
            "Description:",
            "Edit Sticky Note",
            JOptionPane.QUESTION_MESSAGE,
            null,
            null,
            oldDescription
        ) as String?
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = stickyNoteJList.selectedValuesList.size == 1
    }
}
