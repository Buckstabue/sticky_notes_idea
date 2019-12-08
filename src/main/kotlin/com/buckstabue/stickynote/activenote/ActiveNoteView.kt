package com.buckstabue.stickynote.activenote

import com.buckstabue.stickynote.base.BaseView

interface ActiveNoteView : BaseView {
    fun render(viewModel: ActiveStickyNoteViewModel)
}
