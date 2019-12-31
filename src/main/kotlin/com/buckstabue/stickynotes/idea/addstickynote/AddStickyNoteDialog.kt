package com.buckstabue.stickynotes.idea.addstickynote

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField

class AddStickyNoteDialog(
    canBindToCode: Boolean,
    project: Project
) : DialogWrapper(project) {
    private lateinit var content: JPanel
    private lateinit var descriptionInput: JTextField
    private lateinit var bindToCodeCheckbox: JCheckBox

    init {
        init()
        title = "New Sticky Note"
        setResizable(false)
        bindToCodeCheckbox.isSelected = canBindToCode
        bindToCodeCheckbox.isEnabled = canBindToCode
    }

    fun getViewModel(): CreateStickyNoteViewModel {
        return CreateStickyNoteViewModel(
            description = descriptionInput.text.trim(),
            bindToCode = bindToCodeCheckbox.isSelected
        )
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
}
