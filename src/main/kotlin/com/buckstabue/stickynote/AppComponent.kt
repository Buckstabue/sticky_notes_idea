package com.buckstabue.stickynote

import com.buckstabue.stickynote.activenote.ActiveNoteComponent
import com.buckstabue.stickynote.stickynotelist.StickyNoteListWindow
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun stickyNoteInteractor(): StickyNoteInteractor

    fun plusActiveNoteComponent(): ActiveNoteComponent

    fun inject(stickyNoteListWindow: StickyNoteListWindow)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance stickyNoteRouter: StickyNoteRouter): AppComponent
    }

    companion object {
        lateinit var INSTANCE: AppComponent
            private set

        fun init(stickyNoteRouter: StickyNoteRouter) {
            INSTANCE = DaggerAppComponent.factory().create(stickyNoteRouter)
        }
    }
}

@Module
interface AppModule {
    @Binds
    fun bindStickyNoteRepository(stickyNoteRepository: StickyNoteRepositoryImpl): StickyNoteRepository
}
