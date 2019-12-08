package com.buckstabue.stickynote

import kotlinx.coroutines.channels.ReceiveChannel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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

    fun getStickyNotes(): List<StickyNote> {
        return stickyNoteRepository.getStickyNotes()
    }

    fun getActiveStickyNote(): StickyNote? {
        return stickyNoteRepository.getActiveStickyNote()
    }

    fun openStickyNote(stickyNote: FileBoundStickyNote) {
        editor.navigateToLine(stickyNote.fileUrl, stickyNote.lineNumber)
    }

    suspend fun setStickyNoteDone(stickyNote: StickyNote) {
        stickyNoteRepository.setStickyNoteDone(stickyNote)
    }
}
