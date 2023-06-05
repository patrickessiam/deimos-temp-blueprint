package com.deimosdev.frameworkutils.base

import timber.log.Timber

class TimberInit(private val isDev: Boolean) : AppInitializer {
    override fun init(application: CoreApplication) {
        if (isDev) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(FirebaseCrashlyticsReportTree())
        }
    }
}
