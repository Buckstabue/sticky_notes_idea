package com.buckstabue.stickynotes.base.di.app

import com.buckstabue.stickynotes.analytics.Analytics
import com.buckstabue.stickynotes.base.di.project.ProjectComponent
import com.buckstabue.stickynotes.errormonitoring.ErrorLogger
import com.buckstabue.stickynotes.idea.settings.StickyNotesSettings
import com.buckstabue.stickynotes.idea.settings.StickyNotesSettingsComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun errorLogger(): ErrorLogger
    fun analytics(): Analytics
    fun stickyNoteSettings(): StickyNotesSettings

    fun plusProjectComponent(): ProjectComponent.Factory
    fun plusStickyNotesSettings(): StickyNotesSettingsComponent
}
