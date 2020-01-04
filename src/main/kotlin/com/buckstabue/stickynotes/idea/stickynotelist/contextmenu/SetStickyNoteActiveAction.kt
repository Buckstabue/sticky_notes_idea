package com.buckstabue.stickynotes.idea.stickynotelist.contextmenu

import com.buckstabue.stickynotes.AppInjector
import com.buckstabue.stickynotes.idea.MainScope
import com.buckstabue.stickynotes.idea.fullyClearSelection
import com.buckstabue.stickynotes.idea.stickynotelist.StickyNoteViewModel
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import kotlinx.coroutines.launch
import javax.swing.JList

@Suppress("NOTHING_TO_INLINE")
class SetStickyNoteActiveAction(
    private val stickyNoteJList: JList<StickyNoteViewModel>
) : AnAction("Set Active") {
    companion object {
        private val logger = Logger.getInstance(SetStickyNoteActiveAction::class.java)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val selectedValues = stickyNoteJList.selectedValuesList
        if (selectedValues.isEmpty()) {
            logger.error("There is no selection")
            return
        }
        if (selectedValues.size > 1) {
            logger.error("Cannot set multiply sticky notes active")
            return
        }

        val project = e.project
        if (project == null) {
            logger.error("project is null")
            return
        }
        val projectComponent = AppInjector.getProjectComponent(project)
        val stickyNoteInteractor = projectComponent.stickyNoteInteractor()
        val projectScope = projectComponent.projectScope()

        val selectedStickyNote = selectedValues.first().stickyNote
        projectScope.launch {
            stickyNoteInteractor.setStickyNoteActive(selectedStickyNote)
            MainScope().launch {
                if (stickyNoteJList.model.size > 0) {
                    val firstStickyNote = stickyNoteJList.model.getElementAt(0).stickyNote
                    if (firstStickyNote.isArchived) { // if editing an archived list
                        stickyNoteJList.fullyClearSelection()
                    } else { // if editing a backlog list
                        stickyNoteJList.selectedIndex = 0
                    }
                }
            }
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = shouldActionBeEnabled()
    }

    private inline fun shouldActionBeEnabled(): Boolean {
        if (stickyNoteJList.selectedValuesList.size != 1) { // multiple selection or no selection
            return false
        }
        val selectedStickyNote = stickyNoteJList.selectedValue.stickyNote
        return if (selectedStickyNote.isArchived) {
            true
        } else {
            stickyNoteJList.selectedIndex != 0
        }
    }
}
