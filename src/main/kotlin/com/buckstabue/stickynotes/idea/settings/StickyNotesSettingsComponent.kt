package com.buckstabue.stickynotes.idea.settings

import dagger.Subcomponent

@Subcomponent
interface StickyNotesSettingsComponent {
    fun inject(stickyNotesSettingsWindow: StickyNotesSettingsWindow)
}
