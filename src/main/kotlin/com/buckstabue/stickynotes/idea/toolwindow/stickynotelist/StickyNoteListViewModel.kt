package com.buckstabue.stickynotes.idea.toolwindow.stickynotelist

data class StickyNoteListViewModel(
    val backlogStickyNotes: List<StickyNoteViewModel>,
    val archiveStickyNotes: List<StickyNoteViewModel>
)
