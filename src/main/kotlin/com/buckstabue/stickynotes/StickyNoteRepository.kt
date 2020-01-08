package com.buckstabue.stickynotes

import kotlinx.coroutines.channels.ReceiveChannel

interface StickyNoteRepository {
    fun observeBacklogStickyNotes(currentBranchRelatedOnly: Boolean): ReceiveChannel<List<StickyNote>>
    fun observeArchivedStickyNotes(): ReceiveChannel<List<StickyNote>>
    fun observeActiveStickyNote(): ReceiveChannel<StickyNote?>

    suspend fun getBacklogStickyNotes(): List<StickyNote>
    suspend fun getArchivedStickyNotes(): List<StickyNote>

    suspend fun addStickyNote(stickyNote: StickyNote)
    suspend fun removeStickyNotes(stickyNotes: List<StickyNote>)
    suspend fun moveStickyNotes(stickyNotes: List<StickyNote>, insertionIndex: Int)

    suspend fun setStickNotes(stickyNotes: List<StickyNote>)
    suspend fun setBacklogStickyNotes(stickyNotes: List<StickyNote>)
    suspend fun setArchivedStickyNotes(stickyNotes: List<StickyNote>)
    suspend fun setStickyNoteActive(stickyNote: StickyNote)

    suspend fun archiveStickyNote(stickyNote: StickyNote)
    suspend fun archiveStickyNotes(stickyNotes: List<StickyNote>)
    suspend fun addStickyNotesToBacklog(stickyNotes: List<StickyNote>)
    suspend fun editStickyNote(oldStickyNote: StickyNote, newStickyNote: StickyNote)
}
