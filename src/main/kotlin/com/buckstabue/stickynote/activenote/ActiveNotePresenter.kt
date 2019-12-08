package com.buckstabue.stickynote.activenote

import com.buckstabue.stickynote.StickyNote
import com.buckstabue.stickynote.StickyNoteInteractor
import com.buckstabue.stickynote.StickyNoteRouter
import com.buckstabue.stickynote.base.BasePresenter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import javax.inject.Inject

@PerActiveNote
class ActiveNotePresenter @Inject constructor(
    private val router: StickyNoteRouter,
    private val stickyNoteInteractor: StickyNoteInteractor
) : BasePresenter<ActiveNoteView>() {

    private var activeStickyNote: StickyNote? = null

    @ExperimentalCoroutinesApi
    override fun onViewAttached() {
        super.onViewAttached()

        launch {
            stickyNoteInteractor.observeActiveStickyNote()
                .consumeEach {
                    activeStickyNote = it
                    val viewModel = ActiveStickyNoteViewModel(
                        activeNoteDescription = generateActiveNoteDescription(it),
                        showDoneButton = it != null
                    )
                    view?.render(viewModel)
                }
        }

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

    fun onDoneClick() {
        val activeStickyNote = activeStickyNote
        requireNotNull(activeStickyNote)
        launch {
            stickyNoteInteractor.setStickyNoteDone(activeStickyNote)
        }
    }
}
