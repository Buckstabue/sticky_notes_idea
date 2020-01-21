package com.buckstabue.stickynotes.base.di.app

import com.buckstabue.stickynotes.analytics.AdvertisementIdProvider
import com.buckstabue.stickynotes.idea.analytics.IdeaAdvertisementIdProvider
import com.buckstabue.stickynotes.idea.util.IdeaDeviceInfo
import com.buckstabue.stickynotes.util.DeviceInfo
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface AppModule {

    @Module
    companion object {
        @Provides
        @Singleton
        @JvmStatic
        fun provideAdvertisementIdProvider(): AdvertisementIdProvider {
            return IdeaAdvertisementIdProvider.getInstance()
        }
    }

    @Binds
    fun bindDeviceInfo(deviceInfo: IdeaDeviceInfo): DeviceInfo
}
