package com.buckstabue.stickynote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StickyNoteInteractor @Inject constructor(
    private val stickyNoteRepository: StickyNoteRepository,
    private val editor: Editor
) {
    fun addStickyNote(stickyNote: StickyNote) {
        stickyNoteRepository.addStickyNote(stickyNote)
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
}
