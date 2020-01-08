package com.buckstabue.stickynotes.idea.stickynotelist.panel.di

import com.buckstabue.stickynotes.idea.stickynotelist.getstickynotesstrategy.StickyNotesObservable
import com.buckstabue.stickynotes.idea.stickynotelist.panel.StickyNotesPanelPanel
import dagger.BindsInstance
import dagger.Subcomponent

@PerStickyNoteListPanel
@Subcomponent
interface StickyNoteListPanelComponent {
    fun inject(stickyNotesPanelPanel: StickyNotesPanelPanel)

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance
            type: StickyNotesObservable.Type
        ): StickyNoteListPanelComponent
    }
}
