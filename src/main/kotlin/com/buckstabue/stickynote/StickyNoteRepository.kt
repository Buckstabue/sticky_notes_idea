package com.buckstabue.stickynote

import kotlinx.coroutines.channels.ReceiveChannel

interface StickyNoteRepository {
    suspend fun addStickyNote(stickyNote: StickyNote)
    fun getStickyNotes(): List<StickyNote>
    fun getActiveStickyNote(): StickyNote?

    fun observeActiveStickyNote(): ReceiveChannel<StickyNote?>
}
