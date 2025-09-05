import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(isdlibs.plugins.kotlinMultiplatform)
    alias(isdlibs.plugins.androidApplication)
    alias(isdlibs.plugins.composeMultiplatform)
    alias(isdlibs.plugins.compose.compiler)
}

val importLocalKmp: String by project

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
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
            // Import TFLStatus KMP as local dependency
            if (importLocalKmp == "true") {
                implementation(project(":tflstatus"))
            } else {
                // use build from Maven Central
                implementation(isdlibs.intsoftdev.tflstatus)
            }
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(isdlibs.androidx.lifecycle.runtimeCompose)
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
}

dependencies {
    debugImplementation(compose.uiTooling)
}

