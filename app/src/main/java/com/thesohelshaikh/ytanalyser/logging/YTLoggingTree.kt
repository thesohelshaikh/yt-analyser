package com.thesohelshaikh.ytanalyser.logging

import timber.log.Timber

/**
 * A custom tree that uses method name and line number as tags.
 */
class YTLoggingTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String {
        return "[YT][${super.createStackElementTag(element)}]" +
                "[${element.methodName}]:${element.lineNumber}"
    }
}