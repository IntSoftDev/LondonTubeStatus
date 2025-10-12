# London Tube Status - Kotlin Multiplatform Library

[![Maven Central](https://img.shields.io/maven-central/v/com.intsoftdev/tflstatus-core)](https://central.sonatype.com/search?q=com.intsoftdev.tflstatus-core)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/badge/kotlin-multiplatform-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose-Multiplatform-blue.svg)](https://www.jetbrains.com/lp/compose-multiplatform/)

A Kotlin Multiplatform library providing real-time London Underground tube line status information
with ready-to-use UI components and business logic for Android and iOS applications.

## Features

- **Real-time TFL Data**: Fetches live tube line status from Transport for London API
- **Kotlin Multiplatform**: Shared business logic for Android and iOS
- **Compose Multiplatform UI**: Native-feeling UI components
- **Two Integration Options**: Core library only or complete UI solution
- **Easy Integration**: Drop into existing navigation patterns
- **Offline Handling**: Graceful error states and retry mechanisms

## Architecture

The project consists of three main modules:

### Core Library (`com.intsoftdev:tflstatus-core`)

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

## Development Setup

### Prerequisites

- **Android Studio** Iguana+ with Kotlin Multiplatform plugin
- **Xcode 15+** (for iOS development)
- **Java 11+**
- **TFL API Credentials** (free from [TFL Developer Portal](https://api.tfl.gov.uk/)) - Not required
  to run the app, but recommended to avoid throttling and rate limitations from TFL servers

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

## Development Commands

```bash
# Android-only build
./gradlew assembleDebug

# Tests 
./gradlew allTests

# Lint check
./gradlew ktlintCheck

# Full build 
./gradlew build

# Clean build cache
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
├── tflstatus-core/             # Core business logic library
│   └── src/
│       ├── commonMain/         # Shared business logic
│       ├── androidMain/        # Android HTTP client
│       └── iosMain/            # iOS HTTP client
├── tflstatus-ui/              # UI components library
│   └── src/
│       ├── commonMain/         # Compose Multiplatform UI
│       └── ...
├── iosApp/                    # iOS app wrapper
└── PUBLISHING_GUIDE.md        # Library publishing documentation
```

## Documentation

- **[Publishing Guide](PUBLISHING_GUIDE.md)** - How to publish updates to Maven Central
- **[TFL API Documentation](https://api.tfl.gov.uk/)** - Official TFL API reference
- **[Android Integration](#android-integration)** - How to integrate the library in your Android app
- **[iOS Integration](#ios-integration)** - How to integrate the library in your SwiftUI app
  
## Quick Start

### Android Integration

To use the pre-built artifacts:

In `gradle.properties`, set:
   ```properties
   importLocalKmp=false
   ```
Add to your `build.gradle.kts`:

```kotlin
dependencies {
    // Option 1: Complete UI solution (recommended)
    implementation("com.intsoftdev:tflstatus-core:<version>")
    implementation("com.intsoftdev:tflstatus-ui:<version>")
    
    // Option 2: Core library only (custom UI)
    implementation("com.intsoftdev:tflstatus-core:<version>")
}
```

#### Initialisation

The TFL SDK can be initialised in your Application class with various configuration options:

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Option 1 - Basic initialisation without using API keys or Koin
        initTflSDK(context = this)

        // With custom API configuration
        val apiConfig = TflApiConfig(
            appId = "your-app-id",
            appKey = "your-api-key"
        )
        // Option 2 - if App does not use Koin
        initTflSDK(context = this, apiConfig = apiConfig)

        // Option 3 - with Koin
        val koinApp = startKoin { /* your modules */ }
        initTflSDK(context = this, koinApplication = koinApp, apiConfig = apiConfig)
    }
}
```

#### Basic usage with navigation:

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
           tflStatusUI(onBackPressed = {
               navController.popBackStack() 
            })
        }
    }
}
```

#### Theming & Customization

The UI library uses authentic TFL branding but can be customised:

```kotlin
@Composable
fun CustomThemedTFL() {
    MaterialTheme(
        colorScheme = yourCustomColorScheme,
        typography = yourCustomTypography
    ) {
         tflStatusUI(onBackPressed = { /* handle back */ })
    }
}
```

### iOS Integration

#### CocoaPods Setup

Add the TFL Status library to your iOS project using CocoaPods:

**Local Development**

```ruby
# Podfile
platform :ios, '17.0'

install! 'cocoapods', :deterministic_uuids => false

target 'YourApp' do
    use_frameworks!
    
    # Local paths - adjust to match your project structure
    pod 'tflstatus_core', :path => '../tflstatus-core/'
    pod 'tflstatus_ui', :path => '../tflstatus-ui/'
end
```

Then run:
```bash
cd ios
pod install
```

```swift
// From iOS Swift code:
let viewController = TFLStatusBridgeKt.createTFLStatusViewController(
    showBackButton: true,
    onBackPressed: {
        // Handle back button press
    },
    enableLogging: true,
    apiConfig: TflApiConfig()
)
```

#### SwiftUI Integration

Add the framework to your iOS project and use in SwiftUI:

```swift
import SwiftUI
import tflstatus_ui
import tflstatus_core

struct TFLStatusComposeView: UIViewControllerRepresentable {
   var showBackButton: Bool = true
   var onBackPressed: (() -> Void)?

   func makeUIViewController(context: Context) -> UIViewController {
      return TFLStatusBridgeKt.createTFLStatusViewController(
              showBackButton: showBackButton,
              onBackPressed: onBackPressed ?? {
              },
              enableLogging: true,
              apiConfig: TflApiConfig(
                      appId: "your-app-id",
                      appKey: "your-api-key"
              )
      )
    }

   func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
      // No-op: Compose content is self-updating
   }
}

struct TFLStatusScreen: View {
   @Environment(\.dismiss) private var dismiss

   var body: some View {
      NavigationView {
         ZStack {
            Color.black
                .navigationBarTitleDisplayMode(.inline)
                .edgesIgnoringSafeArea(.all)

            // Pipe SwiftUI's dismiss into KMP's onBackPressed
            TFLStatusComposeView(
                    showBackButton: true,
                    onBackPressed: { dismiss() }
            )
         }
         .toolbar {
            ToolbarItem(placement: .principal) {
               Text("TFL Status")
                   .foregroundColor(.white)
                   .font(.headline)
            }
         }
         .navigationBarBackButtonHidden(true)
         .navigationBarItems(leading: Button(action: {
            dismiss()
         }) {
            Image(systemName: "arrow.left")
                .foregroundColor(.white)
            })
        }
        .preferredColorScheme(.dark)
    }
}

// Usage in your main ContentView
struct ContentView: View {
    @State private var showTFLStatus = false
    
    var body: some View {
        NavigationView {
            VStack {
                Button("London Tube Status") {
                    showTFLStatus = true
                }
                .buttonStyle(.borderedProminent)
            }
            .navigationTitle("Your App")
            .sheet(isPresented: $showTFLStatus) {
               TFLStatusScreen()
            }
        }
    }
}
```

## Custom UI with Core Library

```kotlin
@Composable
fun CustomTFLScreen(viewModel: TubeStatusViewModel = koinInject()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        viewModel.getLineStatuses("victoria,central,northern")
    }
    
    when (val state = uiState) {
        is TubeStatusUiState.Loading -> LoadingScreen()
        is TubeStatusUiState.Success -> CustomLineList(state.tubeLines)
        is TubeStatusUiState.Error -> ErrorScreen(state.message)
    }
}
```

## Screenshots and usage in Live apps

<table>
  <tr>
    <td style="text-align: center;">
      <h3>Android</h3>
      <p>Also used in <a href="https://play.google.com/store/apps/details?id=com.intsoftdev.nationalrail">Play Store app</a></p>
      <img width="320" height="640" alt="AndroidScreenshot" src="https://github.com/user-attachments/assets/9d922236-0967-473f-8c67-8582cbe446f1" />
    </td>
    <td style="text-align: center;">
      <h3>iOS</h3>
      <p>Also used in <a href="https://apps.apple.com/us/app/on-rails-train-times-widget/id6464424408">Apple App Store</a></p>
      <img width="320" height="640" alt="iOS Screenshot" src="https://github.com/user-attachments/assets/fb557165-669b-4f74-ade0-eda4a0b691f1" />
    </td>
  </tr>
</table>

## Publishing

Libraries are published to Maven Central:

- **Core**: `com.intsoftdev:tflstatus-core`
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

## Support

- **Issues**: [GitHub Issues](https://github.com/IntSoftDev/LondonTubeStatus/issues)
- **Documentation**: [ReadMe](Readme.md)
- **Email**: az@intsoftdev.com

---
