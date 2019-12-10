package com.buckstabue.stickynote.idea

import com.buckstabue.stickynote.Editor
import com.buckstabue.stickynote.FileLocation
import com.buckstabue.stickynote.PerProject
import javax.inject.Inject

@PerProject
class IdeaEditor @Inject constructor(
) : Editor {
    override fun navigateToLine(fileLocation: FileLocation) {
        (fileLocation as IdeaFileLocation).fileDescriptor.navigate(true)
    }
}
