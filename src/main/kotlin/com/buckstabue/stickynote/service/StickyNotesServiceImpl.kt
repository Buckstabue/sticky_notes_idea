package com.buckstabue.stickynote.service

import com.buckstabue.stickynote.AppInjector
import com.buckstabue.stickynote.FileBoundStickyNote
import com.buckstabue.stickynote.FileLocation
import com.buckstabue.stickynote.NonBoundStickyNote
import com.buckstabue.stickynote.StickyNote
import com.buckstabue.stickynote.idea.IdeaFileLocation
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

@State(
    name = "StickyNotes"
)
@ExperimentalCoroutinesApi
class StickyNotesServiceImpl(
    private val project: Project
) : StickyNotesService, PersistentStateComponent<StickyNotesServiceImpl.ServiceState> {
    companion object {
        private val logger = Logger.getInstance(StickyNotesServiceImpl::class.java)
    }

    private var state: ServiceState? = null
    private val loadedStickyNotesChannel = BroadcastChannel<List<StickyNote>>(Channel.CONFLATED)

    private val projectScope = AppInjector.getProjectComponent(project).projectScope()

    override fun setStickyNotes(stickyNotes: List<StickyNote>) {
        state = ServiceState(
            stickyNotes = stickyNotes.map { SerializedStickyNote.fromStickyNote(it) }
        )
    }

    override fun observeLoadedStickyNotes(): ReceiveChannel<List<StickyNote>> {
        return loadedStickyNotesChannel.openSubscription()
    }

    override fun getState(): ServiceState? {
        return state
    }

    override fun loadState(state: ServiceState) {
        this.state = state
        projectScope.launch {
            loadedStickyNotesChannel.send(state.stickyNotes.map { it.toStickyNote(project) })
        }
    }

    data class ServiceState(
        var stickyNotes: List<SerializedStickyNote> = emptyList()
    )

    data class SerializedStickyNote(
        var type: StickyNoteType = StickyNoteType.NON_BOUND_STICKY_NOTE,
        var description: String = "",
        var isDone: Boolean = false,
        var fileUrl: String? = null,
        var lineNumber: Int? = null
    ) {
        fun toStickyNote(project: Project): StickyNote {
            return when (type) {
                StickyNoteType.NON_BOUND_STICKY_NOTE -> NonBoundStickyNote(
                    description = description,
                    isDone = isDone
                )
                StickyNoteType.FILE_BOUND_STICKY_NOTE -> {
                    val fileLocation = extractFileLocation(project)
                    if (fileLocation == null) {
                        logger.error("Couldn't parse file location from $this")
                        NonBoundStickyNote(
                            description = description,
                            isDone = isDone
                        )
                    } else {
                        FileBoundStickyNote(
                            fileLocation = fileLocation,
                            description = description,
                            isDone = isDone
                        )
                    }
                }
            }
        }

        private fun extractFileLocation(project: Project): FileLocation? {
            val fileUrl = fileUrl ?: return null
            val lineNumber = lineNumber ?: return null
            val virtualFile = VirtualFileManager.getInstance().findFileByUrl(fileUrl) ?: return null
            return IdeaFileLocation(
                project = project,
                file = virtualFile,
                lineNumber = lineNumber
            )

        }

        companion object {
            fun fromStickyNote(stickyNote: StickyNote): SerializedStickyNote {
                return when (stickyNote) {
                    is NonBoundStickyNote -> SerializedStickyNote(
                        type = StickyNoteType.NON_BOUND_STICKY_NOTE,
                        description = stickyNote.description,
                        isDone = stickyNote.isDone,
                        fileUrl = null,
                        lineNumber = null
                    )
                    is FileBoundStickyNote -> SerializedStickyNote(
                        type = StickyNoteType.FILE_BOUND_STICKY_NOTE,
                        description = stickyNote.description,
                        isDone = stickyNote.isDone,
                        fileUrl = stickyNote.fileLocation.fileUrl,
                        lineNumber = stickyNote.fileLocation.lineNumber
                    )
                }
            }
        }
    }

    enum class StickyNoteType {
        NON_BOUND_STICKY_NOTE,
        FILE_BOUND_STICKY_NOTE
    }
}
