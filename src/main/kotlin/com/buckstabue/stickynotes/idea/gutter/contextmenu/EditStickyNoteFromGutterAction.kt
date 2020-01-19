package com.buckstabue.stickynotes.idea.gutter.contextmenu

import com.buckstabue.stickynotes.StickyNote
import com.buckstabue.stickynotes.base.di.AppInjector
import com.buckstabue.stickynotes.idea.createeditstickynote.CreateEditStickyNoteAnalytics
import com.buckstabue.stickynotes.idea.createeditstickynote.CreateEditStickyNoteViewModel
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class EditStickyNoteFromGutterAction(
    private val stickyNote: StickyNote
) : AnAction("Edit Sticky Note") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val projectComponent = AppInjector.getProjectComponent(project)

        val gutterAnalytics = projectComponent.plusGutterComponent().gutterAnalytics()
        gutterAnalytics.editStickyNote()

        val createEditStickyNoteComponent = projectComponent
            .plusCreateEditStickyNoteComponent()
            .create(
                mode = CreateEditStickyNoteViewModel.Mode.EDIT,
                source = CreateEditStickyNoteAnalytics.Source.GUTTER
            )
        val editorScenario = createEditStickyNoteComponent.editStickyNoteScenario()
        editorScenario.launch(stickyNote)
    }
}
