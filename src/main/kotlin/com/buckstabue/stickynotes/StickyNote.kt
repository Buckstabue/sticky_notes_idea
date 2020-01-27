package com.buckstabue.stickynotes

sealed class StickyNote(
) {
    abstract val description: String
    abstract val isActive: Boolean
    abstract val isArchived: Boolean
    abstract val boundBranchName: String?
    inline val isBoundToBranch: Boolean
        get() = boundBranchName != null

    abstract fun setActive(active: Boolean): StickyNote
    abstract fun setArchived(archived: Boolean): StickyNote

    fun isVisibleInBranch(branchName: String?): Boolean {
        return !isBoundToBranch || boundBranchName == branchName
    }
}

/**
 * Sticky note that doesn't have any context
 */
data class NonBoundStickyNote(
    override val description: String,
    override val isActive: Boolean = false,
    override val isArchived: Boolean = false,
    override val boundBranchName: String?
) : StickyNote() {
    override fun setActive(active: Boolean): StickyNote {
        if (this.isActive == active) {
            return this
        }
        return copy(isActive = active)
    }

    override fun setArchived(archived: Boolean): StickyNote {
        if (this.isArchived == archived) {
            return this
        }
        return copy(isArchived = archived)
    }
}

/**
 * Sticky note that has a code reference
 */
data class FileBoundStickyNote(
    val fileLocation: FileLocation,
    override val description: String,
    override val isActive: Boolean = false,
    override val isArchived: Boolean = false,
    override val boundBranchName: String?
) : StickyNote() {
    override fun setActive(active: Boolean): StickyNote {
        if (this.isActive == active) {
            return this
        }
        return copy(isActive = active)
    }

    override fun setArchived(archived: Boolean): StickyNote {
        if (this.isArchived == archived) {
            return this
        }
        return copy(isArchived = archived)
    }
}

interface FileLocation {
    val exists: Boolean
    val fileUrl: String
    val lineNumber: Int
}
