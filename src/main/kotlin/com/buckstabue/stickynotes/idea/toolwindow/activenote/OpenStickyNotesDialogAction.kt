package com.buckstabue.stickynotes.idea.toolwindow.activenote

import com.buckstabue.stickynotes.idea.stickynotelist.StickyNoteListDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader

class OpenStickyNotesDialogAction(
    private val project: Project
) : AnAction("Show All", null, IconLoader.getIcon("/list.svg")), DumbAware {
    override fun actionPerformed(e: AnActionEvent) {
        StickyNoteListDialog(project).show()
    }
}
