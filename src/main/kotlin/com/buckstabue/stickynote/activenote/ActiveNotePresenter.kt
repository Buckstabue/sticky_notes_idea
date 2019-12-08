package com.buckstabue.stickynote.activenote

import com.buckstabue.stickynote.StickyNoteRouter
import com.buckstabue.stickynote.base.BasePresenter
import javax.inject.Inject

@PerActiveNote
class ActiveNotePresenter @Inject constructor(
    private val router: StickyNoteRouter
) : BasePresenter<ActiveNoteView>() {
    override fun onViewAttached() {
        super.onViewAttached()
    }

    fun onGotoStickyNoteListClick() {
        router.openStickyNotesList()
    }
}
