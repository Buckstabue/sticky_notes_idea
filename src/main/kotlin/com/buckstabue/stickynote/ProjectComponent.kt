package com.buckstabue.stickynote

import com.buckstabue.stickynote.idea.IdeaEditor
import com.buckstabue.stickynote.toolwindow.StickyNoteToolWindowComponent
import com.intellij.openapi.project.Project
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Scope

@PerProject
@Subcomponent(modules = [ProjectModule::class])
interface ProjectComponent {
    fun stickyNoteInteractor(): StickyNoteInteractor
    fun plusStickyNoteToolWindowComponent(): StickyNoteToolWindowComponent.Factory
    fun projectScope(): ProjectScope

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance project: Project): ProjectComponent
    }
}

@Module
interface ProjectModule {

    @Binds
    @ExperimentalCoroutinesApi
    fun bindStickyNoteRepository(stickyNoteRepository: StickyNoteRepositoryImpl): StickyNoteRepository

    @Binds
    fun bindEditor(editor: IdeaEditor): Editor

    @Binds
    fun bindProjectScope(projectScope: ProjectScopeImpl): ProjectScope
}


@Scope
annotation class PerProject
