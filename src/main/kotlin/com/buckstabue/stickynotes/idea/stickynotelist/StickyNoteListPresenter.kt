package com.buckstabue.stickynotes.idea.stickynotelist

import com.buckstabue.stickynotes.FileBoundStickyNote
import com.buckstabue.stickynotes.NonBoundStickyNote
import com.buckstabue.stickynotes.StickyNote
import com.buckstabue.stickynotes.StickyNoteInteractor
import com.buckstabue.stickynotes.base.BasePresenter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.map
import javax.inject.Inject

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class StickyNoteListPresenter @Inject constructor(
    private val stickyNoteInteractor: StickyNoteInteractor,
    private val stickyNoteIconProvider: StickyNoteIconProvider
) : BasePresenter<StickyNoteListView>() {

    private var viewModel = StickyNoteListViewModel(
        backlogStickyNotes = emptyList(),
        archiveStickyNotes = emptyList()
    )

    private var observeBacklogJob: Job? = null

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

        observeBacklogStickyNotes(filterByCurrentBranch = true)
    }

    private fun observeBacklogStickyNotes(filterByCurrentBranch: Boolean) {
        observeBacklogJob?.cancel()
        observeBacklogJob = launch {
            stickyNoteInteractor.observeBacklogStickyNotes(currentBranchRelatedOnly = filterByCurrentBranch)
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
                icon = stickyNoteIconProvider.getIcon(it),
                stickyNote = it
            )
        }
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

    fun onFilterByCurrentCheckboxChanged(filterByCurrentBranch: Boolean) {
        observeBacklogStickyNotes(filterByCurrentBranch)
    }
}
