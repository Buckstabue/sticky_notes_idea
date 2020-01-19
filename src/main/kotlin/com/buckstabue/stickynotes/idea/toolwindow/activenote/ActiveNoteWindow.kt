package com.buckstabue.stickynotes.idea.toolwindow.activenote

import com.buckstabue.stickynotes.idea.BaseWindow
import com.buckstabue.stickynotes.idea.HorizontalBorder
import com.buckstabue.stickynotes.idea.createeditstickynote.CreateStickyNoteAction
import com.buckstabue.stickynotes.idea.disableIdeaLookAndFeel
import com.buckstabue.stickynotes.idea.setWrappedText
import com.buckstabue.stickynotes.idea.stickynotelist.ShowStickyNotesAction
import com.buckstabue.stickynotes.idea.stickynotelist.StickyNoteListAnalytics.Source
import com.buckstabue.stickynotes.idea.toolwindow.di.StickyNoteToolWindowComponent
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.intellij.util.IconUtil
import javax.inject.Inject
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class ActiveNoteWindow(
    toolWindowComponent: StickyNoteToolWindowComponent
) : BaseWindow<ActiveNoteView, ActiveNotePresenter>(), ActiveNoteView {
    private lateinit var contentPanel: JPanel
    private lateinit var toolbarPanel: JPanel
    private lateinit var activeNote: JLabel
    private lateinit var openActiveStickyNoteButton: JButton
    private lateinit var doneButton: JButton

    override val routingTag: String = "ActiveStickyNote"

    @Inject
    override lateinit var presenter: ActiveNotePresenter

    @Inject
    lateinit var project: Project

    init {
        toolWindowComponent.inject(this)
        val actionToolbar = createActionToolbar();
        toolbarPanel.add(actionToolbar.component)

        activeNote.border = HorizontalBorder(left = 16, right = 16)

        doneButton.disableIdeaLookAndFeel()
        doneButton.icon = IconLoader.getIcon("/done.svg")
        doneButton.pressedIcon = IconLoader.getIcon("/done_pressed.svg")

        doneButton.addActionListener {
            presenter.onDoneClick()
        }

        openActiveStickyNoteButton.disableIdeaLookAndFeel()
        openActiveStickyNoteButton.border = HorizontalBorder(left = 0, right = 24)
        openActiveStickyNoteButton.icon = IconLoader.getIcon("/pin.svg")
        openActiveStickyNoteButton.pressedIcon = IconLoader.getIcon("/pin_pressed.svg")
        openActiveStickyNoteButton.addActionListener {
            presenter.onOpenActiveStickyNoteButtonClick()
        }
    }

    private fun createActionToolbar(): ActionToolbar {
        val actionGroup = DefaultActionGroup(
            ShowStickyNotesAction(source = Source.ACTIVE_STICKY_NOTE),
            CreateStickyNoteAction(
                codeBindingEnabledByDefaultWhenPossible = false,
                text = "Create a new Sticky Note",
                icon = IconUtil.getAddIcon()
            )
        )
        return ActionManager.getInstance().createActionToolbar("TOP", actionGroup, true)
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
