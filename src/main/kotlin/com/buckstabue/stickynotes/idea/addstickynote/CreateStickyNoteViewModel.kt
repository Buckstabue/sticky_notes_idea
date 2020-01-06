package com.buckstabue.stickynotes.idea.addstickynote

data class CreateStickyNoteViewModel(
    val description: String,

    val isCodeBindingChecked: Boolean,
    val isCodeBindingCheckboxEnabled: Boolean,

    val branchNameBoundTo: String?,
    val isBranchBindingChecked: Boolean,

    val isSetActive: Boolean
)
