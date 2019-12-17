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

    suspend fun removeStickyNotes(stickyNotes: List<StickyNote>) {
        stickyNoteRepository.removeStickyNotes(stickyNotes)
    }

    suspend fun setStickyNoteActive(stickyNote: StickyNote) {
        stickyNoteRepository.setStickyNoteActive(stickyNote)
    }

    suspend fun setStickyNotesDone(stickyNotes: List<StickyNote>) {
        stickyNoteRepository.setStickyNotesDone(stickyNotes)
    }
}
