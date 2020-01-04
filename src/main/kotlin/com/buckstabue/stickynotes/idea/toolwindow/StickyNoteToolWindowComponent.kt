package com.buckstabue.stickynotes.idea.toolwindow

import com.buckstabue.stickynotes.idea.stickynotelist.StickyNoteListWindow
import com.buckstabue.stickynotes.idea.toolwindow.activenote.ActiveNoteWindow
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Scope

@PerToolWindow
@Subcomponent
interface StickyNoteToolWindowComponent {
    fun inject(activeNoteWindow: ActiveNoteWindow)
    fun inject(activeNoteWindow: StickyNoteListWindow)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance router: StickyNoteRouter): StickyNoteToolWindowComponent
    }
}

@Scope
annotation class PerToolWindow
