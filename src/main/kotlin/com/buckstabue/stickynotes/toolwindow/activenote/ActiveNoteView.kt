package com.buckstabue.stickynotes.toolwindow.activenote

import com.buckstabue.stickynotes.base.BaseView

interface ActiveNoteView : BaseView {
    fun render(viewModel: ActiveStickyNoteViewModel)
}
