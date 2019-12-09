package com.buckstabue.stickynote

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class StickyNoteToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val stickyNoteRouter = StickyNoteRouterImpl()
        AppComponent.init(stickyNoteRouter)

        stickyNoteRouter.onCreate()

        val rootPanel = stickyNoteRouter.getRouterRootPanel()
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(rootPanel, "", false)
        toolWindow.contentManager.addContent(content)

        stickyNoteRouter.openActiveStickyNote()
    }
}
