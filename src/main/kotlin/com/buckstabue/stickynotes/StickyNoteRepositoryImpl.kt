package com.buckstabue.stickynotes

import com.buckstabue.stickynotes.base.di.project.PerProject
import com.buckstabue.stickynotes.base.di.project.ProjectScope
import com.buckstabue.stickynotes.vcs.VcsService
import com.intellij.openapi.diagnostic.Logger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject

@PerProject
@UseExperimental(ExperimentalCoroutinesApi::class)
class StickyNoteRepositoryImpl @Inject constructor(
    private val vcsService: VcsService,
    private val stickyNotesService: StickyNotesService,
    projectScope: ProjectScope
) : StickyNoteRepository {
    companion object {
        private val logger = Logger.getInstance(StickyNoteRepositoryImpl::class.java)
    }

    private val backlogStickyNotes: MutableList<StickyNote> = CopyOnWriteArrayList()
    private val archivedStickyNotes: MutableList<StickyNote> = CopyOnWriteArrayList()

    private val activeStickyNoteChannel =
        BroadcastChannel<StickyNote?>(Channel.CONFLATED).also { it.offer(null) }
    private val allBacklogStickyNoteListChannel =
        BroadcastChannel<List<StickyNote>>(Channel.CONFLATED).also { it.offer(emptyList()) }
    private val currentBranchBacklogStickyNoteListChannel =
        BroadcastChannel<List<StickyNote>>(Channel.CONFLATED).also { it.offer(emptyList()) }
    private val archivedStickyNoteListChannel =
        BroadcastChannel<List<StickyNote>>(Channel.CONFLATED).also { it.offer(emptyList()) }

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
            if (stickyNote.isActive) {
                backlogStickyNotes.add(0, stickyNote)
            } else {
                backlogStickyNotes.add(stickyNote)
            }
        }

        notifyStickyNotesChanged()
    }

    private suspend fun notifyStickyNotesChanged() {
        updateActiveStickyNote()
        val currentBranchBacklogStickyNotes = filterCurrentBranchStickyNotes(backlogStickyNotes)
        currentBranchBacklogStickyNoteListChannel.send(currentBranchBacklogStickyNotes)

        allBacklogStickyNoteListChannel.send(backlogStickyNotes)
        archivedStickyNoteListChannel.send(archivedStickyNotes)

        val newStickyNoteList = backlogStickyNotes.toList().plus(archivedStickyNotes.toList())
        stickyNotesService.setStickyNotes(newStickyNoteList)
    }

    private suspend fun updateActiveStickyNote() {
        val currentBranch = vcsService.getCurrentBranchName()
        val firstCurrentBranchNote =
            backlogStickyNotes.firstOrNull { it.isVisibleInBranch(currentBranch) }
        if (firstCurrentBranchNote != null &&
            firstCurrentBranchNote.isActive &&
            backlogStickyNotes.count { it.isActive } == 1
        ) {
            activeStickyNoteChannel.send(firstCurrentBranchNote)
            return
        }
        backlogStickyNotes.replaceAll { it.setActive(it == firstCurrentBranchNote) }
        activeStickyNoteChannel.send(
            backlogStickyNotes.firstOrNull { it.isVisibleInBranch(currentBranch) }
        )
    }

    private fun filterCurrentBranchStickyNotes(stickyNotes: List<StickyNote>): List<StickyNote> {
        val currentBranchName = vcsService.getCurrentBranchName() ?: return stickyNotes
        return stickyNotes.filter {
            it.isVisibleInBranch(currentBranchName)
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
        if (stickyNote.isActive) {
            logger.warn("Setting sticky note active when it's already active")
        }
        if (stickyNote.isArchived) {
            archivedStickyNotes.remove(stickyNote)
            backlogStickyNotes.add(0, stickyNote.setArchived(false).setActive(true))
        } else {
            if (backlogStickyNotes.firstOrNull() != stickyNote) {
                backlogStickyNotes.remove(stickyNote)
                backlogStickyNotes.add(0, stickyNote.setActive(true))
            }
        }

        notifyStickyNotesChanged()
    }

    override suspend fun archiveStickyNotes(stickyNotes: List<StickyNote>) {
        backlogStickyNotes.removeAll(stickyNotes)

        val newArchivedStickyNotes =
            stickyNotes.map { it.setArchived(true).setActive(false) }.minus(archivedStickyNotes)
        archivedStickyNotes.addAll(0, newArchivedStickyNotes)

        notifyStickyNotesChanged()
    }

    override suspend fun addStickyNotesToBacklog(stickyNotes: List<StickyNote>) {
        archivedStickyNotes.removeAll(stickyNotes) // some sticky notes can be archived, remove them from archive first

        val newBacklogStickyNotes =
            stickyNotes.map { it.setArchived(false) }.minus(backlogStickyNotes)
        backlogStickyNotes.addAll(newBacklogStickyNotes)

        notifyStickyNotesChanged()
    }

    override suspend fun editStickyNote(oldStickyNote: StickyNote, newStickyNote: StickyNote) {
        val targetList = if (oldStickyNote.isArchived) archivedStickyNotes else backlogStickyNotes
        val index = targetList.indexOf(oldStickyNote)
        check(index != -1) {
            "Could not find sticky note to update. $oldStickyNote"
        }
        if (newStickyNote.isActive && !oldStickyNote.isActive) {
            backlogStickyNotes.remove(oldStickyNote)
            archivedStickyNotes.remove(oldStickyNote)
            backlogStickyNotes.replaceAll { it.setActive(false) }
            backlogStickyNotes.add(0, newStickyNote)
        } else {
            targetList[index] = newStickyNote
        }
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
