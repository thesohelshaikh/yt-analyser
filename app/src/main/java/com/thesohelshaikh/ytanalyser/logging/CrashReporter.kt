package com.thesohelshaikh.ytanalyser.logging

import android.graphics.Rect
import java.util.Locale

/**
 * Reporter to be used to add extra information to crash reports.
 */
interface CrashReporter {

    /**
     * Used to initialize reporter.
     */
    fun init(): CrashReporter

    /**
     * Logs a message to the report. Don't log sensitive information.
     *
     * @param message A message to be logged.
     */
    fun log(message: String)

    /**
     * Logs exception to the report. Use this to log non-fatal exceptions. Crashes should be logged
     * automatically.
     *
     * @param t A `Throwable`to be logged.
     */
    fun logException(t: Throwable)

    /**
     * Adds user id to crash report. Pass a null string to clear user id.
     * If this key is not set then we can assume user is not logged in.
     *
     * @param id A unique identifier for user. `null` if not present.
     */
    fun setUserId(id: String?)

    /**
     * Add custom key value pair to the report.
     *
     * @param key A unique key.
     * @param value A values associated with the `key`.
     */
    fun setCustomKey(key: String, value: String)

    /**
     * Add custom key value pair to the report.
     *
     * @param key A unique key.
     * @param value A values associated with the `key`.
     */
    fun setCustomKey(key: String, value: Boolean)

    /**
     * Add custom key-value pairs to the report.
     *
     * @param keysAndValues A map of key-value pairs to be recorded.
     */
    fun setCustomKeys(keysAndValues: Map<String, String>)

    /**
     * Set network connectivity status.
     *
     * @param isConnected true if network connectivity is present.
     */
    fun setNetworkStatus(isConnected: Boolean) {
        setCustomKey(Keys.IS_NETWORK_CONNECTED, isConnected)
    }

    /**
     * Set device is emulator.
     *
     * @param isEmulator true if current device is an emulator.
     */
    fun setIsEmulator(isEmulator: Boolean) {
        setCustomKey(Keys.IS_EMULATOR, isEmulator)
    }

    /**
     * Screen resolution of the device. Screen resolution in widthxheight format. Ex: 1920x1080
     *
     * @param bounds Current window bounds.
     */
    fun setScreenResolution(bounds: Rect) {
        val width = bounds.width()
        val height = bounds.height()
        setCustomKey(Keys.SCREEN_RESOLUTION, "${width}x$height")
    }

    /**
     * Screen density of the device.
     *
     * @param density Screen density.
     */
    @Suppress("MagicNumber")
    fun setScreenDensity(density: Float) {
        val densityString = when {
            density >= 4.0 -> "xxxhdpi"
            density >= 3.0 -> "xxhdpi"
            density >= 2.0 -> "xhdpi"
            density >= 1.5 -> "hdpi"
            density >= 1.0 -> "mdpi"
            else -> "ldpi"
        }
        setCustomKey(Keys.SCREEN_DENSITY, densityString)
    }

    /**
     * Current user locale. IETF BCP 47 language tag, ex: en-US.
     *
     * @param locale Current device locale.
     */
    fun setLocale(locale: Locale) {
        setCustomKey(Keys.LOCALE, locale.toLanguageTag())
    }

    /**
     * Set supported abis. Used to find CPU architecture.
     *
     * @param supportedAbis List of supported abis on the device.
     */
    fun setCpuAbi(supportedAbis: Array<String>) {
        if (supportedAbis.isEmpty()) return
        val preferredAbi = supportedAbis.asList().first()
        val cpuAbi = if (preferredAbi == "arm64-v8a") "64-Bit" else "32-Bit"
        setCustomKey(Keys.CPU_ABI, cpuAbi)
    }

    /**
     * A holder class for keys to be used as custom keys.
     *
     * @see setCustomKey
     * @see setCustomKeys
     */
    private object Keys {
        const val IS_NETWORK_CONNECTED = "IS_NETWORK_CONNECTED"
        const val IS_EMULATOR = "IS_EMULATOR"
        const val SCREEN_DENSITY = "SCREEN_DENSITY"
        const val SCREEN_RESOLUTION = "SCREEN_RESOLUTION"
        const val LOCALE = "LOCALE"
        const val CPU_ABI = "CPU_ABI"
    }
}