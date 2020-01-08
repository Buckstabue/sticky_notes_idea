package com.buckstabue.stickynotes.idea.stickynotelist.panel

import com.buckstabue.stickynotes.base.BaseView

interface StickyNotesPanelView : BaseView {
    fun render(viewModels: List<StickyNoteViewModel>)
    fun showHintUnderCursor(hintText: String)
    fun close()
}
