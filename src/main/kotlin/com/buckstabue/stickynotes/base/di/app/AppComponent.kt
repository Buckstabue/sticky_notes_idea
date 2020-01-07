package com.buckstabue.stickynotes.base.di.app

import com.buckstabue.stickynotes.base.di.project.ProjectComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent {
    fun plusProjectComponent(): ProjectComponent.Factory
}
