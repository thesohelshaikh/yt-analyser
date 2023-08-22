// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val kotlinVersion = "1.8.10"
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false

    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
    id("com.google.android.gms.oss-licenses-plugin") version "0.10.6" apply false
    kotlin("plugin.serialization") version kotlinVersion
    id("io.sentry.android.gradle") version "3.11.1" apply false
}