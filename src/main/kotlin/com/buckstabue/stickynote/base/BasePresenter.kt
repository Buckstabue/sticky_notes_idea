package com.buckstabue.stickynote.base

abstract class BasePresenter<VIEW : BaseView> {
    protected var view: VIEW? = null

    fun attachView(view: VIEW) {
        this.view = view
        onViewAttached()
    }

    protected open fun onViewAttached() {}

    fun detachView() {
        this.view = null
    }
}
