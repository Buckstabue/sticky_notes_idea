package com.buckstabue.stickynotes.idea.stickynotelist

import com.buckstabue.stickynotes.StickyNoteInteractor
import com.buckstabue.stickynotes.base.BasePresenter
import com.buckstabue.stickynotes.idea.stickynotelist.di.PerStickyNoteListDialog
import com.intellij.openapi.diagnostic.Logger
import kotlinx.coroutines.channels.consumeEachIndexed
import javax.inject.Inject

@PerStickyNoteListDialog
class StickyNoteListDialogPresenter @Inject constructor(
    private val stickyNoteInteractor: StickyNoteInteractor,
    private val analytics: StickyNoteListAnalytics
) : BasePresenter<StickyNoteListDialogView>() {
    companion object {
        private val logger = Logger.getInstance(StickyNoteListDialogPresenter::class.java)

        private const val CURRENT_BRANCH_BACKLOG_TAB = 0
        private const val ARCHIVE_TAB = 1
        private const val ALL_BRANCH_BACKLOG_TAB = 2
    }

    private var isAllBacklogTabPresent = false
    private var tabsInitialized = false

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
                    tabsInitialized = true
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

    fun onTabSelectionChanged(selectedTabIndex: Int) {
        logChangeTabEvent(selectedTabIndex)
    }

    private fun logChangeTabEvent(selectedTabIndex: Int) {
        if (!tabsInitialized) {
            return
        }
        when (selectedTabIndex) {
            CURRENT_BRANCH_BACKLOG_TAB -> {
                val tab = if (isAllBacklogTabPresent) {
                    StickyNoteListAnalytics.Tab.CURRENT_BRANCH_BACKLOG
                } else {
                    StickyNoteListAnalytics.Tab.BACKLOG
                }
                analytics.tabSelectionChanged(tab)
            }
            ARCHIVE_TAB -> {
                analytics.tabSelectionChanged(StickyNoteListAnalytics.Tab.ARCHIVE)
            }
            ALL_BRANCH_BACKLOG_TAB -> {
                analytics.tabSelectionChanged(StickyNoteListAnalytics.Tab.ALL_BACKLOG)
            }
            else -> {
                logger.error("Cannot process selectedTabIndex = $selectedTabIndex")
            }
        }
    }
}
