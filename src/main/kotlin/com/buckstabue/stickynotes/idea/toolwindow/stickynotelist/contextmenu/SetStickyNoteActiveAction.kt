package com.buckstabue.stickynotes.idea.toolwindow.stickynotelist.contextmenu

import com.buckstabue.stickynotes.AppInjector
import com.buckstabue.stickynotes.idea.MainScope
import com.buckstabue.stickynotes.idea.toolwindow.stickynotelist.StickyNoteViewModel
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import kotlinx.coroutines.launch
import javax.swing.JList

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
                stickyNoteJList.selectedIndex = 0
            }
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = stickyNoteJList.selectedValuesList.size == 1
    }
}
