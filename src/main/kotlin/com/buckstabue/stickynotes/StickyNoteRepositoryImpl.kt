package com.buckstabue.stickynotes

import com.buckstabue.stickynotes.idea.StickyNotesService
import com.buckstabue.stickynotes.vcs.VcsService
import com.intellij.openapi.project.Project
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject

@PerProject
@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class StickyNoteRepositoryImpl @Inject constructor(
    private val vcsService: VcsService,
    projectScope: ProjectScope,
    project: Project
) : StickyNoteRepository {
    private val backlogStickyNotes: MutableList<StickyNote> = CopyOnWriteArrayList()
    private val archivedStickyNotes: MutableList<StickyNote> = CopyOnWriteArrayList()

    private val activeStickyNoteChannel = BroadcastChannel<StickyNote?>(Channel.CONFLATED).also { it.offer(null) }
    private val allBacklogStickyNoteListChannel =
        BroadcastChannel<List<StickyNote>>(Channel.CONFLATED).also { it.offer(emptyList()) }
    private val currentBranchBacklogStickyNoteListChannel =
        BroadcastChannel<List<StickyNote>>(Channel.CONFLATED).also { it.offer(emptyList()) }
    private val archivedStickyNoteListChannel =
        BroadcastChannel<List<StickyNote>>(Channel.CONFLATED).also { it.offer(emptyList()) }

    private val stickyNotesService = StickyNotesService.getInstance(project)

    init {
        projectScope.launch {
            stickyNotesService.observeLoadedStickyNotes()
                .consumeEach {
                    setStickNotes(it)
                }
        }

        projectScope.launch {
            vcsService.observeCurrentBranchName()
                .consumeEach {
                    notifyStickyNotesChanged()
                }
        }
    }

    override suspend fun addStickyNote(stickyNote: StickyNote) {
        if (stickyNote.isArchived) {
            archivedStickyNotes.add(stickyNote)
        } else {
            backlogStickyNotes.add(stickyNote)
        }

        notifyStickyNotesChanged()
    }

    private suspend fun notifyStickyNotesChanged() {
        allBacklogStickyNoteListChannel.send(backlogStickyNotes)
        archivedStickyNoteListChannel.send(archivedStickyNotes)

        val currentBranchBacklogStickyNotes = filterCurrentBranchStickyNotes(backlogStickyNotes)
        currentBranchBacklogStickyNoteListChannel.send(currentBranchBacklogStickyNotes)
        activeStickyNoteChannel.send(currentBranchBacklogStickyNotes.firstOrNull())

        val newStickyNoteList = backlogStickyNotes.toList().plus(archivedStickyNotes.toList())
        stickyNotesService.setStickyNotes(newStickyNoteList)
    }

    private fun filterCurrentBranchStickyNotes(stickyNotes: List<StickyNote>): List<StickyNote> {
        val currentBranchName = vcsService.getCurrentBranchName() ?: return stickyNotes
        return stickyNotes.filter {
            it.boundBranchName == null || it.boundBranchName == currentBranchName
        }
    }

    override suspend fun moveStickyNotes(stickyNotes: List<StickyNote>, insertionIndex: Int) {
        notifyStickyNotesChanged()
    }

    override suspend fun archiveStickyNote(stickyNote: StickyNote) {
        archiveStickyNotes(listOf(stickyNote))
    }

    override suspend fun removeStickyNotes(stickyNotes: List<StickyNote>) {
        backlogStickyNotes.removeAll(stickyNotes)
        archivedStickyNotes.removeAll(stickyNotes)
        notifyStickyNotesChanged()
    }

    override suspend fun setStickNotes(stickyNotes: List<StickyNote>) {
        backlogStickyNotes.clear()
        archivedStickyNotes.clear()

        val isArchivedToStickyNotes: Map<Boolean, List<StickyNote>> =
            stickyNotes.groupBy { it.isArchived }.toMap()
        backlogStickyNotes.addAll(isArchivedToStickyNotes[false].orEmpty())
        archivedStickyNotes.addAll(isArchivedToStickyNotes[true].orEmpty())

        notifyStickyNotesChanged()
    }

    override suspend fun setStickyNoteActive(stickyNote: StickyNote) {
        if (stickyNote.isArchived) {
            archivedStickyNotes.remove(stickyNote)
            backlogStickyNotes.add(0, stickyNote.setArchived(false))
        } else {
            if (backlogStickyNotes.firstOrNull() != stickyNote) {
                backlogStickyNotes.remove(stickyNote)
                backlogStickyNotes.add(0, stickyNote)
            }
        }

        notifyStickyNotesChanged()
    }

    override suspend fun archiveStickyNotes(stickyNotes: List<StickyNote>) {
        backlogStickyNotes.removeAll(stickyNotes)

        val newArchivedStickyNotes = stickyNotes.map { it.setArchived(true) }.minus(archivedStickyNotes)
        archivedStickyNotes.addAll(0, newArchivedStickyNotes)

        notifyStickyNotesChanged()
    }

    override suspend fun addStickyNotesToBacklog(stickyNotes: List<StickyNote>) {
        archivedStickyNotes.removeAll(stickyNotes) // some sticky notes can be archived, remove them from archive first

        val newBacklogStickyNotes = stickyNotes.map { it.setArchived(false) }.minus(backlogStickyNotes)
        backlogStickyNotes.addAll(newBacklogStickyNotes)

        notifyStickyNotesChanged()
    }

    override suspend fun updateStickyNoteDescription(stickyNote: StickyNote, newDescription: String) {
        val targetList = if (stickyNote.isArchived) archivedStickyNotes else backlogStickyNotes
        val index = targetList.indexOf(stickyNote)
        check(index != -1) {
            "Could not find sticky note to update. $stickyNote"
        }
        targetList[index] = stickyNote.setDescription(newDescription)
        notifyStickyNotesChanged()
    }

    override fun observeBacklogStickyNotes(
        currentBranchRelatedOnly: Boolean
    ): ReceiveChannel<List<StickyNote>> {
        return if (currentBranchRelatedOnly) {
            currentBranchBacklogStickyNoteListChannel.openSubscription()
        } else {
            allBacklogStickyNoteListChannel.openSubscription()
        }
    }

    override fun observeArchivedStickyNotes(): ReceiveChannel<List<StickyNote>> {
        return archivedStickyNoteListChannel.openSubscription()
    }

    override fun observeActiveStickyNote(): ReceiveChannel<StickyNote?> {
        return activeStickyNoteChannel.openSubscription()
    }

    override suspend fun getBacklogStickyNotes(): List<StickyNote> {
        return backlogStickyNotes
    }

    override suspend fun getArchivedStickyNotes(): List<StickyNote> {
        return archivedStickyNotes
    }

    override suspend fun setBacklogStickyNotes(stickyNotes: List<StickyNote>) {
        backlogStickyNotes.clear()
        backlogStickyNotes.addAll(stickyNotes)

        notifyStickyNotesChanged()
    }

    override suspend fun setArchivedStickyNotes(stickyNotes: List<StickyNote>) {
        archivedStickyNotes.clear()
        archivedStickyNotes.addAll(stickyNotes)

        notifyStickyNotesChanged()
    }
}
