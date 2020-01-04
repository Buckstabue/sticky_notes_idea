package com.buckstabue.stickynotes.idea.stickynotelist

data class StickyNoteListViewModel(
    val backlogStickyNotes: List<StickyNoteViewModel>,
    val archiveStickyNotes: List<StickyNoteViewModel>
)
