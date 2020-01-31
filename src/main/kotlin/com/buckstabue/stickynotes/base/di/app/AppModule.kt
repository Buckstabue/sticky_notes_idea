package com.buckstabue.stickynotes.base.di.app

import com.buckstabue.stickynotes.analytics.AdvertisementIdProvider
import com.buckstabue.stickynotes.errormonitoring.ErrorLogger
import com.buckstabue.stickynotes.errormonitoring.SentryErrorLogger
import com.buckstabue.stickynotes.idea.analytics.IdeaAdvertisementIdProvider
import com.buckstabue.stickynotes.idea.util.IdeaDeviceInfo
import com.buckstabue.stickynotes.util.DeviceInfo
import dagger.Binds
import dagger.Module

@Module
interface AppModule {

    @Binds
    fun bindAdvertisementIdProvider(
        advertisementIdProvider: IdeaAdvertisementIdProvider
    ): AdvertisementIdProvider

    @Binds
    fun bindDeviceInfo(deviceInfo: IdeaDeviceInfo): DeviceInfo

    @Binds
    fun bindErrorLogger(errorLogger: SentryErrorLogger): ErrorLogger
}
