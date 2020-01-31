package com.buckstabue.stickynotes.base.di.project

import com.buckstabue.stickynotes.Editor
import com.buckstabue.stickynotes.StickyNoteRepository
import com.buckstabue.stickynotes.StickyNoteRepositoryImpl
import com.buckstabue.stickynotes.StickyNotesService
import com.buckstabue.stickynotes.idea.IdeaEditor
import com.buckstabue.stickynotes.idea.VcsServiceImpl
import com.buckstabue.stickynotes.vcs.VcsService
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface ProjectModule {

    @Module
    companion object {
        @Provides
        @PerProject
        @JvmStatic
        fun provideVcsService(project: Project): VcsService =
            VcsServiceImpl.getInstance(project)

        @Provides
        @PerProject
        @JvmStatic
        fun provideStickyNotesService(project: Project): StickyNotesService =
            ServiceManager.getService(
                project,
                StickyNotesService::class.java
            )
    }

    @Binds
    fun bindStickyNoteRepository(stickyNoteRepository: StickyNoteRepositoryImpl): StickyNoteRepository

    @Binds
    fun bindEditor(editor: IdeaEditor): Editor
}
