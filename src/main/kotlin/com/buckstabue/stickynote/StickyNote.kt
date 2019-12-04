package com.buckstabue.stickynote

sealed class StickyNote(
    open val description: String
)

/**
 * Sticky note that doesn't have any context
 */
data class NonBoundStickyNote(
    override val description: String
) : StickyNote(description = description)

/**
 * Sticky note that has a code reference
 */
data class FileBoundStickyNote(
    val fileUrl: String,
    val lineNumber: Int,
    override val description: String
) : StickyNote(description = description)
