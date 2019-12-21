package com.buckstabue.stickynote.toolwindow.stickynotelist

import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JList

class StickyNoteListCellRenderer : DefaultListCellRenderer() {
    override fun getListCellRendererComponent(
        list: JList<*>?,
        value: Any?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        val stickyNoteViewModel = value as StickyNoteViewModel
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
        text = stickyNoteViewModel.description
        icon = stickyNoteViewModel.icon
        return this
    }
}
