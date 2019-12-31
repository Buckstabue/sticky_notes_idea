package com.buckstabue.stickynotes.idea.toolwindow.activenote

import com.buckstabue.stickynotes.idea.BaseWindow
import com.buckstabue.stickynotes.idea.disableIdeaLookAndFeel
import com.buckstabue.stickynotes.idea.setWrappedText
import com.buckstabue.stickynotes.idea.toolwindow.StickyNoteToolWindowComponent
import com.intellij.openapi.util.IconLoader
import javax.inject.Inject
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class ActiveNoteWindow : BaseWindow<ActiveNoteView, ActiveNotePresenter>(), ActiveNoteView {
    private lateinit var contentPanel: JPanel
    private lateinit var activeNote: JLabel
    private lateinit var openActiveStickyNoteButton: JButton
    private lateinit var gotoStickyNoteListButton: JButton
    private lateinit var doneButton: JButton

    override val routingTag: String = "ActiveStickyNote"

    @Inject
    override lateinit var presenter: ActiveNotePresenter

    init {
        activeNote.border = EmptyBorder(0, 16, 0, 16)
        doneButton.disableIdeaLookAndFeel()
        doneButton.icon = IconLoader.getIcon("/done.svg")
        doneButton.pressedIcon = IconLoader.getIcon("/done_pressed.svg")

        gotoStickyNoteListButton.addActionListener {
            presenter.onGotoStickyNoteListClick()
        }

        doneButton.addActionListener {
            presenter.onDoneClick()
        }

        openActiveStickyNoteButton.disableIdeaLookAndFeel()
        openActiveStickyNoteButton.icon = IconLoader.getIcon("/pin.svg")
        openActiveStickyNoteButton.pressedIcon = IconLoader.getIcon("/pin_pressed.svg")
        openActiveStickyNoteButton.addActionListener {
            presenter.onOpenActiveStickyNoteButtonClick()
        }
    }

    override fun onCreate(toolWindowComponent: StickyNoteToolWindowComponent) {
        toolWindowComponent.inject(this)
        super.onCreate(toolWindowComponent)
    }

    override fun render(viewModel: ActiveStickyNoteViewModel) {
        activeNote.setWrappedText(viewModel.activeNoteDescription)
        doneButton.isVisible = viewModel.showDoneButton
        openActiveStickyNoteButton.isVisible = viewModel.showOpenActiveStickyNoteButton
    }


    override fun getContent(): JComponent {
        return contentPanel
    }
}
