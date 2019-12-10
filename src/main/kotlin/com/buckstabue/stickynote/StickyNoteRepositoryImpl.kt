package com.buckstabue.stickynote

import com.buckstabue.stickynote.service.StickyNotesService
import com.intellij.openapi.project.Project
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.distinct
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject

@PerProject
@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class StickyNoteRepositoryImpl @Inject constructor(
    private val projectScope: ProjectScope,
    project: Project
) : StickyNoteRepository {
    private val undoneStickyNotes: MutableList<StickyNote> = CopyOnWriteArrayList()
    private val doneStickyNotes: MutableList<StickyNote> = CopyOnWriteArrayList()

    private val activeStickyNoteChannel = BroadcastChannel<StickyNote?>(Channel.CONFLATED).also { it.offer(null) }
    private val stickyNoteListChannel =
        BroadcastChannel<List<StickyNote>>(Channel.CONFLATED).also { it.offer(emptyList()) }

    private val stickyNotesService = StickyNotesService.getInstance(project).also {
        projectScope.launch {
            it.observeLoadedStickyNotes().consumeEach {
                setStickNotes(it)
            }
        }
    }


    override suspend fun addStickyNote(stickyNote: StickyNote) {
        require(!stickyNote.isDone) { "adding a done sticky note. $stickyNote" }

        undoneStickyNotes.add(stickyNote)
        notifyStickyNotesChanged()
    }

    private suspend fun notifyStickyNotesChanged() {
        val newStickyNoteList = undoneStickyNotes.toList().plus(doneStickyNotes.toList())
        stickyNoteListChannel.send(newStickyNoteList)
        activeStickyNoteChannel.send(undoneStickyNotes.firstOrNull())

        stickyNotesService.setStickyNotes(newStickyNoteList)
    }

    override suspend fun setStickyNoteDone(stickyNote: StickyNote) {
        require(!stickyNote.isDone) { "Trying to set a sticky note done when it's already done" }

        undoneStickyNotes.remove(stickyNote)
        doneStickyNotes.add(0, stickyNote.setDone(true))

        notifyStickyNotesChanged()
    }

    override suspend fun setStickNotes(stickyNotes: List<StickyNote>) {
        undoneStickyNotes.clear()
        undoneStickyNotes.addAll(stickyNotes.filter { !it.isDone })

        doneStickyNotes.clear()
        doneStickyNotes.addAll(stickyNotes.filter { it.isDone })

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

        notifyStickyNotesChanged()
    }

    override fun observeStickyNotes(): ReceiveChannel<List<StickyNote>> {
        return stickyNoteListChannel.openSubscription()
    }

    override fun observeActiveStickyNote(): ReceiveChannel<StickyNote?> {
        return activeStickyNoteChannel.openSubscription().distinct()
    }
}
