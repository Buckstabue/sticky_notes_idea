package com.buckstabue.stickynotes.idea.settings

import com.buckstabue.stickynotes.idea.IdeaConst
import com.intellij.ide.util.PropertiesComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StickyNotesSettings @Inject constructor() {
    companion object {
        private const val KEY_ENABLE_ANALYTICS = "${IdeaConst.PROPERTY_PREFIX}.enable_analytics"
        private const val IS_ANALYTICS_ENABLED_BY_DEFAULT = true
    }

    private val propertiesComponent by lazy { PropertiesComponent.getInstance() }

    var isAnalyticsEnabled: Boolean
        get() = propertiesComponent.getBoolean(
            KEY_ENABLE_ANALYTICS,
            IS_ANALYTICS_ENABLED_BY_DEFAULT
        )
        set(value) {
            propertiesComponent.setValue(
                KEY_ENABLE_ANALYTICS,
                value,
                IS_ANALYTICS_ENABLED_BY_DEFAULT
            )
        }
}
