package com.buckstabue.stickynote.stickynotelist

import com.buckstabue.stickynote.base.BaseView

interface StickyNoteListView : BaseView {
    fun render(stickyNotes: List<StickyNoteViewModel>)
}
