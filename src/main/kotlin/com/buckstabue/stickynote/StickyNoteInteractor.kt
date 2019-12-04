package com.buckstabue.stickynote

import javax.inject.Inject

@PerProject
class StickyNoteInteractor @Inject constructor(
    private val stickyNoteRepository: StickyNoteRepository
) {
    fun addStickyNote(stickyNote: StickyNote) {
        stickyNoteRepository.addStickyNote(stickyNote)
    }
}
