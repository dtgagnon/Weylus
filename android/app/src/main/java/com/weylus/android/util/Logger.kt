package com.weylus.android.util

import timber.log.Timber

/**
 * Wrapper for logging to make it easier to switch logging frameworks if needed
 */
object Logger {

    fun d(message: String, vararg args: Any?) {
        Timber.d(message, *args)
    }

    fun d(t: Throwable, message: String, vararg args: Any?) {
        Timber.d(t, message, *args)
    }

    fun i(message: String, vararg args: Any?) {
        Timber.i(message, *args)
    }

    fun i(t: Throwable, message: String, vararg args: Any?) {
        Timber.i(t, message, *args)
    }

    fun w(message: String, vararg args: Any?) {
        Timber.w(message, *args)
    }

    fun w(t: Throwable, message: String, vararg args: Any?) {
        Timber.w(t, message, *args)
    }

    fun e(message: String, vararg args: Any?) {
        Timber.e(message, *args)
    }

    fun e(t: Throwable, message: String, vararg args: Any?) {
        Timber.e(t, message, *args)
    }

    fun e(t: Throwable) {
        Timber.e(t)
    }

    fun wtf(message: String, vararg args: Any?) {
        Timber.wtf(message, *args)
    }

    fun wtf(t: Throwable, message: String, vararg args: Any?) {
        Timber.wtf(t, message, *args)
    }
}
