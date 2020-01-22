package com.buckstabue.stickynotes.idea.util

import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import kotlinx.coroutines.future.asCompletableFuture
import org.jetbrains.concurrency.asDeferred

object IdeaUtils {

    fun getCurrentEditor(): Editor? {
        return getDataContext()?.let { CommonDataKeys.EDITOR.getData(it) }
    }

    private fun getDataContext(): DataContext? {
        return try {
            DataManager.getInstance()
                .dataContextFromFocusAsync
                .asDeferred()
                .asCompletableFuture()
                .join()
        } catch (e: Throwable) {
            null
        }
    }
}
