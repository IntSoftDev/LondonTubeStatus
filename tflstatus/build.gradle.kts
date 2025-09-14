import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(isdlibs.plugins.maven.publish)
    alias(isdlibs.plugins.kotlinMultiplatform)
    alias(isdlibs.plugins.android.kotlin.multiplatform.library)
    alias(isdlibs.plugins.androidLint)
    alias(isdlibs.plugins.kotlin.serialization)
    alias(isdlibs.plugins.cocoapods)
}

group = "com.intsoftdev"
version = "0.0.1"

mavenPublishing {
    // Define coordinates for the published artifact
    coordinates(
        groupId = group.toString(),
        artifactId = "tflstatus",
        version = version.toString(),
    )

    // Configure POM metadata for the published artifact
    pom {
        name.set("TFL Line Status KMP library")
        description.set("Multiplatform SDK retrieve and show the current status of London Tube lines")
        url.set("https://github.com/IntSoftDev/LondonTubeStatus")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }
        developers {
            developer {
                id.set("azaka01")
                name.set("A Zaka")
                email.set("az@intsoftdev.com")
            }
        }
        scm {
            url.set("https://github.com/IntSoftDev/LondonTubeStatus")
        }
    }

    // Configure publishing to Maven Central (manual mode for debugging)
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = false)

    // Enable GPG signing for all publications
    signAllPublications()
}

kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "com.intsoftdev.tflstatus"
        compileSdk = isdlibs.versions.compileSdk.get().toInt()
        minSdk = isdlibs.versions.minSdk.get().toInt()

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "tflstatus"
    listOf(
        iosArm64(),
        iosSimulatorArm64(),
        // add iosX64 if Intel simulator needed
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = xcfName
            isStatic = true
        }
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                implementation(isdlibs.kotlin.stdlib)
                implementation(isdlibs.napier.logger)
                api(isdlibs.koin.core)
                implementation(isdlibs.koin.compose)
                implementation(isdlibs.koin.compose.viewmodel)
                implementation(isdlibs.ktor.client.core)
                implementation(isdlibs.bundles.ktor.common)
                implementation(isdlibs.coroutines.core)
                implementation(isdlibs.androidx.lifecycle.viewmodel)
                implementation(isdlibs.kotlinx.serialization.json)
                implementation(isdlibs.androidx.lifecycle.runtimeCompose)
            }
        }

        commonTest {
            dependencies {
                implementation(isdlibs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                implementation(isdlibs.ktor.client.okHttp)
                implementation(isdlibs.coroutines.android)
                implementation(isdlibs.androidx.lifecycle.runtimeCompose)
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(isdlibs.androidx.runner)
                implementation(isdlibs.androidx.core)
                implementation(isdlibs.androidx.testExt.junit)
            }
        }

        iosMain {
            dependencies {
                implementation(isdlibs.ktor.client.ios)
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
            }
        }
    }

    cocoapods {
        summary = "TFL Status UI KMP Library"
        homepage = "https://github.com/IntSoftDev/LondonTubeStatus"
        version = "0.0.1"
        ios.deploymentTarget = "17.0"

        framework {
            baseName = "TFLStatus"
            isStatic = true
        }
    }
}
