package com.buckstabue.stickynote

import com.intellij.openapi.project.Project
import javax.inject.Inject

@PerProject
class StickyNoteRepositoryImpl @Inject constructor(
    private val project: Project
) : StickyNoteRepository {
    private val stickyNotes = mutableListOf<StickyNote>()

    override fun addStickyNote(stickyNote: StickyNote) {
        stickyNotes.add(stickyNote)
    }
}
