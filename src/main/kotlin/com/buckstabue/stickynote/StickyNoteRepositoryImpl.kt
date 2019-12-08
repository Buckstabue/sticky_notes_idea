package com.buckstabue.stickynote

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StickyNoteRepositoryImpl @Inject constructor(
) : StickyNoteRepository {
    private val stickyNotes: MutableList<StickyNote> = LinkedList()

    private val activeStickyNoteChannel = Channel<StickyNote?>()

    override suspend fun addStickyNote(stickyNote: StickyNote) {
        stickyNotes.add(0, stickyNote)
        activeStickyNoteChannel.send(stickyNote)
    }

    override fun getStickyNotes(): List<StickyNote> {
        return stickyNotes.toList()
    }

    override fun getActiveStickyNote(): StickyNote? {
        return stickyNotes.firstOrNull()
    }

    override fun observeActiveStickyNote(): ReceiveChannel<StickyNote?> {
        return activeStickyNoteChannel
    }
}
