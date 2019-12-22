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

    /**
     * @return range of indices of the moved sticky notes
     */
    suspend fun moveStickyNotes(stickyNotes: List<StickyNote>, insertionIndex: Int): IntRange {
        require(insertionIndex >= 0) {
            "Cannot insert at a negative position ($insertionIndex)"
        }
        require(stickyNotes.isNotEmpty()) {
            "Sticky Note list is empty, nothing to move"
        }
        val isWorkingWithArchived = stickyNotes.first().isArchived
        val isStickyNotesHomogeneousByArchiveStatus =
            stickyNotes.all { it.isArchived == isWorkingWithArchived }
        require(isStickyNotesHomogeneousByArchiveStatus) {
            "Sticky Note list must contain either archived or backlog items, not mixed ones"
        }

        val targetList =
            if (isWorkingWithArchived) {
                stickyNoteRepository.getArchivedStickyNotes()
            } else {
                stickyNoteRepository.getBacklogStickyNotes()
            }.toMutableList()
        require(targetList.containsAll(stickyNotes)) {
            "Some of moved elements are not part of the actual sticky note list"
        }
        val movedStickyNotesIndicesRange =
            moveStickyNotes(targetList, stickyNotes, insertionIndex)
        if (isWorkingWithArchived) {
            stickyNoteRepository.setArchivedStickyNotes(targetList)
        } else {
            stickyNoteRepository.setBacklogStickyNotes(targetList)
        }
        return movedStickyNotesIndicesRange
    }

    /**
     * @return range of indices of the moved sticky notes
     */
    private fun moveStickyNotes(
        targetList: MutableList<StickyNote>,
        movedStickyNotes: List<StickyNote>,
        insertionIndex: Int
    ): IntRange {
        val indexToInsertAfterRemoving =
            calculateIndexToInsertAfterRemovingToMoveElements(targetList, movedStickyNotes, insertionIndex)
        targetList.removeAll(movedStickyNotes)
        // if multiple elements selected preserve order of the source list
        val stickyNotesToInsert = movedStickyNotes.sortedBy { targetList.indexOf(it) }
        targetList.addAll(indexToInsertAfterRemoving, stickyNotesToInsert)
        return indexToInsertAfterRemoving until indexToInsertAfterRemoving + movedStickyNotes.size
    }

    /**
     * Insertion index may changed after deletion of moved elements from a source list.
     * E.g. ["a", "b", "c"], we want to move "a" to index 2, but before moving we delete "a" from the collection and
     * we have ["b", "c"], so the actual insertion position is 1, not 2. To calculate that we simply count how many
     * elements are placed before the desired position and we subtract this count from the desired insertion position
     */
    private fun calculateIndexToInsertAfterRemovingToMoveElements(
        targetList: List<StickyNote>,
        movedStickyNotes: List<StickyNote>,
        desiredIndexPosition: Int
    ): Int {
        val numberOfElementsBeforeDesiredIndexPosition = targetList.subList(0, desiredIndexPosition)
            .count { movedStickyNotes.contains(it) }
        return desiredIndexPosition - numberOfElementsBeforeDesiredIndexPosition
    }

    suspend fun updateStickyNoteDescription(stickyNote: StickyNote, newDescription: String) {
        stickyNoteRepository.updateStickyNoteDescription(stickyNote, newDescription)
    }
}
