package com.buckstabue.stickynotes

import kotlinx.coroutines.channels.ReceiveChannel

interface StickyNotesService {
    suspend fun setStickyNotes(stickyNotes: List<StickyNote>)
    fun observeLoadedStickyNotes(): ReceiveChannel<List<StickyNote>>
}
