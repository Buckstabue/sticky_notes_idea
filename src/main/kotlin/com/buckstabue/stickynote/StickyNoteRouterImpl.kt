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

    fun onCreate() {
        addScreen(activeNoteWindow)
        addScreen(stickyNoteListWindow)
    }

    private fun addScreen(baseWindow: BaseWindow<*, *>) {
        contentPanel.add(baseWindow.getContent(), baseWindow.routingTag)
        baseWindow.onCreate()
    }

    fun getRouterRootPanel(): JPanel {
        return contentPanel
    }

    override fun openStickyNotesList() {
        showScreen(stickyNoteListWindow)
    }

    private fun showScreen(baseWindow: BaseWindow<*, *>) {
        cardLayout.show(contentPanel, baseWindow.routingTag)
    }

    override fun openActiveStickyNote() {
        showScreen(activeNoteWindow)
    }
}
