package com.buckstabue.stickynotes.idea.createeditstickynote.di

import com.buckstabue.stickynotes.idea.createeditstickynote.CreateEditStickyNoteAnalytics
import com.buckstabue.stickynotes.idea.createeditstickynote.CreateEditStickyNoteDialog
import com.buckstabue.stickynotes.idea.createeditstickynote.CreateEditStickyNoteViewModel
import com.buckstabue.stickynotes.idea.createeditstickynote.EditStickyNoteScenario
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent
@PerCreateEditStickyNote
interface CreateEditStickyNoteComponent {
    fun inject(createEditStickyNoteDialog: CreateEditStickyNoteDialog)

    fun editStickyNoteScenario(): EditStickyNoteScenario

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance
            mode: CreateEditStickyNoteViewModel.Mode,

            @BindsInstance
            source: CreateEditStickyNoteAnalytics.Source
        ): CreateEditStickyNoteComponent
    }
}
