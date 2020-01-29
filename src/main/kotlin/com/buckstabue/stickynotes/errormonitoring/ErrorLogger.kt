package com.buckstabue.stickynotes.errormonitoring

interface ErrorLogger {
    fun reportException(e: Throwable, description: String = "")
    fun logBreadcrumb(message: String, logLevel: LogLevel = LogLevel.DEBUG)
}
