package com.buckstabue.stickynote.activenote

import dagger.Subcomponent
import javax.inject.Scope


@PerActiveNote
@Subcomponent
interface ActiveNoteComponent {
    fun inject(activeNoteWindow: ActiveNoteWindow)
}


@Scope
annotation class PerActiveNote
