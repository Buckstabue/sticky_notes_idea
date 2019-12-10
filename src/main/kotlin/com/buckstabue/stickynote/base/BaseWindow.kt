package com.buckstabue.stickynote.base

import com.buckstabue.stickynote.toolwindow.StickyNoteToolWindowComponent
import javax.swing.JComponent

abstract class BaseWindow<VIEW : BaseView, PRESENTER : BasePresenter<VIEW>> {
    protected abstract val presenter: PRESENTER

    abstract val routingTag: String

    open fun onCreate(toolWindowComponent: StickyNoteToolWindowComponent) {
    }

    fun onAttach() {
        @Suppress("UNCHECKED_CAST")
        presenter.attachView(this as VIEW)
    }

    fun onDetach() {
        presenter.detachView()
    }

    abstract fun getContent(): JComponent
}
