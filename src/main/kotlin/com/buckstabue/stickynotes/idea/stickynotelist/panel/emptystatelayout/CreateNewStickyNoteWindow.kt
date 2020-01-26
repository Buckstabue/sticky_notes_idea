package com.buckstabue.stickynotes.idea.stickynotelist.panel.emptystatelayout

import com.buckstabue.stickynotes.idea.createeditstickynote.CreateStickyNoteScenario
import com.buckstabue.stickynotes.idea.customcomponent.JHyperlink
import javax.swing.JPanel

class CreateNewStickyNoteWindow(
    private val createStickyNoteScenario: CreateStickyNoteScenario
) {
    lateinit var content: JPanel
    private lateinit var addNewStickyNoteLink: JHyperlink

    init {
        addNewStickyNoteLink.setOnLinkClickListener {
            createStickyNoteScenario.launch()
        }
    }
}
