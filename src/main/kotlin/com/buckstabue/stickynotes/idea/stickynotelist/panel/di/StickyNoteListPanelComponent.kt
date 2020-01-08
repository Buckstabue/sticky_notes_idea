package com.buckstabue.stickynotes.idea.stickynotelist.panel.di

import com.buckstabue.stickynotes.idea.stickynotelist.getstickynotesstrategy.StickyNotesObservable
import com.buckstabue.stickynotes.idea.stickynotelist.panel.StickyNotesPanel
import dagger.BindsInstance
import dagger.Subcomponent

@PerStickyNoteListPanel
@Subcomponent
interface StickyNoteListPanelComponent {
    fun inject(stickyNotesPanel: StickyNotesPanel)

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance
            type: StickyNotesObservable.Type
        ): StickyNoteListPanelComponent
    }
}
