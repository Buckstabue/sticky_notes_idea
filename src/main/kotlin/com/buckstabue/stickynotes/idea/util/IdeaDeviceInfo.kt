package com.buckstabue.stickynotes.idea.util

import com.buckstabue.stickynotes.util.DeviceInfo
import com.buckstabue.stickynotes.util.OsType
import com.intellij.openapi.application.ApplicationInfo
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

    override val osName: String
        get() = SystemInfo.OS_NAME + " " + SystemInfo.OS_VERSION

    override val ideBuildVersion: String
        get() = ApplicationInfo.getInstance().build.asStringWithoutProductCodeAndSnapshot()

    override val ideProductCode: String
        get() = ApplicationInfo.getInstance().build.productCode
}
