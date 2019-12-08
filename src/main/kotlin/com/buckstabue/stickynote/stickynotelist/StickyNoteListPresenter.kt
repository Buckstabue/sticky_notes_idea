package com.buckstabue.stickynote.stickynotelist

import com.buckstabue.stickynote.FileBoundStickyNote
import com.buckstabue.stickynote.NonBoundStickyNote
import com.buckstabue.stickynote.StickyNoteInteractor
import com.buckstabue.stickynote.StickyNoteRouter
import com.buckstabue.stickynote.base.BasePresenter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StickyNoteListPresenter @Inject constructor(
    private val router: StickyNoteRouter,
    private val stickyNoteInteractor: StickyNoteInteractor
) : BasePresenter<StickyNoteListView>() {
    override fun onViewAttached() {
        super.onViewAttached()
        val stickyNotes = stickyNoteInteractor.getStickyNotes()
            .map {
                StickyNoteViewModel(
                    description = it.description,
                    stickyNote = it
                )
            }
        view?.render(stickyNotes)
    }

    fun onBackButtonClick() {
        router.openActiveStickyNote()
    }

    fun onItemSelected(item: StickyNoteViewModel) {
        when (val stickyNote = item.stickyNote) {
            is NonBoundStickyNote -> TODO()
            is FileBoundStickyNote -> stickyNoteInteractor.openStickyNote(stickyNote)
        }
    }
}
