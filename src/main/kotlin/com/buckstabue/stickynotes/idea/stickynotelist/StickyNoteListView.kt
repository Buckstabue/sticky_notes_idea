package com.buckstabue.stickynotes.idea.stickynotelist

import com.buckstabue.stickynotes.base.BaseView

interface StickyNoteListView : BaseView {
    fun render(viewModel: StickyNoteListViewModel)
    fun showHintUnderCursor(hintText: String)

    fun close()
}
