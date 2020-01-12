package com.buckstabue.stickynotes.analytics

interface AdvertisementIdProvider {
    fun getOrCreateDeviceId(): String
}
