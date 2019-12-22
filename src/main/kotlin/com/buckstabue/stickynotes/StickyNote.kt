package com.buckstabue.stickynotes

sealed class StickyNote(
    open val description: String,
    open val isArchived: Boolean
) {
    abstract fun setArchived(archived: Boolean): StickyNote
    abstract fun setDescription(description: String): StickyNote
}

/**
 * Sticky note that doesn't have any context
 */
data class NonBoundStickyNote(
    override val description: String,
    override val isArchived: Boolean = false
) : StickyNote(description = description, isArchived = isArchived) {
    override fun setArchived(archived: Boolean): StickyNote {
        if (this.isArchived == archived) {
            return this
        }
        return copy(isArchived = archived)
    }

    override fun setDescription(description: String): StickyNote {
        if (this.description == description) {
            return this
        }
        return copy(description = description)
    }
}

/**
 * Sticky note that has a code reference
 */
data class FileBoundStickyNote(
    val fileLocation: FileLocation,
    override val description: String,
    override val isArchived: Boolean = false
) : StickyNote(description = description, isArchived = isArchived) {
    override fun setArchived(archived: Boolean): StickyNote {
        if (this.isArchived == archived) {
            return this
        }
        return copy(isArchived = archived)
    }

    override fun setDescription(description: String): StickyNote {
        if (this.description == description) {
            return this
        }
        return copy(description = description)
    }
}

interface FileLocation {
    val fileUrl: String
    val lineNumber: Int
}
