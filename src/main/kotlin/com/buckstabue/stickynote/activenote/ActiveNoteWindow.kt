package com.buckstabue.stickynote.activenote

import com.buckstabue.stickynote.AppComponent
import com.buckstabue.stickynote.base.BaseWindow
import javax.inject.Inject
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class ActiveNoteWindow : BaseWindow<ActiveNoteView, ActiveNotePresenter>(), ActiveNoteView {
    private lateinit var contentPanel: JPanel
    private lateinit var activeNote: JLabel
    private lateinit var gotoStickyNoteListButton: JButton

    override val routingTag: String = "ActiveStickyNote"

    @Inject
    override lateinit var presenter: ActiveNotePresenter

    override fun render(viewModel: ActiveStickyNoteViewModel) {
        activeNote.text = viewModel.activeNoteDescription
    }

    init {
        gotoStickyNoteListButton.addActionListener {
            presenter.onGotoStickyNoteListClick()
        }
    }

    override fun onCreate() {
        AppComponent.INSTANCE.plusActiveNoteComponent().inject(this)
        super.onCreate()
    }

    override fun getContent(): JComponent {
        return contentPanel
    }
}
