package com.buckstabue.stickynotes

import com.buckstabue.stickynotes.idea.IdeaEditor
import com.buckstabue.stickynotes.idea.stickynotelist.StickyNoteListDialog
import com.buckstabue.stickynotes.idea.toolwindow.StickyNoteToolWindowComponent
import com.intellij.openapi.project.Project
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import javax.inject.Scope

@PerProject
@Subcomponent(modules = [ProjectModule::class])
interface ProjectComponent {
    fun stickyNoteInteractor(): StickyNoteInteractor
    fun plusStickyNoteToolWindowComponent(): StickyNoteToolWindowComponent.Factory
    fun projectScope(): ProjectScope

    fun inject(stickyNoteListDialog: StickyNoteListDialog)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance project: Project): ProjectComponent
    }
}

@Module
interface ProjectModule {

    @Binds
    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    fun bindStickyNoteRepository(stickyNoteRepository: StickyNoteRepositoryImpl): StickyNoteRepository

    @Binds
    fun bindEditor(editor: IdeaEditor): Editor

    @Binds
    fun bindProjectScope(projectScope: ProjectScopeImpl): ProjectScope
}


@Scope
annotation class PerProject
