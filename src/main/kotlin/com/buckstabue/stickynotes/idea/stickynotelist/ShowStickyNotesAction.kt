package com.buckstabue.stickynotes.idea.stickynotelist

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon


class ShowStickyNotesAction @JvmOverloads constructor(
    text: String = "Show Sticky Notes",
    icon: Icon? = IconLoader.getIcon("/list.svg")
) : AnAction(text, null, icon), DumbAware {
    companion object {
        private val logger = Logger.getInstance(ShowStickyNotesAction::class.java)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        if (project == null) {
            logger.error("project is null")
            return
        }

        StickyNoteListDialog(project).show()
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isVisible = e.project != null
    }
}
