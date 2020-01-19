package com.buckstabue.stickynotes.base.di.project

import com.buckstabue.stickynotes.StickyNoteInteractor
import com.buckstabue.stickynotes.idea.StickyNotesServiceImpl
import com.buckstabue.stickynotes.idea.createeditstickynote.EditStickyNoteScenario
import com.buckstabue.stickynotes.idea.stickynotelist.di.StickyNoteListDialogComponent
import com.buckstabue.stickynotes.idea.toolwindow.di.StickyNoteToolWindowComponent
import com.buckstabue.stickynotes.vcs.VcsService
import com.intellij.openapi.project.Project
import dagger.BindsInstance
import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi

@PerProject
@Subcomponent(modules = [ProjectModule::class])
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
interface ProjectComponent {
    fun stickyNoteInteractor(): StickyNoteInteractor
    fun projectScope(): ProjectScope
    fun vcsService(): VcsService

    fun plusStickyNoteToolWindowComponent(): StickyNoteToolWindowComponent
    fun plusStickyNoteListDialogComponent(): StickyNoteListDialogComponent
    fun editStickyNoteScenario(): EditStickyNoteScenario

    fun inject(stickyNotesServiceImpl: StickyNotesServiceImpl)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance project: Project): ProjectComponent
    }
}
