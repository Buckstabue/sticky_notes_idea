package com.buckstabue.stickynote.base

import com.intellij.ui.DoubleClickListener
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.KeyStroke

fun <ITEM_TYPE : Any> JList<ITEM_TYPE>.addOnActionListener(listener: (ITEM_TYPE) -> Unit) {
    object : DoubleClickListener() {
        override fun onDoubleClick(event: MouseEvent?): Boolean {
            listener.invoke(selectedValue)
            return true
        }
    }.installOn(this)

    this.registerKeyboardAction(
        { listener.invoke(selectedValue) },
        KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
        JComponent.WHEN_FOCUSED
    );
}
