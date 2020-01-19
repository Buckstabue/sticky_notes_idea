package com.buckstabue.stickynotes.idea.stickynotelist

import com.buckstabue.stickynotes.analytics.Analytics
import com.buckstabue.stickynotes.idea.stickynotelist.di.PerStickyNoteListDialog
import javax.inject.Inject

@PerStickyNoteListDialog
class StickyNoteListAnalytics @Inject constructor(
    private val analytics: Analytics,
    private val source: Source
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

    fun present() {
        analytics.sendEvent(
            category = CATEGORY,
            action = "present",
            label = source.analyticsValue
        )
    }

    /**
     * Where Sticky Note list is open from
     */
    enum class Source(
        internal val analyticsValue: String
    ) {
        ACTIVE_STICKY_NOTE("active-sticky-note"),
        CONTEXT_MENU("context-menu")
    }
}
