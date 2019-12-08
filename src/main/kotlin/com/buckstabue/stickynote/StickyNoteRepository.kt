package com.buckstabue.stickynote

import kotlinx.coroutines.channels.ReceiveChannel

interface StickyNoteRepository {
    fun getStickyNotes(): List<StickyNote>
    fun observeActiveStickyNote(): ReceiveChannel<StickyNote?>

    suspend fun setStickyNoteActive(stickyNote: StickyNote)
    suspend fun addStickyNote(stickyNote: StickyNote)
    suspend fun setStickyNoteDone(stickyNote: StickyNote)
}
