package com.buckstabue.stickynotes.idea.customcomponent

import com.buckstabue.stickynotes.idea.setWrappedText
import com.intellij.util.ui.JBUI
import java.awt.Color
import java.awt.Cursor
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JLabel


class JHyperlink @JvmOverloads constructor(
    linkText: String = ""
) : JLabel() {

    var linkText: String = linkText
        set(value) {
            field = value
            setWrappedText(field)
        }

    var linkTextColor: Color = JBUI.CurrentTheme.Link.linkColor()
        set(value) {
            field = value
            foreground = field
        }

    var hoverLinkTextColor: Color = JBUI.CurrentTheme.Link.linkHoverColor()

    private var onLinkClickListener: (() -> Unit)? = null

    init {
        foreground = linkTextColor
        if (linkTextColor == hoverLinkTextColor) {
            hoverLinkTextColor = JBUI.CurrentTheme.Link.linkHoverColor().brighter()
        }
        setWrappedText(linkText)
        cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        setClickListener()
    }


    private fun setClickListener() {
        addMouseListener(object : MouseAdapter() {
            override fun mouseEntered(e: MouseEvent?) {
                foreground = hoverLinkTextColor
            }

            override fun mouseExited(e: MouseEvent?) {
                foreground = linkTextColor
            }

            override fun mouseClicked(e: MouseEvent?) {
                onLinkClickListener?.invoke()
            }
        })
    }

    fun setOnLinkClickListener(linkClickListener: (() -> Unit)?) {
        this.onLinkClickListener = linkClickListener
    }

}
