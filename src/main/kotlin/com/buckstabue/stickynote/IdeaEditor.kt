package com.buckstabue.stickynote

import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import javax.inject.Inject

@PerProject
class IdeaEditor @Inject constructor(
    private val project: Project
) : Editor {
    override fun navigateToLine(fileUrl: String, lineNumber: Int) {
        val virtualFile = VirtualFileManager.getInstance().findFileByUrl(fileUrl) ?: return
        val fileDescriptor = OpenFileDescriptor(project, virtualFile, lineNumber, -1, true)
        fileDescriptor.navigate(true)
    }
}
