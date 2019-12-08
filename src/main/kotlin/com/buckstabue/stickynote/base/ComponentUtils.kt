package com.buckstabue.stickynote.base

import com.intellij.util.EditSourceOnDoubleClickHandler
import com.intellij.util.EditSourceOnEnterKeyHandler
import javax.swing.JList

fun <ITEM_TYPE: Any> JList<ITEM_TYPE>.addOnActionListener(listener: (ITEM_TYPE) -> Unit) {
    val callback = Runnable {
        listener.invoke(selectedValue)
    }

    EditSourceOnDoubleClickHandler.install(this, callback)
    EditSourceOnEnterKeyHandler.install(this, callback)
}
