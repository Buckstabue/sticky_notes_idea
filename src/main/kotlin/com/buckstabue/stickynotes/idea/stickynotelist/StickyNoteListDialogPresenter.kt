package com.buckstabue.stickynotes.idea.stickynotelist

import com.buckstabue.stickynotes.StickyNoteInteractor
import com.buckstabue.stickynotes.base.BasePresenter
import com.buckstabue.stickynotes.idea.stickynotelist.di.PerStickyNoteListDialog
import kotlinx.coroutines.channels.consumeEachIndexed
import javax.inject.Inject

@PerStickyNoteListDialog
class StickyNoteListDialogPresenter @Inject constructor(
    private val stickyNoteInteractor: StickyNoteInteractor,
    private val analytics: StickyNoteListAnalytics
) : BasePresenter<StickyNoteListDialogView>() {

    private var isAllBacklogTabPresent = false

    override fun onViewAttached() {
        launch {
            stickyNoteInteractor.observeBacklogStickyNotes(currentBranchRelatedOnly = false)
                .consumeEachIndexed {
                    if (it.index == 0) { // got initial value
                        addDefaultTabs()
                    }
                    val hasStickyNoteBoundToAnyBranch = it.value.any { it.isBoundToBranch }
                    when {
                        hasStickyNoteBoundToAnyBranch && !isAllBacklogTabPresent ->
                            addAllBacklogTab()
                        !hasStickyNoteBoundToAnyBranch && isAllBacklogTabPresent ->
                            removeAllBacklogTab()

                    }
                }
        }
        analytics.present()
    }

    private fun addDefaultTabs() {
        view?.addCurrentBranchBacklogTab()
        view?.addArchiveTab()
    }

    private fun addAllBacklogTab() {
        view?.setCurrentBranchBacklogTabTitle("Backlog(current branch)")
        view?.addAllBacklogTab()
        isAllBacklogTabPresent = true
    }

    private fun removeAllBacklogTab() {
        view?.removeAllBacklogTab()
        view?.setCurrentBranchBacklogTabTitle("Backlog")
        isAllBacklogTabPresent = false
    }
}
