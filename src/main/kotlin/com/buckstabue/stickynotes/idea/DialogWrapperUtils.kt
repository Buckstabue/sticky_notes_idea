package com.buckstabue.stickynotes.idea

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.DialogWrapperPeer
import javax.swing.JComponent

fun DialogWrapper.disableDefaultSelection(component: JComponent) {
    component.putClientProperty(DialogWrapperPeer.HAVE_INITIAL_SELECTION, true);
}
