# London Tube Status - Kotlin Multiplatform Library

[![Maven Central](https://img.shields.io/maven-central/v/com.intsoftdev/tflstatus)](https://central.sonatype.com/search?q=com.intsoftdev.tflstatus)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/badge/kotlin-multiplatform-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose-Multiplatform-blue.svg)](https://www.jetbrains.com/lp/compose-multiplatform/)

A Kotlin Multiplatform library providing real-time London Underground tube line status information
with ready-to-use UI components and business logic for Android and iOS applications.

## Features

- **Real-time TFL Data**: Fetches live tube line status from Transport for London API
- **Kotlin Multiplatform**: Shared business logic for Android and iOS
- **Compose Multiplatform UI**: Beautiful, native-feeling UI components
- **Authentic TFL Branding**: Official TFL colors and styling
- **Two Integration Options**: Core library only or complete UI solution
- **Easy Integration**: Drop into existing navigation patterns
- **Offline Handling**: Graceful error states and retry mechanisms

## Architecture

The project consists of three main modules:

### Core Library (`com.intsoftdev:tflstatus`)

- Business logic and data layer
- TFL API integration with Ktor
- ViewModels and use cases
- Koin dependency injection setup
- Custom UI implementation support

### UI Library (`com.intsoftdev:tflstatus-ui`)

- Pre-built Compose Multiplatform UI components
- Authentic TFL branding and colors
- Ready-to-use screens and components
- Includes core library as dependency

### Sample App (`/composeApp`)

- Demonstrates library integration
- Example theming and navigation
- Development and testing environment

## Quick Start

### Android Integration

Add to your `build.gradle.kts`:

```kotlin
dependencies {
    // Option 1: Complete UI solution (recommended)
    implementation("com.intsoftdev:tflstatus-ui:1.0.0")
    
    // Option 2: Core library only (custom UI)
    // implementation("com.intsoftdev:tflstatus:1.0.0")
}
```

Basic usage with navigation:

```kotlin
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(onNavigateToTFL = { 
                navController.navigate("tfl_status") 
            })
        }
        
        composable("tfl_status") {
            TFLStatusUI(onBackPressed = { 
                navController.popBackStack() 
            })
        }
    }
}
```

### iOS Integration

Add the framework to your iOS project and use in SwiftUI:

```swift
import SwiftUI
import tflstatus_ui

struct ContentView: View {
    @State private var showTFLStatus = false
    
    var body: some View {
        NavigationView {
            VStack {
                Button("London Tube Status") {
                    showTFLStatus = true
                }
            }
            .fullScreenCover(isPresented: $showTFLStatus) {
                TFLStatusViewWrapper(isPresented: $showTFLStatus)
            }
        }
    }
}
```

## Development Setup

### Prerequisites

- **Android Studio** Iguana+ with Kotlin Multiplatform plugin
- **Xcode 15+** (for iOS development)
- **Java 11+**
- **TFL API Credentials** (free from [TFL Developer Portal](https://api.tfl.gov.uk/))

### Local Development

1. **Clone the repository:**
   ```bash
   git clone https://github.com/IntSoftDev/LondonTubeStatus.git
   cd LondonTubeStatus
   ```

2. **Setup TFL API credentials:**
   Create `local.properties` file:
   ```properties
   tfl.app.id=your_tfl_app_id
   tfl.app.key=your_tfl_app_key
   ```

3. **Configure local development:**
   In `gradle.properties`, set:
   ```properties
   importLocalKmp=true
   ```

4. **Run the sample app:**
   ```bash
   ./gradlew :composeApp:assembleDebug
   ```

## Fast Development Commands

For faster builds during development:

```bash
# Fast Android-only build (skips iOS frameworks)
./gradlew compileDebugKotlinAndroid assembleDebug -x podDebugFramework

# Quick tests (Android + common)
./gradlew testDebugUnitTest -x compileKotlinIos*

# Fast lint check
./gradlew lintDebug -x podDebugFramework

# Build libraries only (Android)
./gradlew :tflstatus:compileKotlinAndroid :tflstatus-ui:compileKotlinAndroid

# Full build (when you need iOS)
./gradlew build

# Clean build cache (if builds are acting strange)
./gradlew clean
```

## Project Structure

```
LondonTubeStatus/
├── composeApp/                 # Sample Android/iOS app
│   └── src/
│       ├── androidMain/        # Android-specific code
│       ├── commonMain/         # Shared Compose UI
│       └── iosMain/            # iOS-specific code
├── tflstatus/                  # Core business logic library
│   └── src/
│       ├── commonMain/         # Shared business logic
│       ├── androidMain/        # Android HTTP client
│       └── iosMain/            # iOS HTTP client
├── tflstatus-ui/              # UI components library
│   └── src/
│       ├── commonMain/         # Compose Multiplatform UI
│       └── ...
├── iosApp/                    # iOS app wrapper
├── INTEGRATION_EXAMPLES.md    # Comprehensive integration guide
└── PUBLISHING_GUIDE.md        # Library publishing documentation
```

## Documentation

- **[Integration Examples](INTEGRATION_EXAMPLES.md)** - Comprehensive integration guide with code
  samples
- **[Publishing Guide](PUBLISHING_GUIDE.md)** - How to publish updates to Maven Central
- **[TFL API Documentation](https://api.tfl.gov.uk/)** - Official TFL API reference

## Theming & Customization

The UI library uses authentic TFL branding but can be customized:

```kotlin
@Composable
fun CustomThemedTFL() {
    MaterialTheme(
        colorScheme = yourCustomColorScheme,
        typography = yourCustomTypography
    ) {
        TFLStatusUI(onBackPressed = { /* handle back */ })
    }
}
```

## Advanced Usage

### Custom UI with Core Library

```kotlin
@Composable
fun CustomTFLScreen(viewModel: TubeStatusViewModel = koinInject()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        viewModel.getLineStatuses("victoria,central,northern")
    }
    
    when (val state = uiState) {
        is TubeStatusUiState.Loading -> LoadingScreen()
        is TubeStatusUiState.Success -> CustomLineList(state.lineStatuses)
        is TubeStatusUiState.Error -> ErrorScreen(state.message)
    }
}
```

### Dependency Injection

```kotlin
class YourApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidContext(this@YourApplication)
            modules(
                yourAppModule,
                TFLStatusDiModule.module // Included automatically with UI library
            )
        }
    }
}
```

## Publishing

Libraries are published to Maven Central:

- **Core**: `com.intsoftdev:tflstatus`
- **UI**: `com.intsoftdev:tflstatus-ui`

See [PUBLISHING_GUIDE.md](PUBLISHING_GUIDE.md) for detailed publishing instructions.

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- **Transport for London** for providing the public API
- **JetBrains** for Kotlin Multiplatform and Compose Multiplatform
- **Koin** for dependency injection framework
- **Ktor** for HTTP client implementation

## Support

- **Issues**: [GitHub Issues](https://github.com/IntSoftDev/LondonTubeStatus/issues)
- **Documentation**: [Integration Examples](INTEGRATION_EXAMPLES.md)
- **Email**: az@intsoftdev.com

---
