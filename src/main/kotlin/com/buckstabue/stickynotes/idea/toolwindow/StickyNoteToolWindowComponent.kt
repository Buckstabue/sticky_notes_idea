package com.buckstabue.stickynotes.idea.toolwindow

import com.buckstabue.stickynotes.idea.toolwindow.activenote.ActiveNoteAnalytics
import com.buckstabue.stickynotes.idea.toolwindow.activenote.ActiveNoteWindow
import dagger.Subcomponent
import javax.inject.Scope

@PerToolWindow
@Subcomponent
interface StickyNoteToolWindowComponent {
    fun inject(activeNoteWindow: ActiveNoteWindow)

    fun activeNoteAnalytics(): ActiveNoteAnalytics
}

@Scope
annotation class PerToolWindow
