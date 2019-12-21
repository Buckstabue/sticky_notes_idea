package com.buckstabue.stickynotes

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

    fun observeBacklogStickyNotes(): ReceiveChannel<List<StickyNote>> {
        return stickyNoteRepository.observeBacklogStickyNotes()
    }

    fun observeArchivedStickyNotes(): ReceiveChannel<List<StickyNote>> {
        return stickyNoteRepository.observeArchivedStickyNotes()
    }


    fun openStickyNote(stickyNote: FileBoundStickyNote) {
        editor.navigateToLine(stickyNote.fileLocation)
    }

    suspend fun archiveStickyNote(stickyNote: StickyNote) {
        stickyNoteRepository.archiveStickyNote(stickyNote)
    }

    suspend fun removeStickyNotes(stickyNotes: List<StickyNote>) {
        stickyNoteRepository.removeStickyNotes(stickyNotes)
    }

    suspend fun setStickyNoteActive(stickyNote: StickyNote) {
        stickyNoteRepository.setStickyNoteActive(stickyNote)
    }

    suspend fun archiveStickyNotes(stickyNotes: List<StickyNote>) {
        stickyNoteRepository.archiveStickyNotes(stickyNotes)
    }

    suspend fun addStickyNotesToBacklog(stickyNotes: List<StickyNote>) {
        stickyNoteRepository.addStickyNotesToBacklog(stickyNotes)
    }

    suspend fun moveStickyNotes(stickyNotes: List<StickyNote>, insertionIndex: Int) {
        val archiveStatus = stickyNotes.first().isArchived
        val isStickyNotesHomogeneousByArchiveStatus =
            stickyNotes.all { it.isArchived == archiveStatus }
        require(isStickyNotesHomogeneousByArchiveStatus) {
            "Sticky Note list must contain either archived or backlog items, not mixed ones"
        }
        stickyNoteRepository.moveStickyNotes(stickyNotes, insertionIndex)
    }
}
