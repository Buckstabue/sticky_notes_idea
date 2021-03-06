package com.buckstabue.stickynotes.idea.toolwindow.activenote

import com.buckstabue.stickynotes.base.di.AppInjector
import com.buckstabue.stickynotes.idea.BaseWindow
import com.buckstabue.stickynotes.idea.HorizontalBorder
import com.buckstabue.stickynotes.idea.createeditstickynote.CreateEditStickyNoteAnalytics
import com.buckstabue.stickynotes.idea.createeditstickynote.CreateEditStickyNoteViewModel
import com.buckstabue.stickynotes.idea.createeditstickynote.CreateStickyNoteAction
import com.buckstabue.stickynotes.idea.customcomponent.JHyperlink
import com.buckstabue.stickynotes.idea.disableIdeaLookAndFeel
import com.buckstabue.stickynotes.idea.setWrappedText
import com.buckstabue.stickynotes.idea.stickynotelist.ShowStickyNotesAction
import com.buckstabue.stickynotes.idea.stickynotelist.StickyNoteListAnalytics.Source
import com.buckstabue.stickynotes.idea.toolwindow.di.StickyNoteToolWindowComponent
import com.buckstabue.stickynotes.idea.util.IdeaUtils
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.intellij.util.IconUtil
import java.awt.Color
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
    private lateinit var nonEmptyPanel: JPanel
    private lateinit var activeNote: JLabel
    private lateinit var openActiveStickyNoteButton: JButton
    private lateinit var emptyStatePanel: JPanel
    private lateinit var addNewStickyNoteLink: JHyperlink
    private lateinit var doneButton: JButton

    override val routingTag: String = "ActiveStickyNote"

    @Inject
    override lateinit var presenter: ActiveNotePresenter

    @Inject
    protected lateinit var analytics: ActiveNoteAnalytics

    @Inject
    lateinit var project: Project

    init {
        toolWindowComponent.inject(this)
        val actionToolbar = createActionToolbar()
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

        addNewStickyNoteLink.linkTextColor = Color.BLUE.darker()
        addNewStickyNoteLink.hoverLinkTextColor = Color.decode("#0700FC")
        addNewStickyNoteLink.setOnLinkClickListener {
            analytics.createStickyNoteLinkClick()
            showCreateStickyNoteDialog()
        }
    }

    override fun showHintUnderCursor(text: String) {
        IdeaUtils.showHintUnderCursor(contentPanel, text)
    }

    private fun showCreateStickyNoteDialog() {
        val createEditStickyNoteComponent = AppInjector.getProjectComponent(project)
            .plusCreateEditStickyNoteComponent()
            .create(
                mode = CreateEditStickyNoteViewModel.Mode.EDIT,
                source = CreateEditStickyNoteAnalytics.Source.ACTIVE_STICKY_NOTE_LINK
            )
        val createStickyNoteScenario =
            createEditStickyNoteComponent.createStickyNoteScenario()
        createStickyNoteScenario.launch(
            editor = IdeaUtils.getCurrentEditor(),
            codeBindingEnabledByDefaultWhenPossible = false
        )
    }

    private fun createActionToolbar(): ActionToolbar {
        val actionGroup = DefaultActionGroup(
            ShowStickyNotesAction(source = Source.ACTIVE_STICKY_NOTE),
            CreateStickyNoteAction(
                codeBindingEnabledByDefaultWhenPossible = false,
                source = CreateEditStickyNoteAnalytics.Source.ACTIVE_STICKY_NOTE_TOOLBAR,
                text = "Create a new Sticky Note",
                icon = IconUtil.getAddIcon()
            )
        )
        return ActionManager.getInstance().createActionToolbar("TOP", actionGroup, true)
    }

    override fun render(viewModel: ActiveStickyNoteViewModel) {
        emptyStatePanel.isVisible = viewModel.showEmptyState
        nonEmptyPanel.isVisible = !viewModel.showEmptyState

        if (viewModel.showEmptyState) {
            doneButton.isVisible = false
            openActiveStickyNoteButton.isVisible = false
        } else {
            doneButton.isVisible = true
            activeNote.setWrappedText(viewModel.activeNoteDescription)
            openActiveStickyNoteButton.isVisible = viewModel.showOpenActiveStickyNoteButton
        }
    }


    override fun getContent(): JComponent {
        return contentPanel
    }
}
