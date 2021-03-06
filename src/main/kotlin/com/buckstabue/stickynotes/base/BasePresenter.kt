package com.buckstabue.stickynotes.base

import com.buckstabue.stickynotes.idea.MainScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BasePresenter<VIEW : BaseView> {
    protected var view: VIEW? = null

    private var coroutineScope = MainScope()

    fun attachView(view: VIEW) {
        this.view = view
        coroutineScope = MainScope()
        onViewAttached()
    }

    protected open fun onViewAttached() {}

    fun detachView() {
        this.view = null
        coroutineScope.cancel()
    }

    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return coroutineScope.launch(block = block)
    }
}
