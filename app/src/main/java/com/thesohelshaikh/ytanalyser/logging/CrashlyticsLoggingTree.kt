package com.thesohelshaikh.ytanalyser.logging

import android.util.Log
import timber.log.Timber

/**
 * A custom tree used to add logs to crash reports.
 */
class CrashlyticsLoggingTree(
    private val reporter: CrashReporter
) : Timber.Tree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        // verbose and debug logs are not added to remove noise
        // while also allowing us to decide what gets logged
        // use higher priority than debug to log on crash reports.
        return priority != Log.VERBOSE && priority != Log.DEBUG
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (!isLoggable(tag, priority)) {
            return
        }

        if (t != null) {
            reporter.logException(t)
        } else {
            // tag is null as it needs to be set for each file and hence can't be used
            reporter.log("${getPriorityChar(priority)}: $message")
        }
    }

    /**
     * Returns a single char format for a log level.
     *
     * @param priority Priority of log level.
     * @return First character of log level.
     */
    private fun getPriorityChar(priority: Int): Char {
        return when (priority) {
            Log.VERBOSE -> 'V'
            Log.DEBUG -> 'D'
            Log.INFO -> 'I'
            Log.WARN -> 'W'
            Log.ERROR -> 'E'
            Log.ASSERT -> 'A'
            else -> ' '
        }
    }
}