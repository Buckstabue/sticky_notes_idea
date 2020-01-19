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
        val dialogTitle: String,
        val analyticsCategory: String
    ) {
        EDIT(
            dialogTitle = "Edit Sticky Note",
            analyticsCategory = "EditStickyNote"
        ),
        CREATE(
            dialogTitle = "New Sticky Note",
            analyticsCategory = "CreateStickyNote"
        )
    }
}
