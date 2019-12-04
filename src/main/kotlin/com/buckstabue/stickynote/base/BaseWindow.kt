package com.buckstabue.stickynote.base

abstract class BaseWindow<VIEW : BaseView, PRESENTER : BasePresenter<VIEW>> {
    protected abstract val presenter: PRESENTER

    fun onAttach() {
        @Suppress("UNCHECKED_CAST")
        presenter.attachView(this as VIEW)
    }

    fun onDetach() {
        presenter.detachView()
    }
}
