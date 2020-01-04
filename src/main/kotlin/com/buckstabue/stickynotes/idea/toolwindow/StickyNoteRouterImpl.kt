package com.buckstabue.stickynotes.idea.toolwindow

import com.buckstabue.stickynotes.idea.BaseWindow
import com.buckstabue.stickynotes.idea.stickynotelist.StickyNoteListWindow
import com.buckstabue.stickynotes.idea.toolwindow.activenote.ActiveNoteWindow
import com.intellij.openapi.project.Project
import java.awt.CardLayout
import javax.swing.JPanel

class StickyNoteRouterImpl(
    private val project: Project
) : StickyNoteRouter {
    private val cardLayout = CardLayout()
    private val contentPanel = JPanel(cardLayout)

    private val activeNoteWindow = ActiveNoteWindow()

    private var currentActiveWindow: BaseWindow<*, *>? = null

    private lateinit var toolWindowComponent: StickyNoteToolWindowComponent

    fun onCreate(toolWindowComponent: StickyNoteToolWindowComponent) {
        this.toolWindowComponent = toolWindowComponent
        addScreen(activeNoteWindow)
    }

    private fun addScreen(screenWindow: BaseWindow<*, *>) {
        contentPanel.add(screenWindow.getContent(), screenWindow.routingTag)
        screenWindow.onCreate(toolWindowComponent)
    }

    fun getRouterRootPanel(): JPanel {
        return contentPanel
    }

    override fun openStickyNotesList() {
        val stickyNoteListWindow = StickyNoteListWindow(project)
        stickyNoteListWindow.show()
    }

    private fun showScreen(screenWindow: BaseWindow<*, *>) {
        currentActiveWindow?.onDetach()
        cardLayout.show(contentPanel, screenWindow.routingTag)
        screenWindow.onAttach()
        currentActiveWindow = screenWindow
    }

    override fun openActiveStickyNote() {
        showScreen(activeNoteWindow)
    }
}
