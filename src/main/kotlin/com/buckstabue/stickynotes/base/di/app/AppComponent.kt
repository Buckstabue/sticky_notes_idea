package com.buckstabue.stickynotes.base.di.app

import com.buckstabue.stickynotes.base.di.project.ProjectComponent
import com.buckstabue.stickynotes.errormonitoring.ErrorLogger
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun errorLogger(): ErrorLogger

    fun plusProjectComponent(): ProjectComponent.Factory
}
