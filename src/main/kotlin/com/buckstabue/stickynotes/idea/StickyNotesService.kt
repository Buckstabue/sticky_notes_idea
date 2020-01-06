package com.buckstabue.stickynotes.idea

import com.buckstabue.stickynotes.StickyNote
import kotlinx.coroutines.channels.ReceiveChannel

interface StickyNotesService {
    suspend fun setStickyNotes(stickyNotes: List<StickyNote>)
    fun observeLoadedStickyNotes(): ReceiveChannel<List<StickyNote>>
}
