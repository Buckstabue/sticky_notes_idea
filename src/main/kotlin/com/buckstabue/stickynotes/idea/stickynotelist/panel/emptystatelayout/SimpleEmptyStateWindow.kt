package com.buckstabue.stickynotes.idea.stickynotelist.panel.emptystatelayout

import javax.swing.JLabel
import javax.swing.JPanel

class SimpleEmptyStateWindow(
    centeredText: String
) {
    lateinit var content: JPanel
    private lateinit var label: JLabel

    init {
        label.text = centeredText
    }
}
