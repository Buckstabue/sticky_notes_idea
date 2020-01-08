package com.buckstabue.stickynotes.idea.stickynotelist

import com.buckstabue.stickynotes.base.BaseView

interface StickyNoteListDialogView : BaseView {
    fun addCurrentBranchBacklogTab()
    fun addArchiveTab()

    fun addAllBacklogTab()
    fun removeAllBacklogTab()

    fun setCurrentBranchBacklogTabTitle(title: String)
}
