package com.buckstabue.stickynote

import com.buckstabue.stickynote.activenote.ActiveNoteWindow
import com.buckstabue.stickynote.base.BaseWindow
import com.buckstabue.stickynote.stickynotelist.StickyNoteListWindow
import java.awt.CardLayout
import javax.swing.JPanel

class StickyNoteRouterImpl : StickyNoteRouter {
    private val cardLayout = CardLayout()
    private val contentPanel = JPanel(cardLayout)

    private val activeNoteWindow = ActiveNoteWindow()
    private val stickyNoteListWindow = StickyNoteListWindow()

    private var currentActiveWindow: BaseWindow<*, *>? = null

    fun onCreate() {
        addScreen(activeNoteWindow)
        addScreen(stickyNoteListWindow)
    }

    private fun addScreen(screenWindow: BaseWindow<*, *>) {
        contentPanel.add(screenWindow.getContent(), screenWindow.routingTag)
        screenWindow.onCreate()
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
