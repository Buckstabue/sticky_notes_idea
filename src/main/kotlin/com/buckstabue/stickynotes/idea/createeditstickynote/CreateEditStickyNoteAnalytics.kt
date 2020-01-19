package com.buckstabue.stickynotes.idea.createeditstickynote

import com.buckstabue.stickynotes.analytics.Analytics
import com.buckstabue.stickynotes.idea.createeditstickynote.di.PerCreateEditStickyNote
import javax.inject.Inject

@PerCreateEditStickyNote
class CreateEditStickyNoteAnalytics @Inject constructor(
    private val analytics: Analytics,
    private val mode: CreateEditStickyNoteViewModel.Mode,
    private val source: Source
) {

    fun present() {
        analytics.sendEvent(
            category = mode.analyticsCategory,
            action = "present",
            label = source.analyticsValue
        )
    }

    enum class Source(
        internal val analyticsValue: String
    ) {
        ACTIVE_STICKY_NOTE("active-sticky-note"),
        CONTEXT_MENU("context-menu"),
        GUTTER("gutter"),
        STICKY_NOTE_LIST("sticky-note-list")
    }
}
