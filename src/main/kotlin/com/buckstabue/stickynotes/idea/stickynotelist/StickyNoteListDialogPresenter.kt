package com.buckstabue.stickynotes.idea.stickynotelist

import com.buckstabue.stickynotes.base.BasePresenter
import com.buckstabue.stickynotes.idea.stickynotelist.di.PerStickyNoteListDialog
import javax.inject.Inject

@PerStickyNoteListDialog
class StickyNoteListDialogPresenter @Inject constructor(
) : BasePresenter<StickyNoteListDialogView>() {

}
