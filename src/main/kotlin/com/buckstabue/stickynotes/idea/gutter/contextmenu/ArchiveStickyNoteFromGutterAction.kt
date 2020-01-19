package com.buckstabue.stickynotes.idea.gutter.contextmenu

import com.buckstabue.stickynotes.StickyNote
import com.buckstabue.stickynotes.base.di.AppInjector
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import kotlinx.coroutines.launch

class ArchiveStickyNoteFromGutterAction(
    private val stickyNote: StickyNote
) : AnAction("Archive Sticky Note") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val projectComponent =
            AppInjector.getProjectComponent(project)

        val gutterAnalytics = projectComponent.plusGutterComponent().gutterAnalytics()
        gutterAnalytics.archiveStickyNote()

        val stickyNoteInteractor = projectComponent.stickyNoteInteractor()
        val projectScope = projectComponent.projectScope()

        projectScope.launch {
            stickyNoteInteractor.archiveStickyNote(stickyNote)
        }
    }
}
