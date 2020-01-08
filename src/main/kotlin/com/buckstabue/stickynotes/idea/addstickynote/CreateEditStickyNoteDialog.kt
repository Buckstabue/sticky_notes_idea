package com.buckstabue.stickynotes.idea.addstickynote

import com.buckstabue.stickynotes.idea.StickyNotesWebHelpProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField

class CreateEditStickyNoteDialog(
    initialViewModel: CreateEditStickyNoteViewModel,
    project: Project
) : DialogWrapper(project) {
    private lateinit var content: JPanel
    private lateinit var descriptionInput: JTextField
    private lateinit var codeBindingCheckbox: JCheckBox
    private lateinit var setActiveCheckbox: JCheckBox
    private lateinit var branchBindingCheckbox: JCheckBox

    private val branchNameBoundTo = initialViewModel.branchNameBoundTo

    init {
        init()
        title = "New Sticky Note"
        setResizable(false)

        descriptionInput.text = initialViewModel.description

        codeBindingCheckbox.isSelected = initialViewModel.isCodeBindingChecked
        codeBindingCheckbox.isEnabled = initialViewModel.isCodeBindingCheckboxEnabled

        branchBindingCheckbox.isVisible = initialViewModel.branchNameBoundTo != null
        branchBindingCheckbox.isSelected = initialViewModel.isBranchBindingChecked
        branchBindingCheckbox.text = "Bind to the current branch(${initialViewModel.branchNameBoundTo})"

        setActiveCheckbox.isSelected = initialViewModel.isSetActive
    }

    fun getResult(): CreateEditStickyNoteResult {
        return CreateEditStickyNoteResult(
            description = descriptionInput.text.trim(),
            isBindToCodeChecked = codeBindingCheckbox.isSelected,
            isSetActive = setActiveCheckbox.isSelected,
            branchNameBoundTo = getBranchNameBoundToResult()
        )
    }

    private fun getBranchNameBoundToResult(): String? {
        return if (branchBindingCheckbox.isVisible && branchBindingCheckbox.isSelected) {
            branchNameBoundTo
        } else {
            null
        }

    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return descriptionInput
    }

    override fun createCenterPanel(): JComponent {
        return content
    }

    override fun doValidate(): ValidationInfo? {
        if (descriptionInput.text.isNotBlank()) {
            return null
        }
        return ValidationInfo("Description cannot be empty", descriptionInput)
    }

    override fun getHelpId(): String? {
        return StickyNotesWebHelpProvider.GITHUB_HELP_TOPIC_ID
    }
}
