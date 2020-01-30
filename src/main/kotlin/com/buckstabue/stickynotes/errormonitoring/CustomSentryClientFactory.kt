package com.buckstabue.stickynotes.errormonitoring

import io.sentry.DefaultSentryClientFactory
import io.sentry.context.ContextManager
import io.sentry.context.SingletonContextManager
import io.sentry.dsn.Dsn

class CustomSentryClientFactory : DefaultSentryClientFactory() {
    override fun getContextManager(dsn: Dsn?): ContextManager {
        return SingletonContextManager()
    }
}
