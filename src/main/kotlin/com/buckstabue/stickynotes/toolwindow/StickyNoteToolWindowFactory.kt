package com.buckstabue.stickynotes.toolwindow

import com.buckstabue.stickynotes.AppInjector
import com.buckstabue.stickynotes.service.StickyNotesService
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class StickyNoteToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val stickyNoteRouter = StickyNoteRouterImpl()
        val toolWindowComponent = AppInjector.getProjectComponent(project)
            .plusStickyNoteToolWindowComponent()
            .create(stickyNoteRouter)

        stickyNoteRouter.onCreate(toolWindowComponent)

        val rootPanel = stickyNoteRouter.getRouterRootPanel()
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(rootPanel, "", false)
        toolWindow.contentManager.addContent(content)

        stickyNoteRouter.openActiveStickyNote()

        StickyNotesService.loadService(project)
    }
}
