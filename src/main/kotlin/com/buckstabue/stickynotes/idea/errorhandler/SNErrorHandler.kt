package com.buckstabue.stickynotes.idea.errorhandler

import com.buckstabue.stickynotes.base.di.AppInjector
import com.buckstabue.stickynotes.errormonitoring.ErrorLogger
import com.intellij.openapi.diagnostic.ErrorReportSubmitter
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.util.Consumer
import java.awt.Component

class SNErrorHandler : ErrorReportSubmitter() {

    private val errorLogger: ErrorLogger = AppInjector.appComponent.errorLogger()

    override fun getReportActionText(): String {
        return "Report to StickyNote author"
    }

    override fun submit(
        events: Array<out IdeaLoggingEvent>,
        additionalInfo: String?,
        parentComponent: Component,
        consumer: Consumer<SubmittedReportInfo>
    ): Boolean {
        for (event in events) {
            errorLogger.reportException(
                stackTrace = event.throwableText,
                description = additionalInfo.orEmpty()
            )
        }
        consumer.consume(SubmittedReportInfo(SubmittedReportInfo.SubmissionStatus.NEW_ISSUE))
        return true
    }
}
