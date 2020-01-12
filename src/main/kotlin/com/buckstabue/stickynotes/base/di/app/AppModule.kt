package com.buckstabue.stickynotes.base.di.app

import com.buckstabue.stickynotes.analytics.AdvertisementIdProvider
import com.buckstabue.stickynotes.idea.analytics.IdeaAdvertisementIdProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideAdvertisementIdProvider(): AdvertisementIdProvider {
        return IdeaAdvertisementIdProvider.getInstance()
    }
}
