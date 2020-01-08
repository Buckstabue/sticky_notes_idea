package com.buckstabue.stickynotes.idea.stickynotelist.di

import com.buckstabue.stickynotes.idea.stickynotelist.StickyNoteListDialog
import com.buckstabue.stickynotes.idea.stickynotelist.panel.di.StickyNoteListPanelComponent
import dagger.Subcomponent

@Subcomponent
@PerStickyNoteListDialog
interface StickyNoteListDialogComponent {
    fun plusStickyNotesPanelComponent(): StickyNoteListPanelComponent.Factory

    fun inject(stickyNoteListDialog: StickyNoteListDialog)
}
