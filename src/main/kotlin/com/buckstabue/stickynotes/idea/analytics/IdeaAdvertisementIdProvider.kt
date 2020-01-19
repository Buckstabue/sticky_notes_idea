package com.buckstabue.stickynotes.idea.analytics

import com.buckstabue.stickynotes.analytics.AdvertisementIdProvider
import com.buckstabue.stickynotes.idea.IdeaConst
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.components.ServiceManager
import java.util.UUID

class IdeaAdvertisementIdProvider : AdvertisementIdProvider {
    companion object {
        private const val KEY_ADV_ID = "${IdeaConst.PROPERTY_PREFIX}.adv_id"

        fun getInstance(): AdvertisementIdProvider {

            return ServiceManager.getService(
                AdvertisementIdProvider::class.java
            )
        }
    }

    override fun getOrCreateDeviceId(): String {
        val propertiesComponent = PropertiesComponent.getInstance()
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
