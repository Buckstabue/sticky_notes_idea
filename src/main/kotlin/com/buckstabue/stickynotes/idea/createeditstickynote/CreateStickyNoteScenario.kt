package com.buckstabue.stickynotes.idea.createeditstickynote

import com.buckstabue.stickynotes.FileBoundStickyNote
import com.buckstabue.stickynotes.FileLocation
import com.buckstabue.stickynotes.NonBoundStickyNote
import com.buckstabue.stickynotes.StickyNote
import com.buckstabue.stickynotes.StickyNoteInteractor
import com.buckstabue.stickynotes.base.di.project.ProjectScope
import com.buckstabue.stickynotes.idea.IdeaFileLocation
import com.buckstabue.stickynotes.idea.createeditstickynote.di.PerCreateEditStickyNote
import com.buckstabue.stickynotes.vcs.VcsService
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@PerCreateEditStickyNote
class CreateStickyNoteScenario @Inject constructor(
    private val stickyNoteInteractor: StickyNoteInteractor,
    private val projectScope: ProjectScope,
    private val project: Project,
    private val vcsService: VcsService,
    private val analytics: CreateEditStickyNoteAnalytics
) {
    companion object {
        private val logger = Logger.getInstance(CreateStickyNoteScenario::class.java)
    }

    fun launch(
        editor: Editor? = null,
        codeBindingEnabledByDefaultWhenPossible: Boolean = true,
        doOnSuccess: ((editedStickyNote: StickyNote) -> Unit)? = null
    ) {
        val fileLocation = editor?.let { extractEditorCaretLocation(it, project) }
        val canBindToCode = fileLocation != null

        val createStickyNoteResult = askUserToFillStickyNoteSettings(
            canBindToCode = canBindToCode,
            codeBindingEnabledByDefaultWhenPossible = codeBindingEnabledByDefaultWhenPossible,
            project = project
        )
        if (createStickyNoteResult == null) {
            logger.debug("User cancelled sticky note description input")
            // user cancelled
            return
        }

        val stickyNote = createStickyNote(fileLocation, createStickyNoteResult)

        projectScope.launch {
            stickyNoteInteractor.addStickyNote(stickyNote)
            logger.debug("Sticky note successfully added $stickyNote")
            withContext(Dispatchers.Main) {
                doOnSuccess?.invoke(stickyNote)
            }
        }
    }

    private fun extractEditorCaretLocation(
        editor: Editor,
        project: Project
    ): IdeaFileLocation? {
        val currentDocument = editor.document
        val currentLineNumber = editor.caretModel.logicalPosition.line
        val currentFile = FileDocumentManager.getInstance()
            .getFile(currentDocument)
        if (currentFile == null) {
            logger.error("Could not extract virtual file from document")
            return null
        }
        return IdeaFileLocation(
            project = project,
            file = currentFile,
            lineNumber = currentLineNumber
        )
    }

    private fun createStickyNote(
        fileLocation: FileLocation?,
        createStickyNoteResult: CreateEditStickyNoteResult
    ): StickyNote {
        return if (fileLocation == null || !createStickyNoteResult.isBindToCodeChecked) {
            NonBoundStickyNote(
                description = createStickyNoteResult.description,
                boundBranchName = createStickyNoteResult.branchNameBoundTo,
                isActive = createStickyNoteResult.isSetActive
            )
        } else {
            FileBoundStickyNote(
                fileLocation = fileLocation,
                description = createStickyNoteResult.description,
                boundBranchName = createStickyNoteResult.branchNameBoundTo,
                isActive = createStickyNoteResult.isSetActive
            )
        }
    }

    private fun askUserToFillStickyNoteSettings(
        canBindToCode: Boolean,
        codeBindingEnabledByDefaultWhenPossible: Boolean,
        project: Project
    ): CreateEditStickyNoteResult? {
        val addStickyNoteDialog = CreateEditStickyNoteDialog(
            initialViewModel = CreateEditStickyNoteViewModel(
                mode = CreateEditStickyNoteViewModel.Mode.CREATE,
                description = "",
                isCodeBindingChecked = canBindToCode && codeBindingEnabledByDefaultWhenPossible,
                isCodeBindingCheckboxEnabled = canBindToCode,
                branchNameBoundTo = vcsService.getCurrentBranchName(),
                isBranchBindingChecked = false,
                isSetActive = false
            ),
            project = project,
            analytics = analytics
        )
        return if (addStickyNoteDialog.showAndGet()) {
            addStickyNoteDialog.getResult()
        } else {
            null
        }
    }
}
