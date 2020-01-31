package com.buckstabue.stickynotes.idea.settings

import com.buckstabue.stickynotes.base.di.AppInjector
import com.buckstabue.stickynotes.idea.StickyNotesWebHelpProvider
import com.intellij.openapi.options.ConfigurableBase

class StickyNotesConfigurable :
    ConfigurableBase<StickyNotesSettingsWindow, StickyNotesSettings>(
        "sticky_notes_settings", "Sticky Notes", StickyNotesWebHelpProvider.GITHUB_HELP_TOPIC_ID
    ) {
    private val stickyNotesSettings = AppInjector.appComponent.stickyNoteSettings()
    private val stickyNotesSettingsWindow by lazy { StickyNotesSettingsWindow() }

    override fun getSettings(): StickyNotesSettings {
        return stickyNotesSettings
    }

    override fun createUi(): StickyNotesSettingsWindow {
        return stickyNotesSettingsWindow
    }
}
