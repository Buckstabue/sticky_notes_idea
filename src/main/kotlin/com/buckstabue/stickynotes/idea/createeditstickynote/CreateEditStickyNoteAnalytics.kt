package com.buckstabue.stickynotes.idea.createeditstickynote

import com.buckstabue.stickynotes.analytics.Analytics
import com.buckstabue.stickynotes.idea.createeditstickynote.di.PerCreateEditStickyNote
import javax.inject.Inject

@PerCreateEditStickyNote
class CreateEditStickyNoteAnalytics @Inject constructor(
    private val analytics: Analytics,
    private val source: Source,
    mode: CreateEditStickyNoteViewModel.Mode
) {
    private val category = mode.analyticsCategory

    fun present() {
        analytics.sendEvent(
            category = category,
            action = "present",
            label = source.analyticsValue
        )
    }

    fun validationError(errorMessage: String) {
        analytics.sendEvent(
            category = category,
            action = "validation-failed",
            label = errorMessage
        )
    }

    fun cancel() {
        analytics.sendEvent(
            category = category,
            action = "cancel",
            label = source.analyticsValue
        )
    }

    fun ok() {
        analytics.sendEvent(
            category = category,
            action = "ok",
            label = source.analyticsValue
        )
    }

    fun help() {
        analytics.sendEvent(
            category = category,
            action = "help",
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
