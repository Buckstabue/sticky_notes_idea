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
    private val stickyNoteListChannel =
        BroadcastChannel<List<StickyNote>>(Channel.CONFLATED).also { it.offer(emptyList()) }

    override suspend fun addStickyNote(stickyNote: StickyNote) {
        require(!stickyNote.isDone) { "adding a done sticky note. $stickyNote" }

        undoneStickyNotes.add(stickyNote)
        if (undoneStickyNotes.size == 1) {
            // if it's the first added element
            activeStickyNoteChannel.send(stickyNote)
        }
        notifyStickyNotesChanged()
    }

    private suspend fun notifyStickyNotesChanged() {
        val newStickyNoteList = undoneStickyNotes.toList().plus(doneStickyNotes.toList())
        stickyNoteListChannel.send(newStickyNoteList)
    }

    override suspend fun setStickyNoteDone(stickyNote: StickyNote) {
        require(!stickyNote.isDone) { "Trying to set a sticky note done when it's already done" }

        undoneStickyNotes.remove(stickyNote)
        doneStickyNotes.add(0, stickyNote.setDone(true))

        activeStickyNoteChannel.send(undoneStickyNotes.firstOrNull())
        notifyStickyNotesChanged()
    }

    override suspend fun setStickyNoteActive(stickyNote: StickyNote) {
        if (stickyNote.isDone) {
            doneStickyNotes.remove(stickyNote)
            undoneStickyNotes.add(0, stickyNote)
        } else {
            if (undoneStickyNotes.firstOrNull() != stickyNote) {
                undoneStickyNotes.remove(stickyNote)
                undoneStickyNotes.add(0, stickyNote)
            }
        }

        activeStickyNoteChannel.send(stickyNote)
        notifyStickyNotesChanged()
    }

    override fun observeStickyNotes(): ReceiveChannel<List<StickyNote>> {
        return stickyNoteListChannel.openSubscription()
    }

    override fun observeActiveStickyNote(): ReceiveChannel<StickyNote?> {
        return activeStickyNoteChannel.openSubscription()
    }
}
