package com.buckstabue.stickynotes.idea.gutter

import com.buckstabue.stickynotes.FileBoundStickyNote
import com.buckstabue.stickynotes.idea.gutter.contextmenu.EditStickyNoteFromGutterAction
import com.buckstabue.stickynotes.idea.gutter.contextmenu.RemoveStickyNoteFromGutterAction
import com.buckstabue.stickynotes.idea.gutter.contextmenu.SetStickyNoteActiveFromGutterAction
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.Separator
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

class StickyNoteGutterIconRenderer(
    private val stickyNote: FileBoundStickyNote
) : GutterIconRenderer() {
    companion object {
        private val GUTTER_ICON by lazy {
            IconLoader.getIcon(
                "/note_gutter.svg"
            )
        }
    }

    override fun getIcon(): Icon {
        return GUTTER_ICON
    }

    override fun getTooltipText(): String? {
        return stickyNote.description
    }

    override fun hashCode(): Int {
        return stickyNote.hashCode()
    }

    override fun getPopupMenuActions(): ActionGroup? {
        return DefaultActionGroup(
            EditStickyNoteFromGutterAction(stickyNote),
            SetStickyNoteActiveFromGutterAction(stickyNote),
            Separator.getInstance(),
            RemoveStickyNoteFromGutterAction(stickyNote)
        )
    }

    override fun equals(other: Any?): Boolean {
        return other is StickyNoteGutterIconRenderer && other.stickyNote == this.stickyNote
    }
}
