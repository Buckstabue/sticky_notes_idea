package com.buckstabue.stickynote.activenote

import com.buckstabue.stickynote.StickyNote
import com.buckstabue.stickynote.StickyNoteInteractor
import com.buckstabue.stickynote.StickyNoteRouter
import com.buckstabue.stickynote.base.BasePresenter
import javax.inject.Inject

@PerActiveNote
class ActiveNotePresenter @Inject constructor(
    private val router: StickyNoteRouter,
    private val stickyNoteInteractor: StickyNoteInteractor
) : BasePresenter<ActiveNoteView>() {
    override fun onViewAttached() {
        super.onViewAttached()
        val activeStickyNote = stickyNoteInteractor.getActiveStickyNote()
        view?.render(
            ActiveStickyNoteViewModel(
                activeNoteDescription = generateActiveNoteDescription(activeStickyNote)
            )
        )
    }

    private fun generateActiveNoteDescription(activeNote: StickyNote?): String {
        if (activeNote == null) {
            return "No sticky note found. Add a new one with the context menu"
        }
        return activeNote.description
    }

    fun onGotoStickyNoteListClick() {
        router.openStickyNotesList()
    }
}
