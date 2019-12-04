package com.buckstabue.stickynote

import com.intellij.openapi.project.Project
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent
import javax.inject.Scope

@PerProject
@Subcomponent(modules = [ProjectModule::class])
interface ProjectComponent {
    fun stickyNoteInteractor(): StickyNoteInteractor

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance project: Project): ProjectComponent
    }
}

@Module
abstract class ProjectModule {
    @Binds
    abstract fun bindStickyNoteRepository(stickyNoteRepository: StickyNoteRepositoryImpl): StickyNoteRepository
}

@Scope
annotation class PerProject
