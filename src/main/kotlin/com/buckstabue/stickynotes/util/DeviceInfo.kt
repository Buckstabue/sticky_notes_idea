package com.buckstabue.stickynotes.util

interface DeviceInfo {
    val os: OsType
    val osName: String
    val osArchitecture: String
    val ideBuildVersion: String
    val ideProductCode: String
    val javaVersion: String
    val javaVendor: String
}
