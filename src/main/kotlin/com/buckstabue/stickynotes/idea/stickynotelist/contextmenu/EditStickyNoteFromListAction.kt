package com.buckstabue.stickynotes.idea.stickynotelist.contextmenu

import com.buckstabue.stickynotes.StickyNote
import com.buckstabue.stickynotes.base.di.AppInjector
import com.buckstabue.stickynotes.idea.stickynotelist.panel.StickyNoteViewModel
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbAware
import javax.swing.JList

class EditStickyNoteFromListAction(
    private val stickyNoteJList: JList<StickyNoteViewModel>
) : AnAction("Edit"), DumbAware {
    companion object {
        private val logger = Logger.getInstance(EditStickyNoteFromListAction::class.java)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val selectedStickyNotes = stickyNoteJList.selectedValuesList.map { it.stickyNote }
        if (selectedStickyNotes.isEmpty()) {
            logger.error("Sticky Notes list is empty, nothing to edit")
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
        val editorScenario =
            AppInjector.getProjectComponent(project).editStickyNoteScenario()
        editorScenario.launch(
            stickyNoteToEdit = editedStickyNote,
            doOnSuccess = { newStickyNote ->
                updateListSelection(editedStickyNote, newStickyNote, editedStickyNoteIndex)
            }
        )
    }

    private fun updateListSelection(
        oldStickyNote: StickyNote,
        newStickyNote: StickyNote,
        oldStickyNoteIndex: Int
    ) {
        if (oldStickyNote.isArchived && !newStickyNote.isArchived) {
            return
        }
        if (newStickyNote.isActive) {
            stickyNoteJList.selectedIndex = 0
        } else {
            stickyNoteJList.selectedIndex = oldStickyNoteIndex
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = stickyNoteJList.selectedValuesList.size == 1
    }
}
