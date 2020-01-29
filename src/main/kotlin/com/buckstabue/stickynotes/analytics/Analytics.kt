package com.buckstabue.stickynotes.analytics

import com.brsanthu.googleanalytics.GoogleAnalytics
import com.brsanthu.googleanalytics.GoogleAnalyticsConfig
import com.brsanthu.googleanalytics.request.DefaultRequest
import com.buckstabue.stickynotes.BuildConfig
import com.buckstabue.stickynotes.errormonitoring.ErrorLogger
import com.buckstabue.stickynotes.errormonitoring.LogLevel
import com.buckstabue.stickynotes.util.DeviceInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Analytics @Inject constructor(
    private val advertisementIdProvider: AdvertisementIdProvider,
    private val deviceInfo: DeviceInfo,
    private val errorLogger: ErrorLogger
) {
    companion object {
        private const val OS_DIMENSION = 1
        private const val IDE_BUILD_VERSION_DIMENSION = 2
        private const val IDE_PRODUCT_CODE_DIMENSION = 3
        private const val IS_FIRST_OPEN_DIMENSION = 4
        private const val SOURCE_DIMENSION = 5
    }

    private val ga = createGoogleAnalytics()

    private fun createGoogleAnalytics(): GoogleAnalytics? {
        if (BuildConfig.GA_ACCOUNT_ID.isBlank()) {
            return null
        }
        return GoogleAnalytics.builder()
            .withDefaultRequest(
                DefaultRequest()
                    .clientId(advertisementIdProvider.getOrCreateDeviceId())
                    .customDimension(OS_DIMENSION, deviceInfo.os.analyticsValue)
                    .customDimension(IDE_BUILD_VERSION_DIMENSION, deviceInfo.ideBuildVersion)
                    .customDimension(IDE_PRODUCT_CODE_DIMENSION, deviceInfo.ideProductCode)
            )
            .withConfig(
                GoogleAnalyticsConfig()
                    .setUserAgent("${BuildConfig.NAME}:${BuildConfig.VERSION}")
            )
            .withAppName(BuildConfig.NAME)
            .withAppVersion(BuildConfig.VERSION)
            .withTrackingId(BuildConfig.GA_ACCOUNT_ID)
            .build()
    }

    fun sendEvent(
        category: String,
        action: String,
        label: String? = null,
        value: Int? = null,

        source: String? = null,
        isFirstOpen: Boolean? = null
    ) {
        val ga = ga ?: return
        ga.event()
            .eventCategory(category)
            .eventAction(action)
            .eventLabel(label)
            .eventValue(value)
            .also {
                if (source != null) {
                    it.customDimension(SOURCE_DIMENSION, source)
                }
                if (isFirstOpen != null) {
                    it.customDimension(IS_FIRST_OPEN_DIMENSION, isFirstOpen.toString())
                }
            }
            .sendAsync()
        errorLogger.logBreadcrumb(
            message = "sent event: category=$category, action=$action",
            logLevel = LogLevel.DEBUG
        )
    }
}
