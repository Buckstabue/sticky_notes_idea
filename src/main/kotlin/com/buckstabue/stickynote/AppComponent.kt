package com.buckstabue.stickynote

import com.buckstabue.stickynote.activenote.ActiveNoteComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent {
    fun plusProjectComponentFactory(): ProjectComponent.Factory

    fun plusActiveNoteComponent(): ActiveNoteComponent

    companion object {
        val INSTANCE by lazy { DaggerAppComponent.create() }
    }
}
