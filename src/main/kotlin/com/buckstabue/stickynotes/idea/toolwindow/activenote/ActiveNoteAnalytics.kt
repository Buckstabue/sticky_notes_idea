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

    fun toolWindowAdded() {
        analytics.sendEvent(
            category = TOOL_WINDOW_CATEGORY,
            action = "added-active-note-window"
        )
    }

    fun toolWindowExpanded() {
        analytics.sendEvent(
            category = TOOL_WINDOW_CATEGORY,
            action = "expanded-active-note-window"
        )
    }

    fun toolWindowCollapsed() {
        analytics.sendEvent(
            category = TOOL_WINDOW_CATEGORY,
            action = "collapsed-active-note-window"
        )
    }

    fun toolWindowRemoved() {
        analytics.sendEvent(
            category = TOOL_WINDOW_CATEGORY,
            action = "removed-active-note-window"
        )
    }
}
