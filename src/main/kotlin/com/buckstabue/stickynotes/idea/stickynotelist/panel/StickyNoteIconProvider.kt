package com.buckstabue.stickynotes.idea.stickynotelist.panel

import com.buckstabue.stickynotes.FileBoundStickyNote
import com.buckstabue.stickynotes.NonBoundStickyNote
import com.buckstabue.stickynotes.StickyNote
import com.intellij.icons.AllIcons
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.vfs.VirtualFileManager
import javax.inject.Inject
import javax.inject.Singleton
import javax.swing.Icon

@Singleton
class StickyNoteIconProvider @Inject constructor() {
    companion object {
        private val NON_BOUND_ICON by lazy { IconLoader.getIcon("/non_bound_sticky_note.svg") }
        private val DEFAULT_BOUND_ICON by lazy { AllIcons.FileTypes.Unknown }
    }

    fun getIcon(stickyNote: StickyNote): Icon? {
        return when (stickyNote) {
            is NonBoundStickyNote -> NON_BOUND_ICON
            is FileBoundStickyNote -> getIconFromFileBoundStickyNote(stickyNote)
        }
    }

    private fun getIconFromFileBoundStickyNote(stickyNote: FileBoundStickyNote): Icon {
        val virtualFile = VirtualFileManager.getInstance()
            .findFileByUrl(stickyNote.fileLocation.fileUrl) ?: return DEFAULT_BOUND_ICON
        return FileTypeManager.getInstance().getFileTypeByFile(virtualFile).icon
            ?: DEFAULT_BOUND_ICON
    }
}
