package com.buckstabue.stickynote.stickynotelist

import com.buckstabue.stickynote.base.BaseWindow
import javax.inject.Inject
import javax.swing.AbstractListModel
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.JPanel

class StickyNoteListWindow : BaseWindow<StickyNoteListView, StickyNoteListPresenter>(), StickyNoteListView {
    private lateinit var stickyNoteList: JList<StickyNoteViewModel>
    private lateinit var contentPanel: JPanel

    @Inject
    override lateinit var presenter: StickyNoteListPresenter

    init {
        stickyNoteList.model = StickyNoteListModel(emptyList())
    }

    override fun render(stickyNotes: List<StickyNoteViewModel>) {
        stickyNoteList.model = StickyNoteListModel(stickyNotes)
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
