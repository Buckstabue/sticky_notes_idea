package com.buckstabue.stickynotes.idea.createeditstickynote

import com.buckstabue.stickynotes.FileBoundStickyNote
import com.buckstabue.stickynotes.NonBoundStickyNote
import com.buckstabue.stickynotes.StickyNote
import com.buckstabue.stickynotes.StickyNoteInteractor
import com.buckstabue.stickynotes.base.di.project.PerProject
import com.buckstabue.stickynotes.base.di.project.ProjectScope
import com.buckstabue.stickynotes.idea.createeditstickynote.CreateEditStickyNoteViewModel.Mode
import com.buckstabue.stickynotes.vcs.VcsService
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@PerProject
class EditStickyNoteScenario @Inject constructor(
    private val stickyNoteInteractor: StickyNoteInteractor,
    private val projectScope: ProjectScope,
    private val vcsService: VcsService,
    private val project: Project
) {
    companion object {
        private val logger = Logger.getInstance(EditStickyNoteScenario::class.java)
    }

    fun launch(
        stickyNoteToEdit: StickyNote,
        doOnSuccess: ((editedStickyNote: StickyNote) -> Unit)? = null
    ) {
        val editResult = askUserToEditStickyNote(stickyNoteToEdit)
        if (editResult == null) {
            logger.debug("User cancelled editing sticky note")
            return
        }

        val newStickyNote = createStickyNote(
            oldStickyNote = stickyNoteToEdit,
            createStickyNoteResult = editResult
        )

        projectScope.launch {
            stickyNoteInteractor.editStickyNote(
                oldStickyNote = stickyNoteToEdit,
                newStickyNote = newStickyNote
            )
            withContext(Dispatchers.Main) {
                doOnSuccess?.invoke(newStickyNote)
            }
        }
    }

    private fun askUserToEditStickyNote(
        stickyNote: StickyNote
    ): CreateEditStickyNoteResult? {
        val branchNameBoundTo = stickyNote.boundBranchName ?: vcsService.getCurrentBranchName()

        val addStickyNoteDialog = CreateEditStickyNoteDialog(
            initialViewModel = CreateEditStickyNoteViewModel(
                mode = Mode.EDIT,
                description = stickyNote.description,
                isCodeBindingChecked = stickyNote is FileBoundStickyNote,
                isCodeBindingCheckboxEnabled = stickyNote is FileBoundStickyNote,
                branchNameBoundTo = branchNameBoundTo,
                isBranchBindingChecked = stickyNote.boundBranchName != null,
                isSetActive = false
            ),
            project = project
        )
        return if (addStickyNoteDialog.showAndGet()) {
            addStickyNoteDialog.getResult()
        } else {
            null
        }
    }

    private fun createStickyNote(
        oldStickyNote: StickyNote,
        createStickyNoteResult: CreateEditStickyNoteResult
    ): StickyNote {
        val description = createStickyNoteResult.description
        val boundBranchName = createStickyNoteResult.branchNameBoundTo
        val isActive = createStickyNoteResult.isSetActive
        val isArchived = oldStickyNote.isArchived && !createStickyNoteResult.isSetActive
        return if (createStickyNoteResult.isBindToCodeChecked) {
            FileBoundStickyNote(
                fileLocation = (oldStickyNote as FileBoundStickyNote).fileLocation,
                description = description,
                boundBranchName = boundBranchName,
                isActive = isActive,
                isArchived = isArchived
            )
        } else {
            NonBoundStickyNote(
                description = description,
                boundBranchName = boundBranchName,
                isActive = isActive,
                isArchived = isArchived
            )
        }
    }
}
