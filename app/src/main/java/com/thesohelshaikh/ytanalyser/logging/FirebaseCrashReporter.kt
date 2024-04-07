package com.thesohelshaikh.ytanalyser.logging

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.crashlytics.setCustomKeys
import com.google.firebase.ktx.Firebase

/**
 * Uses Firebase Crashlytics to report crashes.
 *
 * In variants
 * - Uses singleton pattern as we only one instance throughout the app.
 * - Only 64 custom keys can be stored at a time. Additional keys will be ignored.
 * - Each value must be less than 1 KB in size.
 * - Logs have a maximum size limit of 64 KB. Older log entries are deleted for the current session.
 * - Keys are preserved only for the duration of user session and hence must be set for each
 * session.
 */
object FirebaseCrashReporter : CrashReporter {

    override fun init(): CrashReporter {
        // no-op
        // Crashlytics is initialized automatically
        return this
    }

    override fun log(message: String) {
        Firebase.crashlytics.log(message)
    }

    override fun logException(t: Throwable) {
        Firebase.crashlytics.recordException(t)
    }

    override fun setUserId(id: String?) {
        Firebase.crashlytics.setUserId(id ?: "")
    }

    override fun setCustomKey(key: String, value: String) {
        Firebase.crashlytics.setCustomKey(key, value)
    }

    override fun setCustomKey(key: String, value: Boolean) {
        Firebase.crashlytics.setCustomKey(key, value)
    }

    override fun setCustomKeys(keysAndValues: Map<String, String>) {
        Firebase.crashlytics.setCustomKeys {
            keysAndValues.forEach { entry ->
                key(entry.key, entry.value)
            }
        }
    }
}