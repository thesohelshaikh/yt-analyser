package com.thesohelshaikh.ytanalyser.logging

import android.util.Log
import io.sentry.Sentry
import io.sentry.SentryLevel
import timber.log.Timber

/**
 * Logs exceptions to Sentry. Logs with level above VERBOSE are only captured.
 */
class SentryLoggingTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE) {
            return
        }

        Sentry.captureMessage(message, getSentryLevel(priority))

        if (t != null) {
            Sentry.captureException(t)
        }
    }

    private fun getSentryLevel(logLevel: Int): SentryLevel {
        return when (logLevel) {
            2 -> SentryLevel.DEBUG
            3 -> SentryLevel.DEBUG
            4 -> SentryLevel.INFO
            5 -> SentryLevel.WARNING
            6 -> SentryLevel.ERROR
            else -> throw IllegalArgumentException("Invalid log level: $logLevel. Supported levels are 2 (DEBUG), 3 (DEBUG), 4 (INFO), 5 (WARNING), 6 (ERROR), 7 (CRITICAL).")
        }
    }
}