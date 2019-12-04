package com.buckstabue.stickynote

import com.buckstabue.stickynote.activenote.ActiveNoteWindow
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import javax.swing.event.AncestorEvent
import javax.swing.event.AncestorListener

class StickyNoteToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val activeNoteWindow = ActiveNoteWindow()
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(activeNoteWindow.getContent(), "", false)
        activeNoteWindow.getContent().addAncestorListener(object : AncestorListener {
            override fun ancestorAdded(event: AncestorEvent) {
                activeNoteWindow.onAttach()
            }

            override fun ancestorMoved(event: AncestorEvent) {
            }

            override fun ancestorRemoved(event: AncestorEvent) {
                activeNoteWindow.onDetach()
            }
        })
        toolWindow.contentManager.addContent(content)
    }
}
