package com.buckstabue.stickynotes.idea.toolwindow.stickynotelist

import com.buckstabue.stickynotes.StickyNote
import javax.swing.Icon

data class StickyNoteViewModel(
    val description: String,
    val icon: Icon?,
    val stickyNote: StickyNote
)
