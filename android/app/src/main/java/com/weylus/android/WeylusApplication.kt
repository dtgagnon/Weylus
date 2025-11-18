package com.weylus.android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class WeylusApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Timber logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            // Plant a custom tree for production that logs to analytics/crash reporting
            Timber.plant(ProductionTree())
        }

        Timber.d("Weylus Application started")
    }

    /**
     * Production tree that only logs warnings and errors
     */
    private class ProductionTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority >= android.util.Log.WARN) {
                // In production, send logs to crash reporting service (e.g., Firebase Crashlytics)
                // For now, just use Android's Log
                android.util.Log.println(priority, tag ?: "Weylus", message)
                t?.let { android.util.Log.println(priority, tag ?: "Weylus", it.stackTraceToString()) }
            }
        }
    }
}
