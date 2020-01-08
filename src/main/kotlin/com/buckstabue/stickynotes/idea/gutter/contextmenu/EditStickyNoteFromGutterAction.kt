package com.buckstabue.stickynotes.idea.gutter.contextmenu

import com.buckstabue.stickynotes.StickyNote
import com.buckstabue.stickynotes.base.di.AppInjector
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class EditStickyNoteFromGutterAction(
    private val stickyNote: StickyNote
) : AnAction("Edit Sticky Note") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editorScenario =
            AppInjector.getProjectComponent(
                project
            ).editStickyNoteScenario()
        editorScenario.launch(stickyNote)
    }
}
