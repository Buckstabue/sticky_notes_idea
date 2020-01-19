package com.buckstabue.stickynotes.idea.createeditstickynote

data class CreateEditStickyNoteViewModel(
    val mode: Mode,
    val description: String,

    val isCodeBindingChecked: Boolean,
    val isCodeBindingCheckboxEnabled: Boolean,

    val branchNameBoundTo: String?,
    val isBranchBindingChecked: Boolean,

    val isSetActive: Boolean
) {
    enum class Mode(
        val dialogTitle: String
    ) {
        EDIT("Edit Sticky Note"),
        CREATE("New Sticky Note")
    }
}
