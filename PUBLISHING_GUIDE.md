# TFL Status KMP Library Publishing Guide

This guide covers how to publish both the `tflstatus-core` core library and `tflstatus-ui` Compose
library to Maven Central.

## 📋 Prerequisites

### 1. Maven Central Account Setup

1. **Create Sonatype Account**:
    - Go to [Sonatype Central Portal](https://central.sonatype.com/)
    - Register with your GitHub account
    - Verify your namespace e.g `com.<domainname>`

2. **Generate GPG Key for Signing**:

```bash
# Generate GPG key
gpg --gen-key

# List keys to get the key ID
gpg --list-secret-keys --keyid-format LONG

# Export public key (replace KEY_ID with your actual key ID)
gpg --armor --export KEY_ID > public-key.asc

# Export private key
gpg --armor --export-secret-keys KEY_ID > private-key.asc
```

3. **Upload Public Key**:

```bash
# Upload to key server
gpg --keyserver keyserver.ubuntu.com --send-keys KEY_ID
```

### 2. Configure Gradle Properties

Create or update `gradle.properties` in your home directory (`~/.gradle/gradle.properties`):

```properties
# Maven Central credentials
mavenCentralUsername=your_sonatype_username
mavenCentralPassword=your_sonatype_password

# GPG signing
signing.keyId=YOUR_GPG_KEY_ID
signing.password=YOUR_GPG_PASSPHRASE
signing.secretKeyRingFile=/Users/yourusername/.gnupg/secring.gpg

# Alternative GPG configuration (recommended for newer GPG versions)
signing.gnupg.executable=gpg
signing.gnupg.useLegacyGpg=false
signing.gnupg.keyName=YOUR_GPG_KEY_ID
signing.gnupg.passphrase=YOUR_GPG_PASSPHRASE
```

### 3. Project Configuration

Update both library `build.gradle.kts` files:

```kotlin
// For tflstatus-core/build.gradle.kts and tflstatus-ui/build.gradle.kts
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(isdlibs.plugins.maven.publish)
    // ... other plugins
}

group = "<com.yourdomainname>" // Update as needed
version = "0.0.2" // Update version as needed

mavenPublishing {
    coordinates(
        groupId = group.toString(),
        artifactId = "tflstatus-core", // or "tflstatus-ui"
        version = version.toString()
    )

    pom {
        name.set("TFL Line Status KMP library") // Update for each library
        description.set("Multiplatform SDK retrieve and show the current status of London Tube lines")
        inceptionYear.set("2025")
        url.set("https://github.com/IntSoftDev/LondonTubeStatus")
        
        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
                distribution.set("https://opensource.org/licenses/MIT")
            }
        }
        
        developers {
            developer {
                id.set("<developerId>")
                name.set("<developerName>")
                email.set("<email>")
                url.set("<githubURL>")
                roles.set(listOf("<role>"))
                timezone.set("<Timezone>")
            }
        }
        
        scm {
            url.set("https://github.com/IntSoftDev/LondonTubeStatus")
            connection.set("scm:git:git://github.com/IntSoftDev/LondonTubeStatus.git")
            developerConnection.set("scm:git:ssh://git@github.com/IntSoftDev/LondonTubeStatus.git")
        }
        
        issueManagement {
            system.set("GitHub Issues")
            url.set("https://github.com/IntSoftDev/LondonTubeStatus/issues")
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}
```

## 🚀 Publishing Process

### 1. Version Management

Follow [Semantic Versioning](https://semver.org/):

- **Major** (1.0.0 → 2.0.0): Breaking changes
- **Minor** (1.0.0 → 1.1.0): New features, backward compatible
- **Patch** (1.0.0 → 1.0.1): Bug fixes, backward compatible

### 2. Pre-Publishing Checklist

- [ ] Update version numbers in `build.gradle.kts`
- [ ] Update `CHANGELOG.md` with new features/fixes
- [ ] Run all tests: `./gradlew test`
- [ ] Build all targets: `./gradlew build`
- [ ] Generate documentation: `./gradlew dokkaHtml`
- [ ] Verify GPG signing works: `./gradlew signAllPublications`

### 3. Publishing Commands

#### For Core Library (`tflstatus-core`)

```bash
# Navigate to tflstatus-core directory
cd tflstatus-core

# Clean and build
./gradlew clean build

# Publish to staging repository
./gradlew publishToMavenCentral

```

#### For UI Library (`tflstatus-ui`)

```bash
# Navigate to tflstatus-ui directory
cd tflstatus-ui

# Clean and build
./gradlew clean build

# Publish to staging repository
./gradlew publishToMavenCentral
```

#### Publish Both Libraries (from root)

```bash
# From project root - publish to staging
./gradlew :tflstatus-core:publishToMavenCentral
./gradlew :tflstatus-ui:publishToMavenCentral

# Or publish and release in one step
./gradlew :tflstatus-core:publishAndReleaseToMavenCentral
./gradlew :tflstatus-ui:publishAndReleaseToMavenCentral
```

## 📦 Library Distribution Strategy

### Core Library (`com.<domainname>:tflstatus-core`)

**What it contains:**

- ✅ Business logic and data layer
- ✅ ViewModels and use cases
- ✅ Network layer and repositories
- ✅ Data models
- ✅ Koin dependency injection modules
- ❌ No UI components

**Target audience:** Developers who want TFL data with custom UI

### UI Library (`com.<domainname>:tflstatus-ui`)

**What it contains:**

- ✅ Complete Compose UI components
- ✅ Pre-built TFL Status screen
- ✅ TFL line colors and branding
- ✅ Depends on core library
- ✅ Ready-to-use menu integration

**Target audience:** Developers who want plug-and-play UI

## 📖 Usage Documentation

### For Core Library Only

```kotlin
// In your app's build.gradle.kts
dependencies {
    implementation("com.<domainname>:tflstatus-core:<version>")
}

// Usage example
class YourViewModel(
    private val getTFLStatusUseCase: GetTFLStatusUseCase
) : ViewModel() {
    
    fun loadTubeStatus() {
        viewModelScope.launch {
            val lines = getTFLStatusUseCase.execute("victoria,central,northern")
            // Handle the data with your custom UI
        }
    }
}
```

### For Complete UI Solution

```kotlin
// In your app's build.gradle.kts
dependencies {
   implementation("com.<domainname>:tflstatus-core:<version>")
   implementation("com.<domainname>:tflstatus-ui:<version>")
}

// Usage example
@Composable
fun YourApp() {
    // From menu or navigation
    TflStatusUI(
        onBackPressed = { /* handle back */ }
    )
}
```

## 🚨 Troubleshooting

### Common Issues

1. **GPG Signing Fails**:

```bash
# Ensure GPG is properly configured
gpg --list-secret-keys
export GPG_TTY=$(tty)
```

2. **Namespace Verification**:
    - Ensure you own the `<com.yourdomainname>` namespace in Maven Central
    - Add DNS TXT record or GitHub repository verification

3. **Dependencies Not Found**:
    - Check if all dependencies are available on Maven Central
    - Ensure version compatibility

4. **Publication Fails**:

```bash
# Check available staging tasks
./gradlew tasks | grep staging

# Create staging repository
./gradlew createStagingRepository

# Publish to staging repository
./gradlew publishToMavenCentral

# Release staging repository
./gradlew releaseRepository
```

## 📋 Release Checklist

Before each release:

- [ ] Version bumped in both libraries
- [ ] Dependencies updated to latest stable versions
- [ ] All tests passing on Android and iOS
- [ ] Documentation updated
- [ ] CHANGELOG.md updated
- [ ] Git tag created: `git tag v0.0.2`
- [ ] GitHub release created with release notes
- [ ] Libraries published to Maven Central
- [ ] Integration tested in sample apps
- [ ] Documentation published (if applicable)

## 📈 Version Compatibility Matrix

| tflstatus-ui | tflstatus-core | Compose Multiplatform | Notes                  |
|--------------|----------------|-----------------------|------------------------|
| 0.0.2        | 0.0.2          | 1.8.1                 | Current release        |
| 1.0.0        | 1.0.0          | 1.8.1                 | Planned stable release |
| 1.1.0        | 1.0.x          | 1.8.1                 | UI enhancements        |
| 2.0.0        | 2.0.0          | 1.9.0                 | Breaking changes       |

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/IntSoftDev/LondonTubeStatus/issues)
- **Discussions**: [GitHub Discussions](https://github.com/IntSoftDev/LondonTubeStatus/discussions)
- **Email**: az@intsoftdev.com

---

**Next Steps**: After publishing, create integration examples and update the main README with usage
instructions for both libraries.