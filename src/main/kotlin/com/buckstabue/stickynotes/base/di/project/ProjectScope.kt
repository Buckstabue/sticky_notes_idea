package com.buckstabue.stickynotes.base.di.project

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

@PerProject
class ProjectScope @Inject constructor() : CoroutineScope {
    override val coroutineContext = SupervisorJob() + Dispatchers.Default
}
