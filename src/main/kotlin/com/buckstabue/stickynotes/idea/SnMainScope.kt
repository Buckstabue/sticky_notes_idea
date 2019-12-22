package com.buckstabue.stickynotes.idea

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class MainScope : CoroutineScope {
    override val coroutineContext: CoroutineContext = SupervisorJob(parent = null) + Dispatchers.Main

    fun cancel() {
    }
}
