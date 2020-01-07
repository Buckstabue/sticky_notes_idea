package com.buckstabue.stickynotes.idea

import com.intellij.openapi.help.WebHelpProvider

class StickyNotesWebHelpProvider : WebHelpProvider() {
    companion object {
        const val GITHUB_HELP_TOPIC_ID = "com.buckstabue.stickynotes.reference.github"
    }

    override fun getHelpPageUrl(helpTopicId: String): String? {
        return when (helpTopicId) {
            GITHUB_HELP_TOPIC_ID -> "https://github.com/Buckstabue/sticky_notes_idea#stickynotes-idea-plugin"
            else -> null
        }
    }
}
