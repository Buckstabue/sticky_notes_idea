package com.buckstabue.stickynotes.idea

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

// TODO remove me when min version is updated or bug https://youtrack.jetbrains.com/issue/IDEA-229657 is resolved
class MainScope : CoroutineScope {
    override val coroutineContext: CoroutineContext = SupervisorJob(parent = null) + Dispatchers.Main

    fun cancel() {
    }
}
