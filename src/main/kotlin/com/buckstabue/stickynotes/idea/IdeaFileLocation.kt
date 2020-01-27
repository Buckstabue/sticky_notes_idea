package com.buckstabue.stickynotes.idea

import com.buckstabue.stickynotes.FileLocation
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager

class IdeaFileLocation private constructor(
    openFileDescriptor: OpenFileDescriptor?,
    private val lastSavedLineNumber: Int,
    private val project: Project,
    override val fileUrl: String
) : FileLocation {

    companion object {
        fun fromFileUrl(
            fileUrl: String,
            lineNumber: Int,
            project: Project
        ): FileLocation {
            return IdeaFileLocation(
                openFileDescriptor = resolveFileDescriptor(
                    fileUrl = fileUrl,
                    lineNumber = lineNumber,
                    project = project
                ),
                fileUrl = fileUrl,
                lastSavedLineNumber = lineNumber,
                project = project
            )
        }

        private fun resolveFileDescriptor(
            fileUrl: String,
            project: Project,
            lineNumber: Int
        ): OpenFileDescriptor? {
            val virtualFile = VirtualFileManager.getInstance()
                .findFileByUrl(fileUrl) ?: return null
            return if (!virtualFile.exists()) {
                null
            } else {
                OpenFileDescriptor(
                    project,
                    virtualFile,
                    lineNumber,
                    -1,
                    true
                )
            }
        }
    }

    var openFileDescriptor: OpenFileDescriptor? = openFileDescriptor
        private set
        get() {
            if (field == null || field?.canNavigate() == false) {
                field = resolveFileDescriptor()
            }
            return field
        }

    private fun resolveFileDescriptor(): OpenFileDescriptor? {
        return resolveFileDescriptor(
            fileUrl = fileUrl,
            project = project,
            lineNumber = lastSavedLineNumber
        )
    }

    override val exists: Boolean
        get() = openFileDescriptor?.canNavigate() == true


    override val lineNumber: Int
        get() {
            return getLineNumberFromRangeMarker() ?: openFileDescriptor?.line ?: lastSavedLineNumber
        }

    private fun getLineNumberFromRangeMarker(): Int? {
        val rangeMarker = openFileDescriptor?.rangeMarker ?: return null
        if (!rangeMarker.isValid) {
            return null
        }
        return rangeMarker.document.getLineNumber(rangeMarker.startOffset)
    }
}
