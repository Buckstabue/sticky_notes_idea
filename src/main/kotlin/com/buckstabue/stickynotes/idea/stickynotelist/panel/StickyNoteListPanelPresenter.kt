package com.buckstabue.stickynotes.idea.stickynotelist.panel

import com.buckstabue.stickynotes.FileBoundStickyNote
import com.buckstabue.stickynotes.NonBoundStickyNote
import com.buckstabue.stickynotes.StickyNote
import com.buckstabue.stickynotes.StickyNoteInteractor
import com.buckstabue.stickynotes.base.BasePresenter
import com.buckstabue.stickynotes.idea.stickynotelist.getstickynotesstrategy.StickyNotesObservable
import com.buckstabue.stickynotes.idea.stickynotelist.panel.di.PerStickyNoteListPanel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.map
import javax.inject.Inject

@PerStickyNoteListPanel
class StickyNoteListPanelPresenter @Inject constructor(
    private val stickyNotesObservable: StickyNotesObservable,
    private val stickyNoteInteractor: StickyNoteInteractor,
    private val stickyNoteIconProvider: StickyNoteIconProvider
) : BasePresenter<StickyNotesPanelView>() {
    override fun onViewAttached() {
        super.onViewAttached()
        launch {
            stickyNotesObservable.observeStickyNotes()
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
                description = getDescription(it),
                icon = stickyNoteIconProvider.getIcon(it),
                stickyNote = it
            )
        }
    }

    private fun getDescription(stickyNote: StickyNote): String {
        if (stickyNote.boundBranchName.isNullOrBlank()) {
            return stickyNote.description
        }
        return "${stickyNote.description} (bound to ${stickyNote.boundBranchName})"
    }

    fun onItemOpened(item: StickyNoteViewModel) {
        when (val stickyNote = item.stickyNote) {
            is NonBoundStickyNote -> {
                view?.showHintUnderCursor(
                    "Cannot open a sticky note that is not bound to any file"
                        .replace(" ", "&nbsp;")
                )
            }
            is FileBoundStickyNote -> {
                view?.close()
                stickyNoteInteractor.openStickyNote(stickyNote)
            }
        }
    }
}
