import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(isdlibs.plugins.kotlinMultiplatform)
    alias(isdlibs.plugins.androidApplication)
    alias(isdlibs.plugins.composeMultiplatform)
    alias(isdlibs.plugins.compose.compiler)
}

val importLocalKmp: String by project

// Get TFL API credentials from root project properties (loaded from secrets.properties)
val tflAppId = rootProject.findProperty("tfl.app.id")?.toString() ?: ""
val tflAppKey = rootProject.findProperty("tfl.app.key")?.toString() ?: ""

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
        // iosX64() not needed
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(isdlibs.androidx.compose.activity)
            implementation(isdlibs.koin.android)
        }
        commonMain.dependencies {
            // TFL Status library dependencies
            if (importLocalKmp == "true") {
                // Use local project modules
                implementation(project(":tflstatus"))
                implementation(project(":tflstatus-ui"))
            } else {
                // Use published UI library (includes core library automatically)
                implementation(isdlibs.intsoftdev.tflstatus)
                implementation(isdlibs.intsoftdev.tflstatus.ui)
            }

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(isdlibs.androidx.lifecycle.runtimeCompose)
            implementation(isdlibs.napier.logger)
            api(isdlibs.koin.core)
            implementation(isdlibs.koin.compose)
            implementation(isdlibs.koin.compose.viewmodel)
        }
        commonTest.dependencies {
            implementation(isdlibs.kotlin.test)
        }
    }
}

android {
    namespace = "com.intsoftdev.londontubestatus"
    compileSdk = isdlibs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.intsoftdev.londontubestatus"
        minSdk = isdlibs.versions.minSdk.get().toInt()
        targetSdk = isdlibs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        // Add TFL API credentials to BuildConfig
        buildConfigField("String", "TFL_APP_ID", "\"$tflAppId\"")
        buildConfigField("String", "TFL_APP_KEY", "\"$tflAppKey\"")
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
