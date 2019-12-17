package com.buckstabue.stickynote

import kotlinx.coroutines.channels.ReceiveChannel

interface StickyNoteRepository {
    fun observeStickyNotes(): ReceiveChannel<List<StickyNote>>
    fun observeActiveStickyNote(): ReceiveChannel<StickyNote?>

    suspend fun setStickNotes(stickyNotes: List<StickyNote>)
    suspend fun setStickyNoteActive(stickyNote: StickyNote)
    suspend fun addStickyNote(stickyNote: StickyNote)
    suspend fun setStickyNoteDone(stickyNote: StickyNote)
    suspend fun setStickyNotesDone(stickyNotes: List<StickyNote>)
    suspend fun removeStickyNotes(stickyNotes: List<StickyNote>)
}
