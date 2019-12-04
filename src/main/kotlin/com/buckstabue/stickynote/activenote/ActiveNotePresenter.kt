package com.buckstabue.stickynote.activenote

import com.buckstabue.stickynote.base.BasePresenter
import javax.inject.Inject

@PerActiveNote
class ActiveNotePresenter @Inject constructor(
) : BasePresenter<ActiveNoteView>() {
    override fun onViewAttached() {
        super.onViewAttached()
    }
}
