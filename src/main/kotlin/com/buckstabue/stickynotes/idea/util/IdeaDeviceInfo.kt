package com.buckstabue.stickynotes.idea.util

import com.buckstabue.stickynotes.util.DeviceInfo
import com.buckstabue.stickynotes.util.OsType
import com.intellij.openapi.util.SystemInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IdeaDeviceInfo @Inject constructor() : DeviceInfo {
    override val os: OsType
        get() {
            return when {
                SystemInfo.isWindows -> OsType.WINDOWS
                SystemInfo.isMac -> OsType.MAC
                SystemInfo.isLinux -> OsType.LINUX
                else -> OsType.UNKNOWN
            }
        }
}
