# TFL Status KMP Library Publishing Guide

This guide covers how to publish both the `tflstatus` core library and `tflstatus-ui` Compose
library to Maven Central.

## 📋 Prerequisites

### 1. Maven Central Account Setup

1. **Create Sonatype Account**:
    - Go to [Sonatype Central Portal](https://central.sonatype.com/)
    - Register with your GitHub account
    - Verify your namespace `com.intsoftdev`

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
// For tflstatus/build.gradle.kts and tflstatus-ui/build.gradle.kts
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(isdlibs.plugins.maven.publish)
    // ... other plugins
}

group = "com.intsoftdev"
version = "1.0.0" // Update version as needed

mavenPublishing {
    coordinates(
        groupId = group.toString(),
        artifactId = "tflstatus", // or "tflstatus-ui"
        version = version.toString()
    )

    pom {
        name.set("TFL Line Status KMP library") // Update for each library
        description.set("Multiplatform SDK retrieve and show the current status of London Tube lines")
        inceptionYear.set("2024")
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
                id.set("azaka01")
                name.set("A Zaka")
                email.set("az@intsoftdev.com")
                url.set("https://github.com/azaka01")
                roles.set(listOf("Developer"))
                timezone.set("GMT")
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

#### For Core Library (`tflstatus`)

```bash
# Navigate to tflstatus directory
cd tflstatus

# Clean and build
./gradlew clean build

# Publish to staging repository
./gradlew publishToSonatype

# Close and release (if auto-release is disabled)
./gradlew closeAndReleaseSonatypeStagingRepository
```

#### For UI Library (`tflstatus-ui`)

```bash
# Navigate to tflstatus-ui directory
cd tflstatus-ui

# Clean and build
./gradlew clean build

# Publish to staging repository
./gradlew publishToSonatype

# Close and release (if auto-release is disabled)
./gradlew closeAndReleaseSonatypeStagingRepository
```

#### Publish Both Libraries (from root)

```bash
# From project root
./gradlew :tflstatus:publishToSonatype
./gradlew :tflstatus-ui:publishToSonatype

# Or publish all at once
./gradlew publishToSonatype
```

## 📦 Library Distribution Strategy

### Core Library (`com.intsoftdev:tflstatus`)

**What it contains:**

- ✅ Business logic and data layer
- ✅ ViewModels and use cases
- ✅ Network layer and repositories
- ✅ Data models
- ✅ Koin dependency injection modules
- ❌ No UI components

**Target audience:** Developers who want TFL data with custom UI

### UI Library (`com.intsoftdev:tflstatus-ui`)

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
    implementation("com.intsoftdev:tflstatus:1.0.0")
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
    implementation("com.intsoftdev:tflstatus:1.0.0")
    implementation("com.intsoftdev:tflstatus-ui:1.0.0")
}

// Usage example
@Composable
fun YourApp() {
    // From menu or navigation
    TFLStatusUI(
        onBackPressed = { /* handle back */ }
    )
}
```

## 🔍 Verification

After publishing, verify your libraries are available:

1. **Check Maven Central**:
    - Search for `com.intsoftdev` on [Maven Central](https://central.sonatype.com/)
    - Verify both `tflstatus` and `tflstatus-ui` are listed

2. **Test Integration**:

```kotlin
// Create a test project and add dependencies
dependencies {
    implementation("com.intsoftdev:tflstatus:1.0.0")
    implementation("com.intsoftdev:tflstatus-ui:1.0.0")
}
```

3. **Monitor Download Stats**:
    - Check download statistics on Maven Central
    - Monitor GitHub releases and issues

## 🚨 Troubleshooting

### Common Issues

1. **GPG Signing Fails**:

```bash
# Ensure GPG is properly configured
gpg --list-secret-keys
export GPG_TTY=$(tty)
```

2. **Namespace Verification**:
    - Ensure you own the `com.intsoftdev` namespace in Maven Central
    - Add DNS TXT record or GitHub repository verification

3. **Dependencies Not Found**:
    - Check if all dependencies are available on Maven Central
    - Ensure version compatibility

4. **Publication Fails**:

```bash
# Check staging repositories
./gradlew getSonatypeStagingProfiles
```

## 📋 Release Checklist

Before each release:

- [ ] Version bumped in both libraries
- [ ] Dependencies updated to latest stable versions
- [ ] All tests passing on Android and iOS
- [ ] Documentation updated
- [ ] CHANGELOG.md updated
- [ ] Git tag created: `git tag v1.0.0`
- [ ] GitHub release created with release notes
- [ ] Libraries published to Maven Central
- [ ] Integration tested in sample apps
- [ ] Documentation published (if applicable)

## 📈 Version Compatibility Matrix

| tflstatus-ui | tflstatus (core) | Compose Multiplatform | Notes |
|-------------|------------------|---------------------|-------|
| 1.0.0       | 1.0.0           | 1.8.1              | Initial release |
| 1.1.0       | 1.0.x           | 1.8.1              | UI enhancements |
| 2.0.0       | 2.0.0           | 1.9.0              | Breaking changes |

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/IntSoftDev/LondonTubeStatus/issues)
- **Discussions**: [GitHub Discussions](https://github.com/IntSoftDev/LondonTubeStatus/discussions)
- **Email**: az@intsoftdev.com

---

**Next Steps**: After publishing, create integration examples and update the main README with usage
instructions for both libraries.