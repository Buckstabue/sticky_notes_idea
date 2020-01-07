package com.buckstabue.stickynotes.base.di

import com.buckstabue.stickynotes.base.di.app.AppComponent
import com.buckstabue.stickynotes.base.di.app.DaggerAppComponent
import com.buckstabue.stickynotes.base.di.project.ProjectComponent
import com.intellij.openapi.project.Project
import java.util.concurrent.ConcurrentHashMap

object AppInjector {
    private val projectComponentMap: MutableMap<Project, ProjectComponent> = ConcurrentHashMap()

    private val appComponent: AppComponent by lazy { DaggerAppComponent.create() }

    fun getProjectComponent(project: Project): ProjectComponent {
        return projectComponentMap.getOrPut(project, { appComponent.plusProjectComponent().create(project) })
    }

    fun releaseProjectComponent(project: Project) {
        projectComponentMap.remove(project)
    }
}
