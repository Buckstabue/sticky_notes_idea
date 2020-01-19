package com.buckstabue.stickynotes.idea.createeditstickynote

import com.buckstabue.stickynotes.idea.StickyNotesWebHelpProvider
import com.buckstabue.stickynotes.idea.createeditstickynote.di.CreateEditStickyNoteComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import javax.inject.Inject
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField

class CreateEditStickyNoteDialog(
    initialViewModel: CreateEditStickyNoteViewModel,
    project: Project,
    createEditStickyNoteComponent: CreateEditStickyNoteComponent
) : DialogWrapper(project) {
    private lateinit var content: JPanel
    private lateinit var descriptionInput: JTextField
    private lateinit var codeBindingCheckbox: JCheckBox
    private lateinit var setActiveCheckbox: JCheckBox
    private lateinit var branchBindingCheckbox: JCheckBox

    private val branchNameBoundTo = initialViewModel.branchNameBoundTo
    private var myValidationStarted = false

    @Inject
    protected lateinit var analytics: CreateEditStickyNoteAnalytics

    init {
        createEditStickyNoteComponent.inject(this)
        init()
        title = initialViewModel.mode.dialogTitle
        setResizable(false)

        descriptionInput.text = initialViewModel.description

        codeBindingCheckbox.isSelected = initialViewModel.isCodeBindingChecked
        codeBindingCheckbox.isEnabled = initialViewModel.isCodeBindingCheckboxEnabled
        codeBindingCheckbox.addActionListener {
            analytics.bindToCodeSelected(isChecked = codeBindingCheckbox.isSelected)
        }

        branchBindingCheckbox.isVisible = initialViewModel.branchNameBoundTo != null
        branchBindingCheckbox.isSelected = initialViewModel.isBranchBindingChecked
        branchBindingCheckbox.text =
            "Bind to the current branch(${initialViewModel.branchNameBoundTo})"

        setActiveCheckbox.isSelected = initialViewModel.isSetActive

        analytics.present()
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

    override fun startTrackingValidation() {
        myValidationStarted = true
        super.startTrackingValidation()
    }

    override fun doValidateAll(): MutableList<ValidationInfo> {
        val validationErrors = super.doValidateAll()
        if (!myValidationStarted) {
            validationErrors.forEach {
                analytics.validationError(it.message)
            }
        }
        return validationErrors
    }

    override fun doValidate(): ValidationInfo? {
        if (descriptionInput.text.isNotBlank()) {
            return null
        }
        return ValidationInfo("Description cannot be empty", descriptionInput)
    }

    override fun doCancelAction() {
        super.doCancelAction()
        analytics.cancel()
    }

    override fun doOKAction() {
        super.doOKAction()
        analytics.ok()
    }

    override fun doHelpAction() {
        super.doHelpAction()
        analytics.help()
    }

    override fun getHelpId(): String? {
        return StickyNotesWebHelpProvider.GITHUB_HELP_TOPIC_ID
    }
}
