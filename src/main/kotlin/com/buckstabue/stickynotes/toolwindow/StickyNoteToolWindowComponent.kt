package com.buckstabue.stickynotes.toolwindow

import com.buckstabue.stickynotes.toolwindow.activenote.ActiveNoteWindow
import com.buckstabue.stickynotes.toolwindow.stickynotelist.StickyNoteListWindow
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
