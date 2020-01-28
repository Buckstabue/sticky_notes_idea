package com.buckstabue.stickynotes.idea.stickynotelist.panel

import com.buckstabue.stickynotes.idea.addOnActionListener
import com.buckstabue.stickynotes.idea.setContextMenu
import com.buckstabue.stickynotes.idea.stickynotelist.StickyNoteListAnalytics
import com.buckstabue.stickynotes.idea.stickynotelist.StickyNoteListDialog
import com.buckstabue.stickynotes.idea.stickynotelist.contextmenu.ArchiveStickyNoteAction
import com.buckstabue.stickynotes.idea.stickynotelist.contextmenu.EditStickyNoteFromListAction
import com.buckstabue.stickynotes.idea.stickynotelist.contextmenu.MoveStickyNoteToBacklogAction
import com.buckstabue.stickynotes.idea.stickynotelist.contextmenu.RemoveStickyNoteAction
import com.buckstabue.stickynotes.idea.stickynotelist.contextmenu.SetStickyNoteActiveAction
import com.buckstabue.stickynotes.idea.stickynotelist.di.StickyNoteListDialogComponent
import com.buckstabue.stickynotes.idea.stickynotelist.panel.emptystatelayout.StickyNoteListEmptyPanelFactory
import com.buckstabue.stickynotes.idea.util.IdeaUtils
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.Separator
import com.intellij.openapi.ui.DialogWrapper
import java.awt.event.KeyEvent
import javax.inject.Inject
import javax.swing.DropMode
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.KeyStroke

class StickyNotesPanel(
    private val parentDialog: StickyNoteListDialog,
    stickyNoteListDialogComponent: StickyNoteListDialogComponent,
    stickyNotesObservableType: StickyNotesObservable.Type
) : StickyNotesPanelView, Disposable {
    private lateinit var stickyNoteList: JList<StickyNoteViewModel>
    private lateinit var contentPanel: JPanel
    private lateinit var nonEmptyContentLayout: JComponent
    private lateinit var emptyContentLayout: JPanel

    @Inject
    protected lateinit var presenter: StickyNoteListPanelPresenter

    @Inject
    protected lateinit var analytics: StickyNoteListAnalytics

    @Inject
    protected lateinit var stickyNoteListEmptyPanelFactory: StickyNoteListEmptyPanelFactory

    init {
        stickyNoteListDialogComponent.plusStickyNotesPanelComponent()
            .create(stickyNotesObservableType)
            .inject(this)
        stickyNoteListEmptyPanelFactory.createEmptyPanel()?.let {
            emptyContentLayout.add(it)
        }
        emptyContentLayout.isVisible = false
        nonEmptyContentLayout.isVisible = false

        stickyNoteList.addOnActionListener {
            presenter.onItemOpened(it)
        }
        stickyNoteList.registerKeyboardAction(
            {
                presenter.onDeleteKeyPressed(stickyNoteList.selectedValuesList)
            },
            KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
            JComponent.WHEN_FOCUSED
        )
        stickyNoteList.model =
            StickyNoteListModel(
                emptyList()
            )
        stickyNoteList.cellRenderer =
            StickyNoteListCellRenderer()

        setupContextMenu()
        setupDragAndDrop()

        presenter.attachView(this)
    }

    private fun setupContextMenu() {
        stickyNoteList.setContextMenu(
            DefaultActionGroup(
                SetStickyNoteActiveAction(stickyNoteList, analytics),
                EditStickyNoteFromListAction(stickyNoteList, analytics),
                Separator.getInstance(),
                ArchiveStickyNoteAction(stickyNoteList, analytics),
                MoveStickyNoteToBacklogAction(stickyNoteList, analytics),
                Separator.getInstance(),
                RemoveStickyNoteAction(stickyNoteList, analytics)
            )
        )
    }

    private fun setupDragAndDrop() {
        stickyNoteList.dragEnabled = true
        stickyNoteList.transferHandler = StickyNoteTransferHandler(analytics)
        stickyNoteList.dropMode = DropMode.INSERT
    }

    override fun render(viewModels: List<StickyNoteViewModel>) {
        stickyNoteList.model =
            StickyNoteListModel(
                viewModels
            )
        emptyContentLayout.isVisible = viewModels.isEmpty()
        nonEmptyContentLayout.isVisible = viewModels.isNotEmpty()
    }

    override fun showHintUnderCursor(hintText: String) {
        IdeaUtils.showHintUnderCursor(contentPanel, hintText)
    }

    override fun close() {
        parentDialog.close(DialogWrapper.OK_EXIT_CODE)
    }

    override fun dispose() {
        presenter.detachView()
    }

    fun getContentPanel(): JComponent {
        return contentPanel
    }
}
