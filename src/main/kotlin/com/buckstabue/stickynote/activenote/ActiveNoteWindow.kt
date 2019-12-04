package com.buckstabue.stickynote.activenote

import com.buckstabue.stickynote.AppComponent
import com.buckstabue.stickynote.base.BaseWindow
import javax.inject.Inject
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class ActiveNoteWindow : BaseWindow<ActiveNoteView, ActiveNotePresenter>(), ActiveNoteView {
    private lateinit var contentPanel: JPanel
    private lateinit var activeNote: JLabel

    @Inject
    override lateinit var presenter: ActiveNotePresenter

    init {
        AppComponent.INSTANCE.plusActiveNoteComponent().inject(this)
    }


    fun getContent(): JComponent {
        return contentPanel
    }
}
