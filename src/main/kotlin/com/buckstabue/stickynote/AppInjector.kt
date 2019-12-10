package com.buckstabue.stickynote

import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.getOrCreate
import java.util.concurrent.ConcurrentHashMap

object AppInjector {
    private val projectComponentMap: MutableMap<Project, ProjectComponent> = ConcurrentHashMap()

    val appComponent: AppComponent by lazy { DaggerAppComponent.create() }

    fun getProjectComponent(project: Project): ProjectComponent {
        return projectComponentMap.getOrCreate(project, { appComponent.plusProjectComponent().create(project) })
    }

    fun releaseProjectComponent(project: Project) {
        projectComponentMap.remove(project)
    }
}
