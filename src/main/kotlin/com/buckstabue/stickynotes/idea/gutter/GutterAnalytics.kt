package com.buckstabue.stickynotes.idea.gutter

import com.buckstabue.stickynotes.analytics.Analytics
import com.buckstabue.stickynotes.idea.gutter.di.PerGutter
import javax.inject.Inject

@PerGutter
class GutterAnalytics @Inject constructor(
    private val analytics: Analytics
) {
    companion object {
        private const val CATEGORY = "Gutter"
    }

    fun editStickyNote() {
        analytics.sendEvent(
            category = CATEGORY,
            action = "edit-sticky-note"
        )
    }
}
