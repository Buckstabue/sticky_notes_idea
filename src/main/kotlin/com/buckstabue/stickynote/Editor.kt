package com.buckstabue.stickynote

interface Editor {
    fun navigateToLine(fileUrl: String, lineNumber: Int)
}
