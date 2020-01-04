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

class RemoveStickyNoteAction(
    private val stickyNoteJList: JList<StickyNoteViewModel>
) : AnAction("Remove") {
    companion object {
        private val logger = Logger.getInstance(RemoveStickyNoteAction::class.java)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val selectedStickyNotes = stickyNoteJList.selectedValuesList.map { it.stickyNote }
        if (selectedStickyNotes.isEmpty()) {
            logger.error("Sticky Notes list is empty, nothing to remove")
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

        projectScope.launch {
            stickyNoteInteractor.removeStickyNotes(selectedStickyNotes)
            MainScope().launch {
                stickyNoteJList.fullyClearSelection()
            }
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = stickyNoteJList.selectedValuesList.size > 0
    }
}
