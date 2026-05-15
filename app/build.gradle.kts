plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)

    //kotlin-serialization-plugin
    kotlin("plugin.serialization")
    //ksp
    alias(libs.plugins.ksp)
    //dagger-hilt
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.avirajsharma.booko"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.avirajsharma.booko"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Retrofit core library
    implementation(libs.retrofit)

    // JSON Converters
    implementation(libs.converter.gson)

    //OkHttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Core Compose support
    implementation(libs.coil.compose)
    // Network support for Coil 3
    implementation(libs.coil.network.okhttp)

    // Material Icons
    implementation(libs.androidx.compose.material.icons.extended)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Kotlin-serialization
    implementation(libs.kotlinx.serialization.json)

    // Dagger-Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    //Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
}