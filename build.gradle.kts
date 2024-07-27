// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val kotlinVersion = "1.8.10"
    id("com.android.application") version "8.5.1" apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false

    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
    id("com.google.android.gms.oss-licenses-plugin") version "0.10.6" apply false
    kotlin("plugin.serialization") version kotlinVersion
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
}