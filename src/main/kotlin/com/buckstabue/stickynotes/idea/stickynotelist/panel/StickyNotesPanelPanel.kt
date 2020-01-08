package com.buckstabue.stickynotes.idea.stickynotelist.panel

import com.buckstabue.stickynotes.idea.addOnActionListener
import com.buckstabue.stickynotes.idea.setContextMenu
import com.buckstabue.stickynotes.idea.stickynotelist.StickyNoteListDialog
import com.buckstabue.stickynotes.idea.stickynotelist.contextmenu.ArchiveStickyNoteAction
import com.buckstabue.stickynotes.idea.stickynotelist.contextmenu.EditStickyNoteAction
import com.buckstabue.stickynotes.idea.stickynotelist.contextmenu.MoveStickyNoteToBacklogAction
import com.buckstabue.stickynotes.idea.stickynotelist.contextmenu.RemoveStickyNoteAction
import com.buckstabue.stickynotes.idea.stickynotelist.contextmenu.SetStickyNoteActiveAction
import com.buckstabue.stickynotes.idea.stickynotelist.di.StickyNoteListDialogComponent
import com.buckstabue.stickynotes.idea.stickynotelist.getstickynotesstrategy.StickyNotesObservable
import com.intellij.codeInsight.hint.HintManager
import com.intellij.codeInsight.hint.HintUtil
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.Separator
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.awt.RelativePoint
import javax.inject.Inject
import javax.swing.DropMode
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.JPanel

class StickyNotesPanelPanel(
    private val parentDialog: StickyNoteListDialog,
    stickyNoteListDialogComponent: StickyNoteListDialogComponent,
    stickyNotesObservableType: StickyNotesObservable.Type
) : StickyNotesPanelView, Disposable {
    private lateinit var stickyNoteList: JList<StickyNoteViewModel>
    private lateinit var contentPanel: JPanel

    @Inject
    lateinit var presenter: StickyNoteListPanelPresenter

    init {
        stickyNoteListDialogComponent.plusStickyNotesPanelComponent()
            .create(stickyNotesObservableType)
            .inject(this)

        stickyNoteList.addOnActionListener {
            presenter.onItemOpened(it)
        }
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
                SetStickyNoteActiveAction(stickyNoteList),
                EditStickyNoteAction(stickyNoteList),
                Separator.getInstance(),
                ArchiveStickyNoteAction(stickyNoteList),
                MoveStickyNoteToBacklogAction(stickyNoteList),
                Separator.getInstance(),
                RemoveStickyNoteAction(stickyNoteList)
            )
        )
    }

    private fun setupDragAndDrop() {
        stickyNoteList.dragEnabled = true
        stickyNoteList.transferHandler =
            StickyNoteTransferHandler()
        stickyNoteList.dropMode = DropMode.INSERT
    }

    override fun render(viewModels: List<StickyNoteViewModel>) {
        stickyNoteList.model =
            StickyNoteListModel(
                viewModels
            )
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
        parentDialog.close(DialogWrapper.OK_EXIT_CODE)
    }

    override fun dispose() {
        presenter.detachView()
    }

    fun getContentPanel(): JComponent {
        return contentPanel
    }
}
