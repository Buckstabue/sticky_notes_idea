package com.buckstabue.stickynotes.errormonitoring

import com.buckstabue.stickynotes.BuildConfig
import com.buckstabue.stickynotes.analytics.AdvertisementIdProvider
import com.buckstabue.stickynotes.util.DeviceInfo
import io.sentry.SentryClientFactory
import io.sentry.event.Breadcrumb
import io.sentry.event.BreadcrumbBuilder
import io.sentry.event.UserBuilder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SentryErrorLogger @Inject constructor(
    private val userProvider: AdvertisementIdProvider,
    private val deviceInfo: DeviceInfo
) : ErrorLogger {
    companion object {
        private const val DSN = "https://71b16aaf12154f20a3bffbcf7f6a00ca@sentry.io/2076016"
    }

    private val sentry by lazy {
        SentryClientFactory.sentryClient(DSN)
            .apply {
                release = BuildConfig.VERSION
                dist = "${deviceInfo.ideProductCode}-${deviceInfo.ideBuildVersion}"
                environment = deviceInfo.os.analyticsValue
                context.user = UserBuilder().setId(userProvider.getOrCreateDeviceId()).build()
            }
    }

    override fun reportException(e: Throwable, description: String) {
        if (description.isNotBlank()) {
            logBreadcrumb(
                message = "User description: $description",
                logLevel = LogLevel.INFO
            )
        }
        sentry.sendException(e)
    }

    override fun logBreadcrumb(message: String, logLevel: LogLevel) {
        sentry.context.recordBreadcrumb(
            BreadcrumbBuilder().setMessage(message)
                .setLevel(
                    logLevel.toSentryBreadcrumbLevel()
                ).build()
        )
    }

}

private fun LogLevel.toSentryBreadcrumbLevel(): Breadcrumb.Level = when (this) {
    LogLevel.DEBUG -> Breadcrumb.Level.DEBUG
    LogLevel.INFO -> Breadcrumb.Level.INFO
}
