package com.buckstabue.stickynotes.idea.toolwindow

import com.intellij.openapi.wm.ToolWindow

val ToolWindow.isRemoved: Boolean
    get() = !this.isShowStripeButton

val ToolWindow.isExpanded: Boolean
    get() = this.isVisible && !isRemoved
