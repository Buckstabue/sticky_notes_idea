package com.buckstabue.stickynotes.idea.toolwindow.stickynotelist

import com.buckstabue.stickynotes.idea.BaseWindow
import com.buckstabue.stickynotes.idea.addOnActionListener
import com.buckstabue.stickynotes.idea.addOnPopupActionListener
import com.buckstabue.stickynotes.idea.toolwindow.StickyNoteToolWindowComponent
import com.buckstabue.stickynotes.idea.toolwindow.stickynotelist.contextmenu.ArchiveStickyNoteAction
import com.buckstabue.stickynotes.idea.toolwindow.stickynotelist.contextmenu.EditStickyNoteAction
import com.buckstabue.stickynotes.idea.toolwindow.stickynotelist.contextmenu.MoveStickyNoteToBacklogAction
import com.buckstabue.stickynotes.idea.toolwindow.stickynotelist.contextmenu.RemoveStickyNoteAction
import com.buckstabue.stickynotes.idea.toolwindow.stickynotelist.contextmenu.SetStickyNoteActiveAction
import com.intellij.codeInsight.hint.HintManager
import com.intellij.codeInsight.hint.HintUtil
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.Separator
import com.intellij.ui.awt.RelativePoint
import javax.inject.Inject
import javax.swing.DropMode
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.JPanel

class StickyNoteListWindow : BaseWindow<StickyNoteListView, StickyNoteListPresenter>(), StickyNoteListView {
    private lateinit var backlogStickyNoteList: JList<StickyNoteViewModel>
    private lateinit var archivedStickyNoteList: JList<StickyNoteViewModel>
    private lateinit var contentPanel: JPanel
    private lateinit var backButton: JButton

    override val routingTag: String = "StickyNoteList"

    @Inject
    override lateinit var presenter: StickyNoteListPresenter

    init {
        backlogStickyNoteList.addOnPopupActionListener(createBacklogStickyNoteActions(backlogStickyNoteList))
        backlogStickyNoteList.addOnActionListener {
            presenter.onItemOpened(it)
        }
        backlogStickyNoteList.model = StickyNoteListModel(emptyList())
        backlogStickyNoteList.cellRenderer = StickyNoteListCellRenderer()
        setupDragAndDrop(backlogStickyNoteList)

        archivedStickyNoteList.addOnPopupActionListener(createBacklogStickyNoteActions(archivedStickyNoteList))
        archivedStickyNoteList.addOnActionListener {
            presenter.onItemOpened(it)
        }
        archivedStickyNoteList.model = StickyNoteListModel(emptyList())
        archivedStickyNoteList.cellRenderer = StickyNoteListCellRenderer()
        setupDragAndDrop(archivedStickyNoteList)

        backButton.addActionListener {
            presenter.onBackButtonClick()
        }
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

    private fun setupDragAndDrop(stickNoteList: JList<StickyNoteViewModel>) {
        stickNoteList.dragEnabled = true
        stickNoteList.transferHandler = StickyNoteTransferHandler()
        stickNoteList.dropMode = DropMode.INSERT
    }

    private fun createBacklogStickyNoteActions(stickNoteList: JList<StickyNoteViewModel>): ActionGroup {
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

    override fun onCreate(toolWindowComponent: StickyNoteToolWindowComponent) {
        toolWindowComponent.inject(this)
        super.onCreate(toolWindowComponent)
    }

    override fun render(viewModel: StickyNoteListViewModel) {
        backlogStickyNoteList.model = StickyNoteListModel(viewModel.backlogStickyNotes)
        archivedStickyNoteList.model = StickyNoteListModel(viewModel.archiveStickyNotes)
    }

    override fun getContent(): JComponent {
        return contentPanel
    }
}

