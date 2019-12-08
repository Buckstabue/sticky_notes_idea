package com.buckstabue.stickynote

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ExperimentalCoroutinesApi
class StickyNoteRepositoryImpl @Inject constructor(
) : StickyNoteRepository {
    private val undoneStickyNotes: MutableList<StickyNote> = CopyOnWriteArrayList()
    private val doneStickyNotes: MutableList<StickyNote> = CopyOnWriteArrayList()

    private val activeStickyNoteChannel = BroadcastChannel<StickyNote?>(Channel.CONFLATED).also { it.offer(null) }

    override suspend fun addStickyNote(stickyNote: StickyNote) {
        require(!stickyNote.isDone) { "adding a done sticky note. $stickyNote" }

        undoneStickyNotes.add(stickyNote)
        if (undoneStickyNotes.size == 1) {
            // if it's the first added element
            setStickyNoteActive(stickyNote)
        }
    }

    override suspend fun setStickyNoteDone(stickyNote: StickyNote) {
        require(!stickyNote.isDone) { "Trying to set a sticky note done when it's already done" }

        undoneStickyNotes.remove(stickyNote)
        doneStickyNotes.add(stickyNote.setDone(true))

        activeStickyNoteChannel.send(undoneStickyNotes.firstOrNull())
    }

    override suspend fun setStickyNoteActive(stickyNote: StickyNote) {
        activeStickyNoteChannel.send(stickyNote)
    }

    override fun getStickyNotes(): List<StickyNote> {
        return undoneStickyNotes.toList()
    }

    override fun observeActiveStickyNote(): ReceiveChannel<StickyNote?> {
        return activeStickyNoteChannel.openSubscription()
    }
}
