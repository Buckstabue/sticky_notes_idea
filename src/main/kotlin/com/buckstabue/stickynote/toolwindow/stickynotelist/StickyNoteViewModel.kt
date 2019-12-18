package com.buckstabue.stickynote.toolwindow.stickynotelist

import com.buckstabue.stickynote.StickyNote
import javax.swing.Icon

data class StickyNoteViewModel(
    val description: String,
    val icon: Icon?,
    val stickyNote: StickyNote
)
