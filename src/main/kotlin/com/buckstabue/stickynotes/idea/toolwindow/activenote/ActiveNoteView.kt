package com.buckstabue.stickynotes.idea.toolwindow.activenote

import com.buckstabue.stickynotes.base.BaseView

interface ActiveNoteView : BaseView {
    fun render(viewModel: ActiveStickyNoteViewModel)
    fun showHintUnderCursor(text: String)
}
