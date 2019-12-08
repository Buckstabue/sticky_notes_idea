package com.buckstabue.stickynote.stickynotelist

import com.buckstabue.stickynote.*
import com.buckstabue.stickynote.base.BasePresenter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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
