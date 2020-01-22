package com.buckstabue.stickynotes.analytics

import com.brsanthu.googleanalytics.GoogleAnalytics
import com.brsanthu.googleanalytics.GoogleAnalyticsConfig
import com.brsanthu.googleanalytics.request.DefaultRequest
import com.buckstabue.stickynotes.BuildConfig
import com.buckstabue.stickynotes.util.DeviceInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Analytics @Inject constructor(
    private val advertisementIdProvider: AdvertisementIdProvider,
    private val deviceInfo: DeviceInfo
) {
    companion object {
        private const val OS_DIMENSION = 1
        private const val IDE_BUILD_VERSION_DIMENSION = 2
        private const val IDE_PRODUCT_CODE_DIMENSION = 3
        const val IS_FIRST_OPEN_DIMENSION = 4
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
        customDimensions: Map<Int, String> = emptyMap(),
        customMetrics: Map<Int, String> = emptyMap()
    ) {
        val ga = ga ?: return
        ga.event()
            .eventCategory(category)
            .eventAction(action)
            .eventLabel(label)
            .eventValue(value)
            .also {
                for (entry in customDimensions) {
                    it.customDimension(entry.key, entry.value)
                }
            }.also {
                for (entry in customMetrics) {
                    it.customMetric(entry.key, entry.value)
                }
            }
            .sendAsync()
    }
}
