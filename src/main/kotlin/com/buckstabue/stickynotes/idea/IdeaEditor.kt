package com.buckstabue.stickynotes.idea

import com.buckstabue.stickynotes.Editor
import com.buckstabue.stickynotes.FileLocation
import com.buckstabue.stickynotes.PerProject
import javax.inject.Inject

@PerProject
class IdeaEditor @Inject constructor(
) : Editor {
    override fun navigateToLine(fileLocation: FileLocation) {
        (fileLocation as IdeaFileLocation).fileDescriptor.navigate(true)
    }
}
