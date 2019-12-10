package com.buckstabue.stickynote.idea

import com.buckstabue.stickynote.FileLocation
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class IdeaFileLocation(
    project: Project,
    file: VirtualFile,
    lineNumber: Int
) : FileLocation {
    internal val fileDescriptor = OpenFileDescriptor(project, file, lineNumber, -1, true)

    override val fileUrl: String = file.url

    override val lineNumber: Int
        get() {
            return getLineNumberFromRangeMarker() ?: fileDescriptor.line
        }

    private fun getLineNumberFromRangeMarker(): Int? {
        val rangeMarker = fileDescriptor.rangeMarker ?: return null
        if (!rangeMarker.isValid) {
            return null
        }
        return rangeMarker.document.getLineNumber(rangeMarker.startOffset)
    }
}
