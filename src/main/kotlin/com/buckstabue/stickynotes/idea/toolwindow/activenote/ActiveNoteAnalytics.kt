package com.buckstabue.stickynotes.idea.toolwindow.activenote

import com.buckstabue.stickynotes.analytics.Analytics
import com.buckstabue.stickynotes.idea.toolwindow.di.PerToolWindow
import javax.inject.Inject

@PerToolWindow
class ActiveNoteAnalytics @Inject constructor(
    private val analytics: Analytics
) {
    companion object {
        private const val TOOL_WINDOW_CATEGORY = "ActiveNoteToolWindow"
    }

    fun firstOpen() {
        analytics.sendEvent(
            category = TOOL_WINDOW_CATEGORY,
            action = "first_open"
        )
    }

    fun toolWindowAdded() {
        analytics.sendEvent(
            category = TOOL_WINDOW_CATEGORY,
            action = "added"
        )
    }

    fun toolWindowExpanded() {
        analytics.sendEvent(
            category = TOOL_WINDOW_CATEGORY,
            action = "expanded"
        )
    }

    fun toolWindowCollapsed() {
        analytics.sendEvent(
            category = TOOL_WINDOW_CATEGORY,
            action = "collapsed"
        )
    }

    fun toolWindowRemoved() {
        analytics.sendEvent(
            category = TOOL_WINDOW_CATEGORY,
            action = "removed"
        )
    }

    fun doneClicked() {
        analytics.sendEvent(
            category = TOOL_WINDOW_CATEGORY,
            action = "done-click"
        )
    }

    fun toolWindowPresent() {
        analytics.sendEvent(
            category = TOOL_WINDOW_CATEGORY,
            action = "present"
        )
    }

    fun goToCode() {
        analytics.sendEvent(
            category = TOOL_WINDOW_CATEGORY,
            action = "go-to-code"
        )
    }
}
