package com.buckstabue.stickynote

import kotlinx.coroutines.channels.ReceiveChannel
import javax.inject.Inject

@PerProject
class StickyNoteInteractor @Inject constructor(
    private val stickyNoteRepository: StickyNoteRepository,
    private val editor: Editor
) {
    suspend fun addStickyNote(stickyNote: StickyNote) {
        stickyNoteRepository.addStickyNote(stickyNote)
    }

    fun observeActiveStickyNote(): ReceiveChannel<StickyNote?> {
        return stickyNoteRepository.observeActiveStickyNote()
    }

    fun observeStickyNotes(): ReceiveChannel<List<StickyNote>> {
        return stickyNoteRepository.observeStickyNotes()
    }

    fun openStickyNote(stickyNote: FileBoundStickyNote) {
        editor.navigateToLine(stickyNote.fileLocation)
    }

    suspend fun setStickyNoteDone(stickyNote: StickyNote) {
        stickyNoteRepository.setStickyNoteDone(stickyNote)
    }
}
