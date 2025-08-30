plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(isdlibs.plugins.androidApplication) apply false
    alias(isdlibs.plugins.composeMultiplatform) apply false
    alias(isdlibs.plugins.compose.compiler) apply false
    alias(isdlibs.plugins.kotlinMultiplatform) apply false
    alias(isdlibs.plugins.android.kotlin.multiplatform.library) apply false
    alias(isdlibs.plugins.androidLint) apply false
    alias(isdlibs.plugins.cocoapods) apply false
}