package com.buckstabue.stickynote.toolwindow.activenote

import com.buckstabue.stickynote.base.BaseView

interface ActiveNoteView : BaseView {
    fun render(viewModel: ActiveStickyNoteViewModel)
}
