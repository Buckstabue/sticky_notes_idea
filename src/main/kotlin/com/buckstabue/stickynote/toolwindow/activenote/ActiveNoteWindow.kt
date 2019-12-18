package com.buckstabue.stickynote.toolwindow.activenote

import com.buckstabue.stickynote.base.BaseWindow
import com.buckstabue.stickynote.base.setWrappedText
import com.buckstabue.stickynote.toolwindow.StickyNoteToolWindowComponent
import javax.inject.Inject
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class ActiveNoteWindow : BaseWindow<ActiveNoteView, ActiveNotePresenter>(), ActiveNoteView {
    private lateinit var contentPanel: JPanel
    private lateinit var activeNote: JLabel
    private lateinit var gotoStickyNoteListButton: JButton
    private lateinit var doneButton: JButton

    override val routingTag: String = "ActiveStickyNote"

    @Inject
    override lateinit var presenter: ActiveNotePresenter

    init {
        activeNote.border = EmptyBorder(0, 16, 0, 16)

        gotoStickyNoteListButton.addActionListener {
            presenter.onGotoStickyNoteListClick()
        }

        doneButton.addActionListener {
            presenter.onDoneClick()
        }
    }

    override fun onCreate(toolWindowComponent: StickyNoteToolWindowComponent) {
        toolWindowComponent.inject(this)
        super.onCreate(toolWindowComponent)
    }

    override fun render(viewModel: ActiveStickyNoteViewModel) {
        activeNote.setWrappedText(viewModel.activeNoteDescription)
        doneButton.isVisible = viewModel.showDoneButton
    }


    override fun getContent(): JComponent {
        return contentPanel
    }
}
