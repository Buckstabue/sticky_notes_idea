package com.buckstabue.stickynotes.idea.toolwindow

import com.buckstabue.stickynotes.base.di.AppInjector
import com.buckstabue.stickynotes.idea.IdeaConst
import com.buckstabue.stickynotes.idea.StickyNotesWebHelpProvider
import com.buckstabue.stickynotes.idea.toolwindow.activenote.ActiveNoteWindow
import com.buckstabue.stickynotes.idea.toolwindow.di.StickyNoteToolWindowComponent
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import com.intellij.ui.content.ContentFactory

class StickyNoteToolWindowFactory : ToolWindowFactory, DumbAware {
    companion object {
        private const val ID = "Sticky Notes"
    }

    private var wasPresent = false

    override fun shouldBeAvailable(project: Project): Boolean {
        // we place this code here, because we want to register ToolWindowManagerListener ASAP
        // to process toolWindowRegistered(id) callback
        val toolWindowComponent = AppInjector.getProjectComponent(project)
            .plusStickyNoteToolWindowComponent()

        project.messageBus.connect().subscribe(
            ToolWindowManagerListener.TOPIC,
            StickyNoteToolWindowManagerListener(
                project = project,
                toolWindowComponent = toolWindowComponent
            )
        )
        return super.shouldBeAvailable(project)
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val toolWindowComponent = AppInjector.getProjectComponent(project)
            .plusStickyNoteToolWindowComponent()

        val activeNoteWindow = ActiveNoteWindow(toolWindowComponent)

        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(activeNoteWindow.getContent(), "", false)
        toolWindow.contentManager.addContent(content)

        activeNoteWindow.onAttach()

        if (!wasPresent && toolWindow.isExpanded) {
            wasPresent = true
            onFirstPresentInSession(toolWindowComponent)
        }
    }

    private fun onFirstPresentInSession(toolWindowComponent: StickyNoteToolWindowComponent) {
        val activeNoteAnalytics = toolWindowComponent.activeNoteAnalytics()
        if (!wasEverPresent()) {
            activeNoteAnalytics.firstOpen()
        }
        activeNoteAnalytics.toolWindowPresent()
    }

    private fun wasEverPresent(): Boolean {
        val wasEverPresentKey = "${IdeaConst.PROPERTY_PREFIX}.wasEverPresent"
        val propertiesComponent = PropertiesComponent.getInstance()
        if (propertiesComponent.isValueSet(wasEverPresentKey)) {
            return true
        }
        propertiesComponent.setValue(wasEverPresentKey, true)
        return false
    }

    override fun init(window: ToolWindow) {
        window.setSplitMode(true, null)
        window.helpId = StickyNotesWebHelpProvider.GITHUB_HELP_TOPIC_ID
    }

    private inner class StickyNoteToolWindowManagerListener(
        private val project: Project,
        private val toolWindowComponent: StickyNoteToolWindowComponent
    ) : ToolWindowManagerListener {

        private var wasRemoved = false
        private var wasExpanded = true
        private var isRegistered = false

        private val analytics = toolWindowComponent.activeNoteAnalytics()

        override fun toolWindowRegistered(id: String) {
            if (id != ID) {
                return
            }
            val toolWindow =
                ToolWindowManager.getInstance(project)
                    .getToolWindow(ID) ?: return
            wasRemoved = toolWindow.isRemoved
            wasExpanded = toolWindow.isExpanded
            isRegistered = true
        }

        override fun stateChanged() {
            if (!isRegistered) {
                return
            }
            // We need to query the tool window again, because it might have been unregistered when closing the project.
            val toolWindow =
                ToolWindowManager.getInstance(project)
                    .getToolWindow(ID) ?: return
            reportStateToAnalytics(toolWindow)
            wasRemoved = toolWindow.isRemoved
            wasExpanded = toolWindow.isExpanded
        }

        fun reportStateToAnalytics(toolWindow: ToolWindow) {
            if (!wasRemoved && toolWindow.isRemoved) { // removed from the side bar
                analytics.toolWindowRemoved()
                return
            }
            if (wasRemoved && !toolWindow.isRemoved) { // added to the side bar
                analytics.toolWindowAdded()
                return
            }
            if (wasExpanded && !toolWindow.isExpanded) { // collapsed
                analytics.toolWindowCollapsed()
                return
            }
            if (!wasExpanded && toolWindow.isExpanded) { // expanded
                analytics.toolWindowExpanded()
                if (!wasPresent) {
                    wasPresent = true
                    onFirstPresentInSession(toolWindowComponent)
                }
                return
            }
        }
    }
}
