package com.buckstabue.stickynotes.errormonitoring

import com.buckstabue.stickynotes.BuildConfig
import com.buckstabue.stickynotes.analytics.AdvertisementIdProvider
import com.buckstabue.stickynotes.util.DeviceInfo
import io.sentry.SentryClientFactory
import io.sentry.event.Breadcrumb
import io.sentry.event.BreadcrumbBuilder
import io.sentry.event.UserBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SentryErrorLogger @Inject constructor(
    private val userProvider: AdvertisementIdProvider,
    private val deviceInfo: DeviceInfo
) : ErrorLogger {
    companion object {
        private const val DSN = BuildConfig.SENTRY_DSN
    }

    private val sentry by lazy {
        SentryClientFactory.sentryClient(DSN, CustomSentryClientFactory())
            .apply {
                release = BuildConfig.VERSION
                addTag("os_type", deviceInfo.os.analyticsValue)
                addTag("os_name", deviceInfo.osName)
                addTag("os_arch", deviceInfo.osArchitecture)
                addTag("java_version", deviceInfo.javaVersion)
                addTag("java_vendor", deviceInfo.javaVendor)
                dist = "${deviceInfo.ideProductCode}-${deviceInfo.ideBuildVersion}"
                environment = BuildConfig.ENVIRONMENT
                serverName = "unknown"
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
        GlobalScope.launch {
            sentry.sendException(e)
        }
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
