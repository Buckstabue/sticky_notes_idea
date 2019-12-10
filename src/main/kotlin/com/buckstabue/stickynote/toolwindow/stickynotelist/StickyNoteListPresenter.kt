package com.buckstabue.stickynote.toolwindow.stickynotelist

import com.buckstabue.stickynote.FileBoundStickyNote
import com.buckstabue.stickynote.NonBoundStickyNote
import com.buckstabue.stickynote.StickyNote
import com.buckstabue.stickynote.StickyNoteInteractor
import com.buckstabue.stickynote.base.BasePresenter
import com.buckstabue.stickynote.toolwindow.PerToolWindow
import com.buckstabue.stickynote.toolwindow.StickyNoteRouter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.map
import javax.inject.Inject

@PerToolWindow
class StickyNoteListPresenter @Inject constructor(
    private val router: StickyNoteRouter,
    private val stickyNoteInteractor: StickyNoteInteractor
) : BasePresenter<StickyNoteListView>() {

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    override fun onViewAttached() {
        super.onViewAttached()
        launch {
            stickyNoteInteractor.observeStickyNotes()
                .map { stickyNotes ->
                    toViewModels(stickyNotes)
                }.consumeEach {
                    view?.render(it)
                }
        }
    }

    private fun toViewModels(stickyNotes: List<StickyNote>): List<StickyNoteViewModel> {
        return stickyNotes.map {
            StickyNoteViewModel(
                description = it.description,
                stickyNote = it
            )
        }
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