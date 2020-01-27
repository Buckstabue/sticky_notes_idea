package com.buckstabue.stickynotes.idea

import com.buckstabue.stickynotes.Editor
import com.buckstabue.stickynotes.FileLocation
import com.buckstabue.stickynotes.base.di.project.PerProject
import java.io.FileNotFoundException
import javax.inject.Inject

@PerProject
class IdeaEditor @Inject constructor(
) : Editor {
    override fun navigateToLine(fileLocation: FileLocation) {
        if (!fileLocation.exists) {
            throw FileNotFoundException("Couldn't find file ${fileLocation.fileUrl}")
        }
        (fileLocation as IdeaFileLocation).openFileDescriptor!!.navigate(true)
    }
}
