package com.buckstabue.stickynote.toolwindow.stickynotelist

import com.buckstabue.stickynote.base.BaseWindow
import com.buckstabue.stickynote.base.addOnActionListener
import com.buckstabue.stickynote.base.addOnPopupActionListener
import com.buckstabue.stickynote.toolwindow.StickyNoteToolWindowComponent
import com.buckstabue.stickynote.toolwindow.stickynotelist.contextmenu.ArchiveStickyNoteAction
import com.buckstabue.stickynote.toolwindow.stickynotelist.contextmenu.MoveStickyNoteToBacklogAction
import com.buckstabue.stickynote.toolwindow.stickynotelist.contextmenu.RemoveStickyNoteAction
import com.buckstabue.stickynote.toolwindow.stickynotelist.contextmenu.SetStickyNoteActiveAction
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.Separator
import java.awt.Component
import javax.inject.Inject
import javax.swing.AbstractListModel
import javax.swing.DefaultListCellRenderer
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JTabbedPane

class StickyNoteListWindow : BaseWindow<StickyNoteListView, StickyNoteListPresenter>(), StickyNoteListView {
    private lateinit var tabbedPane: JTabbedPane
    private lateinit var backlogStickyNoteList: JList<StickyNoteViewModel>
    private lateinit var archivedStickyNoteList: JList<StickyNoteViewModel>
    private lateinit var contentPanel: JPanel
    private lateinit var backButton: JButton

    override val routingTag: String = "StickyNoteList"

    @Inject
    override lateinit var presenter: StickyNoteListPresenter

    private val stickyNoteActions = createStickyNoteActions()

    init {
        backlogStickyNoteList.model = StickyNoteListModel(emptyList())

        backlogStickyNoteList.addOnPopupActionListener(stickyNoteActions)
        backlogStickyNoteList.addOnActionListener {
            presenter.onItemOpened(it)
        }

        archivedStickyNoteList.model = StickyNoteListModel(emptyList())
        archivedStickyNoteList.addOnActionListener {
            presenter.onItemOpened(it)
        }
        archivedStickyNoteList.cellRenderer = StickyNoteListCellRenderer()
        backlogStickyNoteList.cellRenderer = StickyNoteListCellRenderer()

        backButton.addActionListener {
            presenter.onBackButtonClick()
        }
    }

    private fun createStickyNoteActions(): ActionGroup {
        return DefaultActionGroup(
            SetStickyNoteActiveAction(backlogStickyNoteList),
            Separator.getInstance(),
            ArchiveStickyNoteAction(backlogStickyNoteList),
            MoveStickyNoteToBacklogAction(backlogStickyNoteList),
            Separator.getInstance(),
            RemoveStickyNoteAction(backlogStickyNoteList)
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

class StickyNoteListModel(
    private val items: List<StickyNoteViewModel>
) : AbstractListModel<StickyNoteViewModel>() {
    override fun getElementAt(index: Int): StickyNoteViewModel {
        return items[index]
    }

    override fun getSize(): Int {
        return items.size
    }
}

class StickyNoteListCellRenderer : DefaultListCellRenderer() {
    override fun getListCellRendererComponent(
        list: JList<*>?,
        value: Any?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        val stickyNoteViewModel = value as StickyNoteViewModel
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
        text = stickyNoteViewModel.description
        icon = stickyNoteViewModel.icon
        return this
    }
}
