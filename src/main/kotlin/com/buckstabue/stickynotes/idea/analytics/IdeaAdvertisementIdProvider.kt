package com.buckstabue.stickynotes.idea.analytics

import com.buckstabue.stickynotes.analytics.AdvertisementIdProvider
import com.buckstabue.stickynotes.idea.IdeaConst
import com.intellij.ide.util.PropertiesComponent
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IdeaAdvertisementIdProvider @Inject constructor() : AdvertisementIdProvider {
    companion object {
        private const val KEY_ADV_ID = "${IdeaConst.PROPERTY_PREFIX}.adv_id"
    }

    private val propertiesComponent by lazy { PropertiesComponent.getInstance() }

    override fun getOrCreateDeviceId(): String {
        val storedId = propertiesComponent.getValue(KEY_ADV_ID)
        if (storedId != null) {
            return storedId
        }
        val newAdvId = generateAdvId()
        propertiesComponent.setValue(KEY_ADV_ID, newAdvId)
        return newAdvId
    }

    private fun generateAdvId(): String {
        return UUID.randomUUID().toString()
    }
}
