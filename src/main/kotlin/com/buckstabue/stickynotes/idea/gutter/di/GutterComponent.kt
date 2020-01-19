package com.buckstabue.stickynotes.idea.gutter.di

import com.buckstabue.stickynotes.idea.gutter.GutterAnalytics
import dagger.Subcomponent

@PerGutter
@Subcomponent
interface GutterComponent {
    fun gutterAnalytics(): GutterAnalytics
}
