package com.buckstabue.stickynotes.idea.toolwindow.activenote

import com.buckstabue.stickynotes.FileBoundStickyNote
import com.buckstabue.stickynotes.StickyNote
import com.buckstabue.stickynotes.StickyNoteInteractor
import com.buckstabue.stickynotes.base.BasePresenter
import com.buckstabue.stickynotes.idea.toolwindow.di.PerToolWindow
import com.intellij.openapi.diagnostic.Logger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import javax.inject.Inject

@PerToolWindow
class ActiveNotePresenter @Inject constructor(
    private val stickyNoteInteractor: StickyNoteInteractor,
    private val analytics: ActiveNoteAnalytics
) : BasePresenter<ActiveNoteView>() {
    companion object {
        private val logger = Logger.getInstance(ActiveNotePresenter::class.java)
    }

    private var activeStickyNote: StickyNote? = null

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    override fun onViewAttached() {
        super.onViewAttached()
        var wasEmpty = false
        launch {
            stickyNoteInteractor.observeActiveStickyNote()
                .consumeEach {
                    activeStickyNote = it
                    val isEmpty = it == null
                    val viewModel = ActiveStickyNoteViewModel(
                        showEmptyState = isEmpty,
                        activeNoteDescription = it?.description.orEmpty(),
                        showOpenActiveStickyNoteButton = it is FileBoundStickyNote
                    )
                    view?.render(viewModel)
                    if (!wasEmpty && isEmpty) {
                        analytics.emptyContent()
                    }
                    wasEmpty = isEmpty
                }
        }
    }

    fun onDoneClick() {
        val activeStickyNote = checkNotNull(activeStickyNote)
        analytics.doneClicked()
        launch {
            stickyNoteInteractor.archiveStickyNote(activeStickyNote)
        }
    }

    fun onOpenActiveStickyNoteButtonClick() {
        val activeStickyNote = activeStickyNote
        if (activeStickyNote == null) {
            logger.error("Cannot open the active sticky note because it's null")
            return
        }
        if (activeStickyNote !is FileBoundStickyNote) {
            logger.error("Cannot open the active sticky note because it's not bound to any file")
            return
        }
        analytics.goToCode()
        stickyNoteInteractor.openStickyNote(activeStickyNote)
    }
}
