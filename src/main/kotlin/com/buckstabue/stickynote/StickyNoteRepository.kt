package com.buckstabue.stickynote

interface StickyNoteRepository {
    fun addStickyNote(stickyNote: StickyNote)
    fun getStickyNotes(): List<StickyNote>
}
