package com.buckstabue.stickynote.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class BasePresenter<VIEW : BaseView> : CoroutineScope by MainScope() {
    protected var view: VIEW? = null

    fun attachView(view: VIEW) {
        this.view = view
        onViewAttached()
    }

    protected open fun onViewAttached() {}

    fun detachView() {
        this.view = null
        cancel()
    }
}
