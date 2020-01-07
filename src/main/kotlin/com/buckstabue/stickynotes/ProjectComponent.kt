package com.buckstabue.stickynotes

import com.buckstabue.stickynotes.idea.IdeaEditor
import com.buckstabue.stickynotes.idea.StickyNotesService
import com.buckstabue.stickynotes.idea.VcsServiceImpl
import com.buckstabue.stickynotes.idea.stickynotelist.StickyNoteListDialog
import com.buckstabue.stickynotes.idea.toolwindow.StickyNoteToolWindowComponent
import com.buckstabue.stickynotes.vcs.VcsService
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import javax.inject.Scope

@PerProject
@Subcomponent(modules = [ProjectModule::class])
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
interface ProjectComponent {
    fun stickyNoteInteractor(): StickyNoteInteractor
    fun plusStickyNoteToolWindowComponent(): StickyNoteToolWindowComponent
    fun projectScope(): ProjectScope

    fun inject(stickyNoteListDialog: StickyNoteListDialog)
    fun vcsService(): VcsService

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance project: Project): ProjectComponent
    }
}

@Module
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
interface ProjectModule {

    @Module
    companion object {
        @Provides
        @PerProject
        @JvmStatic
        fun provideVcsService(project: Project): VcsService = VcsServiceImpl.getInstance(project)

        @Provides
        @PerProject
        @JvmStatic
        fun provideStickyNotesService(project: Project): StickyNotesService =
            ServiceManager.getService(project, StickyNotesService::class.java)
    }

    @Binds
    fun bindStickyNoteRepository(stickyNoteRepository: StickyNoteRepositoryImpl): StickyNoteRepository

    @Binds
    fun bindEditor(editor: IdeaEditor): Editor

    @Binds
    fun bindProjectScope(projectScope: ProjectScopeImpl): ProjectScope
}


@Scope
annotation class PerProject
