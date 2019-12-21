package com.buckstabue.stickynotes.toolwindow

import com.buckstabue.stickynotes.base.BaseWindow
import com.buckstabue.stickynotes.toolwindow.activenote.ActiveNoteWindow
import com.buckstabue.stickynotes.toolwindow.stickynotelist.StickyNoteListWindow
import java.awt.CardLayout
import javax.swing.JPanel

class StickyNoteRouterImpl : StickyNoteRouter {
    private val cardLayout = CardLayout()
    private val contentPanel = JPanel(cardLayout)

    private val activeNoteWindow = ActiveNoteWindow()
    private val stickyNoteListWindow = StickyNoteListWindow()

    private var currentActiveWindow: BaseWindow<*, *>? = null

    private lateinit var toolWindowComponent: StickyNoteToolWindowComponent

    fun onCreate(toolWindowComponent: StickyNoteToolWindowComponent) {
        this.toolWindowComponent = toolWindowComponent
        addScreen(activeNoteWindow)
        addScreen(stickyNoteListWindow)
    }

    private fun addScreen(screenWindow: BaseWindow<*, *>) {
        contentPanel.add(screenWindow.getContent(), screenWindow.routingTag)
        screenWindow.onCreate(toolWindowComponent)
    }

    fun getRouterRootPanel(): JPanel {
        return contentPanel
    }

    override fun openStickyNotesList() {
        showScreen(stickyNoteListWindow)
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
