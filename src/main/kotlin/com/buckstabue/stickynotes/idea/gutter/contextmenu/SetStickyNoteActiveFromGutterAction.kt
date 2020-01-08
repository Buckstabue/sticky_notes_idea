package com.buckstabue.stickynotes.idea.gutter.contextmenu

import com.buckstabue.stickynotes.StickyNote
import com.buckstabue.stickynotes.base.di.AppInjector
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import kotlinx.coroutines.launch

class SetStickyNoteActiveFromGutterAction(
    private val stickyNote: StickyNote
) : AnAction("Set Sticky Note Active") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val projectComponent =
            AppInjector.getProjectComponent(
                project
            )
        val projectScope = projectComponent.projectScope()
        val stickyNoteInteractor =
            projectComponent.stickyNoteInteractor()

        projectScope.launch {
            stickyNoteInteractor.setStickyNoteActive(stickyNote)
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = !stickyNote.isActive
    }
}
