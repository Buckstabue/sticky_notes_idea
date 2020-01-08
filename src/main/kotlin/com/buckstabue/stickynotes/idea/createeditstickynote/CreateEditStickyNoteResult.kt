package com.buckstabue.stickynotes.idea.createeditstickynote

data class CreateEditStickyNoteResult(
    val description: String,
    val isBindToCodeChecked: Boolean,
    val branchNameBoundTo: String?,
    val isSetActive: Boolean
)
