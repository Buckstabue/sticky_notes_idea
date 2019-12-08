package com.buckstabue.stickynote.stickynotelist

import com.buckstabue.stickynote.AppComponent
import com.buckstabue.stickynote.base.BaseWindow
import javax.inject.Inject
import javax.swing.*

class StickyNoteListWindow : BaseWindow<StickyNoteListView, StickyNoteListPresenter>(), StickyNoteListView {
    private lateinit var stickyNoteList: JList<StickyNoteViewModel>
    private lateinit var contentPanel: JPanel
    private lateinit var backButton: JButton

    override val routingTag: String = "StickyNoteList"

    @Inject
    override lateinit var presenter: StickyNoteListPresenter

    init {
        stickyNoteList.model = StickyNoteListModel(emptyList())
        backButton.addActionListener {
            presenter.onBackButtonClick()
        }
    }

    override fun onCreate() {
        AppComponent.INSTANCE.inject(this)
        super.onCreate()
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
