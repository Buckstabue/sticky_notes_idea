package com.buckstabue.stickynote

import kotlinx.coroutines.channels.ReceiveChannel

interface StickyNoteRepository {
    fun observeBacklogStickyNotes(): ReceiveChannel<List<StickyNote>>
    fun observeArchivedStickyNotes(): ReceiveChannel<List<StickyNote>>
    fun observeActiveStickyNote(): ReceiveChannel<StickyNote?>

    suspend fun addStickyNote(stickyNote: StickyNote)
    suspend fun removeStickyNotes(stickyNotes: List<StickyNote>)

    suspend fun setStickNotes(stickyNotes: List<StickyNote>)
    suspend fun setStickyNoteActive(stickyNote: StickyNote)

    suspend fun archiveStickyNote(stickyNote: StickyNote)
    suspend fun archiveStickyNotes(stickyNotes: List<StickyNote>)
    suspend fun addStickyNotesToBacklog(stickyNotes: List<StickyNote>)


}
