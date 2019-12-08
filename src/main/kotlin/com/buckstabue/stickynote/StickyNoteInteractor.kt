package com.buckstabue.stickynote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StickyNoteInteractor @Inject constructor(
    private val stickyNoteRepository: StickyNoteRepository
) {
    fun addStickyNote(stickyNote: StickyNote) {
        stickyNoteRepository.addStickyNote(stickyNote)
    }
}
