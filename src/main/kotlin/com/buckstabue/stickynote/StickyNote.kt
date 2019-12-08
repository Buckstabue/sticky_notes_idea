package com.buckstabue.stickynote

sealed class StickyNote(
    open val description: String,
    open val isDone: Boolean
) {
    abstract fun setDone(isDone: Boolean): StickyNote
}

/**
 * Sticky note that doesn't have any context
 */
data class NonBoundStickyNote(
    override val description: String,
    override val isDone: Boolean = false
) : StickyNote(description = description, isDone = isDone) {
    override fun setDone(isDone: Boolean): StickyNote {
        return copy(isDone = isDone)
    }
}

/**
 * Sticky note that has a code reference
 */
data class FileBoundStickyNote(
    val fileUrl: String,
    val lineNumber: Int,
    override val description: String,
    override val isDone: Boolean = false
) : StickyNote(description = description, isDone = isDone) {
    override fun setDone(isDone: Boolean): StickyNote {
        return copy(isDone = isDone)
    }
}
