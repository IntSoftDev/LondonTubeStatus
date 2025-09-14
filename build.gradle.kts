import java.util.Properties

// Load secrets.properties
val secretsProperties = Properties()
val secretsFile = rootProject.file("secrets.properties")
if (secretsFile.exists()) {
    secretsProperties.load(secretsFile.inputStream())
    // Apply secrets as project properties
    secretsProperties.forEach { key, value ->
        project.ext.set(key.toString(), value)
    }
}

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
    alias(isdlibs.plugins.ktlint) apply false
}

subprojects {
    apply(plugin = rootProject.isdlibs.plugins.ktlint.get().pluginId)
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        enableExperimentalRules.set(true)
        verbose.set(true)
        filter {
            exclude { it.file.path.contains("build/") }
        }
    }

    afterEvaluate {
        tasks.named("check").configure {
            dependsOn(tasks.getByName("ktlintCheck"))
        }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}