package com.buckstabue.stickynotes.idea.addstickynote

data class CreateStickyNoteResult(
    val description: String,
    val isBindToCodeChecked: Boolean,
    val branchNameBoundTo: String?,
    val isSetActive: Boolean
)
