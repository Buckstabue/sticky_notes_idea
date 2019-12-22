package com.buckstabue.stickynotes

import com.intellij.openapi.project.Project
import java.util.concurrent.ConcurrentHashMap

object AppInjector {
    private val projectComponentMap: MutableMap<Project, ProjectComponent> = ConcurrentHashMap()

    val appComponent: AppComponent by lazy { DaggerAppComponent.create() }

    fun getProjectComponent(project: Project): ProjectComponent {
        return projectComponentMap.getOrPut(project, { appComponent.plusProjectComponent().create(project) })
    }

    fun releaseProjectComponent(project: Project) {
        projectComponentMap.remove(project)
    }
}
