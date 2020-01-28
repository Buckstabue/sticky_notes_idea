package com.buckstabue.stickynotes.idea.util

import com.intellij.codeInsight.hint.HintManager
import com.intellij.codeInsight.hint.HintUtil
import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.ui.awt.RelativePoint
import kotlinx.coroutines.future.asCompletableFuture
import org.jetbrains.concurrency.asDeferred
import javax.swing.JComponent

object IdeaUtils {

    fun getCurrentEditor(): Editor? {
        return getDataContext()?.let { CommonDataKeys.EDITOR.getData(it) }
    }

    fun showHintUnderCursor(component: JComponent, text: String) {
        val mousePosition = component.mousePosition ?: return
        // replace spaces with non-breaking space to prevent from cutting out the text in the hint
        val hintComponent = HintUtil.createInformationLabel(text.replace(" ", "&nbsp;"))
        mousePosition.translate(0, -20)
        HintManager.getInstance()
            .showHint(
                hintComponent,
                RelativePoint(component, mousePosition),
                HintManager.HIDE_BY_ANY_KEY or HintManager.HIDE_BY_TEXT_CHANGE,
                -1
            )
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
