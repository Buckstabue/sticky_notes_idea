package com.buckstabue.stickynotes.idea.stickynotelist.panel

import com.buckstabue.stickynotes.StickyNote
import com.buckstabue.stickynotes.StickyNoteInteractor
import com.buckstabue.stickynotes.idea.stickynotelist.panel.di.PerStickyNoteListPanel
import kotlinx.coroutines.channels.ReceiveChannel
import javax.inject.Inject

@PerStickyNoteListPanel
class StickyNotesObservable @Inject constructor(
    private val type: Type,
    private val stickyNoteInteractor: StickyNoteInteractor
) {
    fun observeStickyNotes(): ReceiveChannel<List<StickyNote>> {
        return when (type) {
            Type.ARCHIVED ->
                stickyNoteInteractor.observeArchivedStickyNotes()
            Type.CURRENT_BRANCH_BACKLOG ->
                stickyNoteInteractor.observeBacklogStickyNotes(currentBranchRelatedOnly = true)
            Type.ALL_BACKLOG ->
                stickyNoteInteractor.observeBacklogStickyNotes(currentBranchRelatedOnly = false)
        }
    }

    enum class Type {
        ARCHIVED,
        CURRENT_BRANCH_BACKLOG,
        ALL_BACKLOG
    }
}
