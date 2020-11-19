package com.buckstabue.stickynotes.errormonitoring

interface ErrorLogger {
    fun reportException(stackTrace: String, description: String = "")
    fun logBreadcrumb(message: String, logLevel: LogLevel = LogLevel.DEBUG)
}
