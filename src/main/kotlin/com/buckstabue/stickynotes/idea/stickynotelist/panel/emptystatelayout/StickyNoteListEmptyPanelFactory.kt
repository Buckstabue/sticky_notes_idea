package com.buckstabue.stickynotes.idea.stickynotelist.panel.emptystatelayout

import com.buckstabue.stickynotes.base.di.AppInjector
import com.buckstabue.stickynotes.idea.createeditstickynote.CreateEditStickyNoteAnalytics
import com.buckstabue.stickynotes.idea.createeditstickynote.CreateEditStickyNoteViewModel
import com.buckstabue.stickynotes.idea.createeditstickynote.CreateStickyNoteScenario
import com.buckstabue.stickynotes.idea.stickynotelist.panel.StickyNotesObservable
import com.buckstabue.stickynotes.idea.stickynotelist.panel.di.PerStickyNoteListPanel
import com.intellij.openapi.project.Project
import javax.inject.Inject
import javax.swing.JPanel

@PerStickyNoteListPanel
class StickyNoteListEmptyPanelFactory @Inject constructor(
    private val tabType: StickyNotesObservable.Type,
    private val project: Project
) {
    fun createEmptyPanel(): JPanel? {
        return when (tabType) {
            StickyNotesObservable.Type.CURRENT_BRANCH_BACKLOG -> {
                CreateNewStickyNoteWindow(newCreateStickyNoteScenario()).content
            }
            StickyNotesObservable.Type.ARCHIVED -> {
                null
            }
            StickyNotesObservable.Type.ALL_BACKLOG -> {
                null
            }
        }
    }

    private fun newCreateStickyNoteScenario(): CreateStickyNoteScenario {
        return AppInjector.getProjectComponent(project)
            .plusCreateEditStickyNoteComponent()
            .create(
                mode = CreateEditStickyNoteViewModel.Mode.CREATE,
                source = CreateEditStickyNoteAnalytics.Source.STICKY_NOTE_LIST_LINK
            ).createStickyNoteScenario()
    }
}
