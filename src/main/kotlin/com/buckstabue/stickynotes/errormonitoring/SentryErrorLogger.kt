package com.buckstabue.stickynotes.errormonitoring

import com.buckstabue.stickynotes.BuildConfig
import com.buckstabue.stickynotes.analytics.AdvertisementIdProvider
import com.buckstabue.stickynotes.util.DeviceInfo
import io.sentry.*
import io.sentry.protocol.User
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

    init {
        Sentry.init { options ->
            options.dsn = DSN
            options.release = BuildConfig.VERSION
            options.serverName = "unknown"
            options.environment = BuildConfig.ENVIRONMENT
            options.dist = "${deviceInfo.ideProductCode}-${deviceInfo.ideBuildVersion}"
            options.setBeforeSend { event, _ ->
                event.setTag("os_type", deviceInfo.os.analyticsValue)
                event.setTag("os_name", deviceInfo.osName)
                event.setTag("os_arch", deviceInfo.osArchitecture)
                event.setTag("java_version", deviceInfo.javaVersion)
                event.setTag("java_vendor", deviceInfo.javaVendor)
                event
            }
        }
        Sentry.setUser(User().apply { id = userProvider.getOrCreateDeviceId() })
    }

    override fun reportException(stackTrace: String, description: String) {
        logBreadcrumb("User description: $description", LogLevel.INFO)
        val sentryId = Sentry.captureMessage(stackTrace, SentryLevel.ERROR)
        Sentry.captureUserFeedback(UserFeedback(sentryId).apply { comments = description })
    }

    override fun logBreadcrumb(message: String, logLevel: LogLevel) {
        Sentry.addBreadcrumb(Breadcrumb(message).apply { level = logLevel.toSentryBreadcrumbLevel() })
    }

}

private fun LogLevel.toSentryBreadcrumbLevel(): SentryLevel = when (this) {
    LogLevel.DEBUG -> SentryLevel.DEBUG
    LogLevel.INFO -> SentryLevel.INFO
}
