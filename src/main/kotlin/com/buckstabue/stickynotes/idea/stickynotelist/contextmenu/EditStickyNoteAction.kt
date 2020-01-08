package com.buckstabue.stickynotes.idea.stickynotelist.contextmenu

import com.buckstabue.stickynotes.FileBoundStickyNote
import com.buckstabue.stickynotes.NonBoundStickyNote
import com.buckstabue.stickynotes.StickyNote
import com.buckstabue.stickynotes.base.di.AppInjector
import com.buckstabue.stickynotes.idea.addstickynote.CreateEditStickyNoteDialog
import com.buckstabue.stickynotes.idea.addstickynote.CreateEditStickyNoteResult
import com.buckstabue.stickynotes.idea.addstickynote.CreateEditStickyNoteViewModel
import com.buckstabue.stickynotes.idea.stickynotelist.panel.StickyNoteViewModel
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.swing.JList

class EditStickyNoteAction(
    private val stickyNoteJList: JList<StickyNoteViewModel>
) : AnAction("Edit"), DumbAware {
    companion object {
        private val logger = Logger.getInstance(EditStickyNoteAction::class.java)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val selectedStickyNotes = stickyNoteJList.selectedValuesList.map { it.stickyNote }
        if (selectedStickyNotes.isEmpty()) {
            logger.error("Sticky Notes list is empty, nothing to edit")
            return
        }
        if (selectedStickyNotes.size > 1) {
            logger.error("Cannot edit multiple sticky notes")
            return
        }
        val editedStickyNote = selectedStickyNotes.first()
        val editedStickyNoteIndex = stickyNoteJList.selectedIndex

        val project = e.project
        if (project == null) {
            logger.error("project is null")
            return
        }
        val editResult = askUserToEditStickyNote(editedStickyNote, project)
        if (editResult == null) {
            logger.debug("User cancelled editing sticky note")
            return
        }

        val newStickyNote = createStickyNote(
            oldStickyNote = editedStickyNote,
            createStickyNoteResult = editResult
        )
        val projectComponent = AppInjector.getProjectComponent(project)
        val stickyNoteInteractor = projectComponent.stickyNoteInteractor()
        val projectScope = projectComponent.projectScope()

        projectScope.launch {
            stickyNoteInteractor.editStickyNote(
                oldStickyNote = editedStickyNote,
                newStickyNote = newStickyNote
            )
            withContext(Dispatchers.Main) {
                if (editResult.isSetActive) {
                    stickyNoteJList.selectedIndex = 0
                } else {
                    stickyNoteJList.selectedIndex = editedStickyNoteIndex
                }
            }
        }
    }

    private fun askUserToEditStickyNote(
        stickyNote: StickyNote,
        project: Project
    ): CreateEditStickyNoteResult? {
        val vcsService = AppInjector.getProjectComponent(project).vcsService()
        val branchNameBoundTo = stickyNote.boundBranchName ?: vcsService.getCurrentBranchName()

        val addStickyNoteDialog = CreateEditStickyNoteDialog(
            initialViewModel = CreateEditStickyNoteViewModel(
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
        return if (createStickyNoteResult.isBindToCodeChecked) {
            FileBoundStickyNote(
                fileLocation = (oldStickyNote as FileBoundStickyNote).fileLocation,
                description = createStickyNoteResult.description,
                boundBranchName = createStickyNoteResult.branchNameBoundTo,
                isActive = createStickyNoteResult.isSetActive
            )
        } else {
            NonBoundStickyNote(
                description = createStickyNoteResult.description,
                boundBranchName = createStickyNoteResult.branchNameBoundTo,
                isActive = createStickyNoteResult.isSetActive
            )
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = stickyNoteJList.selectedValuesList.size == 1
    }
}
