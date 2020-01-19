package com.buckstabue.stickynotes.idea.stickynotelist.contextmenu

import com.buckstabue.stickynotes.base.di.AppInjector
import com.buckstabue.stickynotes.idea.MainScope
import com.buckstabue.stickynotes.idea.fullyClearSelection
import com.buckstabue.stickynotes.idea.stickynotelist.StickyNoteListAnalytics
import com.buckstabue.stickynotes.idea.stickynotelist.panel.StickyNoteViewModel
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbAware
import kotlinx.coroutines.launch
import javax.swing.JList

class MoveStickyNoteToBacklogAction(
    private val stickyNoteJList: JList<StickyNoteViewModel>,
    private val analytics: StickyNoteListAnalytics
) : AnAction("Move To Backlog"), DumbAware {
    companion object {
        private val logger = Logger.getInstance(MoveStickyNoteToBacklogAction::class.java)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val selectedStickyNotes = stickyNoteJList.selectedValuesList.map { it.stickyNote }
        if (selectedStickyNotes.isEmpty()) {
            logger.error("Sticky Notes list is empty, nothing to move to backlog")
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
            stickyNoteInteractor.addStickyNotesToBacklog(selectedStickyNotes)
            MainScope().launch {
                stickyNoteJList.fullyClearSelection()
            }
        }
        analytics.moveToBacklog()
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = stickyNoteJList.selectedValuesList
            .any { it.stickyNote.isArchived }
    }
}
