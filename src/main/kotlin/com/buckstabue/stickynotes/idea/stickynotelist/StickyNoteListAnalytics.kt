package com.buckstabue.stickynotes.idea.stickynotelist

import com.buckstabue.stickynotes.analytics.Analytics
import com.buckstabue.stickynotes.idea.stickynotelist.di.PerStickyNoteListDialog
import javax.inject.Inject

@PerStickyNoteListDialog
class StickyNoteListAnalytics @Inject constructor(
    private val analytics: Analytics
) {
    companion object {
        private const val CATEGORY = "StickyNoteList"
    }

    fun removeStickyNotesWithDeleteKey(count: Int) {
        analytics.sendEvent(
            category = CATEGORY,
            action = "remove-with-delete-key",
            label = "$count"
        )
    }
}
