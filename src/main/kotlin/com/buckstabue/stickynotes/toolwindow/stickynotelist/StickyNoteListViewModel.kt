package com.buckstabue.stickynotes.toolwindow.stickynotelist

data class StickyNoteListViewModel(
    val backlogStickyNotes: List<StickyNoteViewModel>,
    val archiveStickyNotes: List<StickyNoteViewModel>
)
