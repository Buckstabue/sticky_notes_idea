package com.buckstabue.stickynote

import com.intellij.openapi.project.Project
import java.util.*

object ComponentManager {
    private val projectToComponent: MutableMap<Project, ProjectComponent> = WeakHashMap()
    private val appComponent by lazy { AppComponent.INSTANCE }

    fun getProjectComponent(project: Project): ProjectComponent {
        return projectToComponent.getOrPut(project, { appComponent.plusProjectComponentFactory().create(project) })
    }
}
