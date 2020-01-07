package com.buckstabue.stickynotes.idea.toolwindow

import com.buckstabue.stickynotes.idea.toolwindow.activenote.ActiveNoteWindow
import dagger.Subcomponent
import javax.inject.Scope

@PerToolWindow
@Subcomponent
interface StickyNoteToolWindowComponent {
    fun inject(activeNoteWindow: ActiveNoteWindow)
}

@Scope
annotation class PerToolWindow
