package com.buckstabue.stickynotes.util

interface DeviceInfo {
    val os: OsType
    val osName: String
    val ideBuildVersion: String
    val ideProductCode: String
}
