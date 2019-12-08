package com.buckstabue.stickynote.stickynotelist

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
                    description = it.description
                )
            }
        view?.render(stickyNotes)
    }

    fun onBackButtonClick() {
        router.openActiveStickyNote()
    }
}
