package com.buckstabue.stickynotes.idea.settings

import com.buckstabue.stickynotes.analytics.Analytics
import com.buckstabue.stickynotes.base.di.AppInjector
import com.intellij.openapi.options.ConfigurableUi
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import javax.inject.Inject
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class StickyNotesSettingsWindow : ConfigurableUi<StickyNotesSettings> {

    @Inject
    protected lateinit var analytics: Analytics

    private lateinit var contentPanel: JPanel
    private lateinit var isAnalyticsEnabledCheckbox: JCheckBox
    private lateinit var analyticsHint: JLabel

    init {
        AppInjector.appComponent.plusStickyNotesSettings()
            .inject(this)
        analyticsHint.foreground = UIUtil.getContextHelpForeground()
        analyticsHint.font = JBUI.Fonts.smallFont()
    }

    override fun isModified(settings: StickyNotesSettings): Boolean {
        return isAnalyticsEnabledCheckbox.isSelected != settings.isAnalyticsEnabled
    }

    override fun apply(settings: StickyNotesSettings) {
        settings.isAnalyticsEnabled = isAnalyticsEnabledCheckbox.isSelected
        analytics.onAnalyticsSettingsChanged()
    }

    override fun reset(settings: StickyNotesSettings) {
        isAnalyticsEnabledCheckbox.isSelected = settings.isAnalyticsEnabled
    }

    override fun getComponent(): JComponent {
        return contentPanel
    }


}
