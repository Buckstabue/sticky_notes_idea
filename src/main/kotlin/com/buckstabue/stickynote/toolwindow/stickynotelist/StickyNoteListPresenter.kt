package com.buckstabue.stickynote.toolwindow.stickynotelist

import com.buckstabue.stickynote.FileBoundStickyNote
import com.buckstabue.stickynote.NonBoundStickyNote
import com.buckstabue.stickynote.StickyNote
import com.buckstabue.stickynote.StickyNoteInteractor
import com.buckstabue.stickynote.base.BasePresenter
import com.buckstabue.stickynote.toolwindow.PerToolWindow
import com.buckstabue.stickynote.toolwindow.StickyNoteRouter
import com.intellij.icons.AllIcons
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.map
import javax.inject.Inject
import javax.swing.Icon

@PerToolWindow
class StickyNoteListPresenter @Inject constructor(
    private val router: StickyNoteRouter,
    private val stickyNoteInteractor: StickyNoteInteractor
) : BasePresenter<StickyNoteListView>() {

    private var viewModel = StickyNoteListViewModel(
        backlogStickyNotes = emptyList(),
        archiveStickyNotes = emptyList()
    )

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    override fun onViewAttached() {
        super.onViewAttached()
        launch {
            stickyNoteInteractor.observeArchivedStickyNotes()
                .map { stickyNotes ->
                    toViewModels(stickyNotes)
                }.consumeEach {
                    viewModel = viewModel.copy(archiveStickyNotes = it)
                    view?.render(viewModel)
                }
        }

        launch {
            stickyNoteInteractor.observeBacklogStickyNotes()
                .map { stickyNotes ->
                    toViewModels(stickyNotes)
                }.consumeEach {
                    viewModel = viewModel.copy(backlogStickyNotes = it)
                    view?.render(viewModel)
                }
        }
    }

    private fun toViewModels(stickyNotes: List<StickyNote>): List<StickyNoteViewModel> {
        return stickyNotes.map {
            StickyNoteViewModel(
                description = it.description,
                icon = getIcon(it),
                stickyNote = it
            )
        }
    }

    private fun getIcon(stickyNote: StickyNote): Icon? {
        return when (stickyNote) {
            is NonBoundStickyNote -> AllIcons.General.Note
            is FileBoundStickyNote -> AllIcons.Actions.ShowCode
        }
    }

    fun onBackButtonClick() {
        router.openActiveStickyNote()
    }

    fun onItemOpened(item: StickyNoteViewModel) {
        when (val stickyNote = item.stickyNote) {
            is NonBoundStickyNote ->
                view?.showHintUnderCursor(
                    "Cannot open a sticky note that is not bound to any file"
                        .replace(" ", "&nbsp;")
                )
            is FileBoundStickyNote -> stickyNoteInteractor.openStickyNote(stickyNote)
        }
    }
}
