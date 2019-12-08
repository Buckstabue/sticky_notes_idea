package com.buckstabue.stickynote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StickyNoteRepositoryImpl @Inject constructor(
) : StickyNoteRepository {
    private val stickyNotes = mutableListOf<StickyNote>()

    override fun addStickyNote(stickyNote: StickyNote) {
        stickyNotes.add(stickyNote)
    }

    override fun getStickyNotes(): List<StickyNote> {
        return stickyNotes.toList()
    }

    override fun getActiveStickyNote(): StickyNote? {
        return stickyNotes.firstOrNull()
    }
}
