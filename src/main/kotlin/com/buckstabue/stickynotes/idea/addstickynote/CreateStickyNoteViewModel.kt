package com.buckstabue.stickynotes.idea.addstickynote

data class CreateStickyNoteViewModel(
    val description: String,
    val bindToCode: Boolean,
    val isSetActive: Boolean
)
