package com.buckstabue.stickynotes.idea.stickynotelist

import com.buckstabue.stickynotes.base.di.AppInjector
import com.buckstabue.stickynotes.idea.StickyNotesWebHelpProvider
import com.buckstabue.stickynotes.idea.VerticalBorder
import com.buckstabue.stickynotes.idea.addOnActionListener
import com.buckstabue.stickynotes.idea.addOnPopupActionListener
import com.buckstabue.stickynotes.idea.stickynotelist.contextmenu.ArchiveStickyNoteAction
import com.buckstabue.stickynotes.idea.stickynotelist.contextmenu.EditStickyNoteAction
import com.buckstabue.stickynotes.idea.stickynotelist.contextmenu.MoveStickyNoteToBacklogAction
import com.buckstabue.stickynotes.idea.stickynotelist.contextmenu.RemoveStickyNoteAction
import com.buckstabue.stickynotes.idea.stickynotelist.contextmenu.SetStickyNoteActiveAction
import com.intellij.codeInsight.hint.HintManager
import com.intellij.codeInsight.hint.HintUtil
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.Separator
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.awt.RelativePoint
import javax.inject.Inject
import javax.swing.Action
import javax.swing.DropMode
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.JPanel

class StickyNoteListDialog(
    project: Project
) : DialogWrapper(project), StickyNoteListView {

    private lateinit var contentPanel: JPanel
    private lateinit var filterByCurrentBranchCheckbox: JCheckBox
    private lateinit var backlogStickyNoteList: JList<StickyNoteViewModel>
    private lateinit var archivedStickyNoteList: JList<StickyNoteViewModel>

    @Inject
    lateinit var presenter: StickyNoteListPresenter

    init {
        AppInjector.getProjectComponent(project).inject(this)
        init()
        title = "Sticky Notes"

        filterByCurrentBranchCheckbox.border = VerticalBorder(top = 8)
        filterByCurrentBranchCheckbox.addActionListener {
            presenter.onFilterByCurrentCheckboxChanged(filterByCurrentBranchCheckbox.isSelected)
        }
        backlogStickyNoteList.addOnPopupActionListener(createContextMenuActions(backlogStickyNoteList))
        backlogStickyNoteList.addOnActionListener {
            presenter.onItemOpened(it)
        }
        backlogStickyNoteList.model = StickyNoteListModel(emptyList())
        backlogStickyNoteList.cellRenderer = StickyNoteListCellRenderer()
        setupDragAndDrop(backlogStickyNoteList)

        archivedStickyNoteList.addOnPopupActionListener(createContextMenuActions(archivedStickyNoteList))
        archivedStickyNoteList.addOnActionListener {
            presenter.onItemOpened(it)
        }
        archivedStickyNoteList.model = StickyNoteListModel(emptyList())
        archivedStickyNoteList.cellRenderer = StickyNoteListCellRenderer()
        setupDragAndDrop(archivedStickyNoteList)

        presenter.attachView(this)
    }

    override fun showHintUnderCursor(hintText: String) {
        val mousePosition = contentPanel.mousePosition ?: return
        val hintComponent = HintUtil.createInformationLabel(hintText)
        HintManager.getInstance()
            .showHint(
                hintComponent,
                RelativePoint(contentPanel, mousePosition),
                HintManager.HIDE_BY_ANY_KEY or HintManager.HIDE_BY_TEXT_CHANGE,
                -1
            )
    }

    override fun close() {
        close(OK_EXIT_CODE)
    }

    private fun setupDragAndDrop(stickNoteList: JList<StickyNoteViewModel>) {
        stickNoteList.dragEnabled = true
        stickNoteList.transferHandler = StickyNoteTransferHandler()
        stickNoteList.dropMode = DropMode.INSERT
    }

    private fun createContextMenuActions(stickNoteList: JList<StickyNoteViewModel>): ActionGroup {
        return DefaultActionGroup(
            SetStickyNoteActiveAction(stickNoteList),
            EditStickyNoteAction(stickNoteList),
            Separator.getInstance(),
            ArchiveStickyNoteAction(stickNoteList),
            MoveStickyNoteToBacklogAction(stickNoteList),
            Separator.getInstance(),
            RemoveStickyNoteAction(stickNoteList)
        )
    }

    override fun render(viewModel: StickyNoteListViewModel) {
        backlogStickyNoteList.model = StickyNoteListModel(viewModel.backlogStickyNotes)
        archivedStickyNoteList.model = StickyNoteListModel(viewModel.archiveStickyNotes)
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

