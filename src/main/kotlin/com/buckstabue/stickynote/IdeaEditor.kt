package com.buckstabue.stickynote

import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.vfs.VirtualFileManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IdeaEditor @Inject constructor(
) : Editor {
    override fun navigateToLine(fileUrl: String, lineNumber: Int) {
        DataManager.getInstance()
            .dataContextFromFocusAsync
            .onSuccess {
                navigateToLine(fileUrl, lineNumber, it)
            }
            // TODO handle error
    }

    private fun navigateToLine(fileUrl: String, lineNumber: Int, dataContext: DataContext) {
        val project = CommonDataKeys.PROJECT.getData(dataContext) ?: return
        val virtualFile = VirtualFileManager.getInstance().findFileByUrl(fileUrl) ?: return

        val fileDescriptor = OpenFileDescriptor(project, virtualFile, lineNumber, -1, true)
        fileDescriptor.navigate(true)
    }
}
