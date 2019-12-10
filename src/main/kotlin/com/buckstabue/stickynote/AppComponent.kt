package com.buckstabue.stickynote

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent {
    fun plusProjectComponent(): ProjectComponent.Factory
}
