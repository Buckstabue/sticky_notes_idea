package com.buckstabue.stickynotes.idea.toolwindow

import com.buckstabue.stickynotes.AppInjector
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class StickyNoteToolWindowFactory : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val stickyNoteRouter = StickyNoteRouterImpl(project)
        val toolWindowComponent = AppInjector.getProjectComponent(project)
            .plusStickyNoteToolWindowComponent()
            .create(stickyNoteRouter)

        stickyNoteRouter.onCreate(toolWindowComponent)

        val rootPanel = stickyNoteRouter.getRouterRootPanel()
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(rootPanel, "", false)
        toolWindow.contentManager.addContent(content)

        stickyNoteRouter.openActiveStickyNote()
    }
}
