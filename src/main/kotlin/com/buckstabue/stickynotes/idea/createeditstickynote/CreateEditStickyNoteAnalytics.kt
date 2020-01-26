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
            source = source.analyticsValue
        )
    }

    fun validationError(errorMessage: String) {
        analytics.sendEvent(
            category = category,
            action = "validation-failed",
            source = source.analyticsValue,
            label = errorMessage
        )
    }

    fun cancel() {
        analytics.sendEvent(
            category = category,
            action = "cancel",
            source = source.analyticsValue
        )
    }

    fun ok() {
        analytics.sendEvent(
            category = category,
            action = "ok",
            source = source.analyticsValue
        )
    }

    fun help() {
        analytics.sendEvent(
            category = category,
            action = "help",
            source = source.analyticsValue
        )
    }

    fun bindToCodeSelected(
        isChecked: Boolean
    ) {
        analytics.sendEvent(
            category = category,
            action = "bind-to-code-selected",
            source = source.analyticsValue,
            label = "isChecked: $isChecked"
        )
    }

    fun setActive(
        isChecked: Boolean
    ) {
        analytics.sendEvent(
            category = category,
            action = "set-active",
            source = source.analyticsValue,
            label = "isChecked: $isChecked"
        )
    }

    fun bindToCurrentBranch(
        isChecked: Boolean
    ) {
        analytics.sendEvent(
            category = category,
            action = "bind-to-current-branch",
            source = source.analyticsValue,
            label = "isChecked: $isChecked"
        )
    }

    fun editDescription() {
        analytics.sendEvent(
            category = category,
            action = "edit-description",
            source = source.analyticsValue
        )
    }

    enum class Source(
        internal val analyticsValue: String
    ) {
        ACTIVE_STICKY_NOTE_TOOLBAR("active-sticky-note-toolbar"),
        ACTIVE_STICKY_NOTE_LINK("active-sticky-note-link"),
        CONTEXT_MENU("context-menu"),
        GUTTER("gutter"),
        STICKY_NOTE_LIST_CONTEXT_MENU("sticky-note-list-context-menu"),
        STICKY_NOTE_LIST_LINK("sticky-note-list-link")
    }
}
