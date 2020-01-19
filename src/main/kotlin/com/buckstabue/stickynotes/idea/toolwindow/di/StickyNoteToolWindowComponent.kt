package com.buckstabue.stickynotes.idea.toolwindow.di

import com.buckstabue.stickynotes.idea.toolwindow.activenote.ActiveNoteAnalytics
import com.buckstabue.stickynotes.idea.toolwindow.activenote.ActiveNoteWindow
import dagger.Subcomponent

@PerToolWindow
@Subcomponent
interface StickyNoteToolWindowComponent {
    fun inject(activeNoteWindow: ActiveNoteWindow)

    fun activeNoteAnalytics(): ActiveNoteAnalytics
}
