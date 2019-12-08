package com.buckstabue.stickynote.base

import javax.swing.JComponent

abstract class BaseWindow<VIEW : BaseView, PRESENTER : BasePresenter<VIEW>> {
    protected abstract val presenter: PRESENTER

    abstract val routingTag: String

    open fun onCreate() {
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
