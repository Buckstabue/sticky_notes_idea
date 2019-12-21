package com.buckstabue.stickynotes.toolwindow.stickynotelist

import com.buckstabue.stickynotes.base.BaseView

interface StickyNoteListView : BaseView {
    fun render(viewModel: StickyNoteListViewModel)
    fun showHintUnderCursor(hintText: String)
}
