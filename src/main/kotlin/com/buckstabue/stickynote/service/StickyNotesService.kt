package com.buckstabue.stickynote.service

import com.buckstabue.stickynote.StickyNote
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import kotlinx.coroutines.channels.ReceiveChannel

interface StickyNotesService {
    companion object {
        fun getInstance(project: Project): StickyNotesService {
            return ServiceManager.getService(project, StickyNotesService::class.java)
        }

        fun loadService(project: Project) {
            getInstance(project)
        }
    }

    suspend fun setStickyNotes(stickyNotes: List<StickyNote>)

    fun observeLoadedStickyNotes(): ReceiveChannel<List<StickyNote>>
}
