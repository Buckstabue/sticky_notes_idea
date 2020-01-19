package com.buckstabue.stickynotes.idea.stickynotelist

import com.buckstabue.stickynotes.base.di.AppInjector
import com.buckstabue.stickynotes.idea.StickyNotesWebHelpProvider
import com.buckstabue.stickynotes.idea.forEachTab
import com.buckstabue.stickynotes.idea.minWidth
import com.buckstabue.stickynotes.idea.stickynotelist.panel.StickyNotesObservable
import com.buckstabue.stickynotes.idea.stickynotelist.panel.StickyNotesPanel
import com.intellij.openapi.Disposable
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.inject.Inject
import javax.swing.Action
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTabbedPane

class StickyNoteListDialog(
    project: Project,
    source: StickyNoteListAnalytics.Source
) : DialogWrapper(project), StickyNoteListDialogView {
    companion object {
        const val CONTROLLER_PROPERTY = "StickyNoteListDialog_Controller"
        private const val MIN_WIDTH = 400
        private val logger = Logger.getInstance(StickyNoteListDialog::class.java)
    }

    private lateinit var contentPanel: JPanel
    private lateinit var tabs: JTabbedPane

    @Inject
    lateinit var presenter: StickyNoteListDialogPresenter

    private val daggerComponent = AppInjector.getProjectComponent(project)
        .plusStickyNoteListDialogComponent().create(source)

    init {
        daggerComponent.inject(this)
        init()
        title = "Sticky Notes"
        peer.contentPane?.minWidth = MIN_WIDTH
        tabs.addChangeListener {
            presenter.onTabSelectionChanged(tabs.selectedIndex)
        }

        presenter.attachView(this)
    }

    override fun addCurrentBranchBacklogTab() {
        addTab(
            tabName = "Backlog",
            observableType = StickyNotesObservable.Type.CURRENT_BRANCH_BACKLOG
        )
    }

    private fun addTab(tabName: String, observableType: StickyNotesObservable.Type) {
        val stickyNotesPanel = StickyNotesPanel(
            parentDialog = this,
            stickyNoteListDialogComponent = daggerComponent,
            stickyNotesObservableType = observableType
        )
        stickyNotesPanel.getContentPanel()
            .putClientProperty(CONTROLLER_PROPERTY, stickyNotesPanel)
        tabs.addTab(
            tabName,
            null,
            stickyNotesPanel.getContentPanel()
        )
    }

    override fun addArchiveTab() {
        addTab(
            tabName = "Archive",
            observableType = StickyNotesObservable.Type.ARCHIVED
        )
    }

    override fun addAllBacklogTab() {
        addTab(
            tabName = "Backlog(all)",
            observableType = StickyNotesObservable.Type.ALL_BACKLOG
        )
    }

    override fun removeAllBacklogTab() {
        val allBacklogTabIndex = 2
        if (tabs.tabCount != 3) {
            logger.error(
                "Trying to remove 'All Backlog' tab " +
                        "while there are no exactly 3 tabs(${tabs.tabCount})"
            )
            return
        }
        tabs.removeTabAt(allBacklogTabIndex)
    }

    override fun setCurrentBranchBacklogTabTitle(title: String) {
        val currentBranchBacklogTabIndex = 0
        tabs.setTitleAt(currentBranchBacklogTabIndex, title)
    }

    override fun createActions(): Array<Action> {
        return emptyArray()
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return contentPanel // return non null to make close-on-escape work without any selection
    }

    override fun createCenterPanel(): JComponent? {
        return contentPanel
    }

    override fun dispose() {
        tabs.forEachTab {
            (it.getClientProperty(CONTROLLER_PROPERTY) as? Disposable)?.dispose()
        }
        presenter.detachView()
        super.dispose()
    }

    override fun getDimensionServiceKey(): String? {
        return "STICKY_NOTE_LIST_DIALOG"
    }

    override fun getHelpId(): String? {
        return StickyNotesWebHelpProvider.GITHUB_HELP_TOPIC_ID
    }
}

