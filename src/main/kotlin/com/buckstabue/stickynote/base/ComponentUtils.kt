package com.buckstabue.stickynote.base

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.ui.DoubleClickListener
import com.intellij.ui.PopupHandler
import org.apache.commons.lang.StringEscapeUtils
import java.awt.Component
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.KeyStroke

fun <ITEM_TYPE : Any> JList<ITEM_TYPE>.addOnActionListener(listener: (ITEM_TYPE) -> Unit) {
    object : DoubleClickListener() {
        override fun onDoubleClick(event: MouseEvent?): Boolean {
            selectedValue?.also(listener)
            listener.invoke(selectedValue)
            return true
        }
    }.installOn(this)

    this.registerKeyboardAction(
        { listener.invoke(selectedValue) },
        KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
        JComponent.WHEN_FOCUSED
    )
}

fun <ITEM_TYPE : Any> JList<ITEM_TYPE>.addOnPopupActionListener(actionGroup: ActionGroup) {
    val jList = this
    this.addMouseListener(object : MouseAdapter() {
        override fun mousePressed(e: MouseEvent) {
            if (e.isPopupTrigger) {
                val elementIndex = jList.locationToIndex(e.point)
                if (elementIndex != -1 && !jList.isSelectedIndex(elementIndex)) {
                    jList.selectedIndex = elementIndex
                }
            }
        }
    })
    this.addMouseListener(object : PopupHandler() {
        override fun invokePopup(comp: Component?, x: Int, y: Int) {
            val popupMenu = ActionManager.getInstance()
                .createActionPopupMenu(ActionPlaces.UNKNOWN, actionGroup)
            popupMenu.component.show(jList, x, y)
        }
    })
}

fun JLabel.setWrappedText(text: String) {
    this.text = "<HTML>${StringEscapeUtils.escapeHtml(text)}</HTML>"
}
