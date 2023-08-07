import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("kotlinx-serialization")
    id("com.google.devtools.ksp")
    id("com.google.android.gms.oss-licenses-plugin")

    id("io.sentry.android.gradle")
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

android {
    namespace = "com.thesohelshaikh.ytanalyser"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33
        versionCode = 3
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            type = "String",
            name = "YOUTUBE_API_KEY",
            value = properties["YOUTUBE_API_KEY"].toString()
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isPseudoLocalesEnabled = true
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.2"
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

class RoomSchemaArgProvider(
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    val schemaDir: File,
) : CommandLineArgumentProvider {
    override fun asArguments() = listOf("room.schemaLocation=${schemaDir.path}")
}

ksp {
    arg(RoomSchemaArgProvider(File(projectDir, "schemas")))
}

dependencies {

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.10")

    // Retrofit - Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    // Coroutines - Async
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Lifecycle Components
    val lifecycleVersion = "2.6.1"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycleVersion")

    // Room - Caching
    val roomVersion = "2.5.2"
    implementation("androidx.room:room-ktx:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Compose - UI
    val composeBom = platform("androidx.compose:compose-bom:2023.06.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    // Compose - Lifecycle
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.compose.runtime:runtime-livedata")

    val navVersion = "2.6.0"
    implementation("androidx.navigation:navigation-compose:$navVersion")

    implementation("io.coil-kt:coil-compose:2.4.0")

    // List open source notices
    implementation("com.google.android.gms:play-services-oss-licenses:17.0.1")

    // Time operations
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

    implementation("com.jakewharton.timber:timber:5.0.1")
}