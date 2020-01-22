package com.buckstabue.stickynotes.idea.customcomponent

import com.buckstabue.stickynotes.idea.setWrappedText
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

    private var onLinkClickListener: (() -> Unit)? = null

    init {
        setWrappedText(linkText)
        foreground = Color.BLUE.darker()
        cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        setClickListener()
    }

    private fun setClickListener() {
        addMouseListener(object : MouseAdapter() {
            override fun mouseEntered(e: MouseEvent?) {
                setLinkLookingTextStyle()
            }

            override fun mouseExited(e: MouseEvent?) {
                setWrappedText(linkText)
            }

            override fun mouseClicked(e: MouseEvent?) {
                onLinkClickListener?.invoke()
            }
        })
    }


    private fun setLinkLookingTextStyle() {
        text = "<html><a href=''>$linkText</a></html>"
    }
}
