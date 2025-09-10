# TFL Status KMP Library Integration Examples

This document provides comprehensive examples of how to integrate the TFL Status KMP libraries into
your Android and iOS applications.

## 📚 Library Overview

### Core Library (`com.intsoftdev:tflstatus`)

- Business logic and data layer only
- Custom UI implementation required
- Smaller footprint for custom solutions

### UI Library (`com.intsoftdev:tflstatus-ui`)

- Complete pre-built UI components
- Authentic TFL branding and colors
- Ready-to-use in any menu system
- Depends on core library

## 📱 Android Integration

### Option 1: Complete UI Solution (Recommended)

#### Dependencies

```kotlin
// app/build.gradle.kts
dependencies {
    implementation("com.intsoftdev:tflstatus:1.0.0")
    implementation("com.intsoftdev:tflstatus-ui:1.0.0")
    
    // Required Compose dependencies (if not already added)
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.navigation:navigation-compose:2.8.5")
}
```

#### Navigation Integration

```kotlin
// MainActivity.kt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            YourAppTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToTFL = {
                    navController.navigate("tfl_status")
                }
            )
        }
        
        composable("tfl_status") {
            // Complete TFL Status screen with back button
            TFLStatusUI(
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
    }
}
```

#### Menu Integration

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToTFL: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your App") },
                actions = {
                    // Overflow menu
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("TFL Tube Status") },
                            onClick = {
                                showMenu = false
                                onNavigateToTFL()
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_train),
                                    contentDescription = null
                                )
                            }
                        )
                        
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = { /* Other menu item */ }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // Your app content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Direct access button
            Card(
                onClick = onNavigateToTFL,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_train),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column {
                        Text(
                            text = "London Tube Status",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Check real-time tube line status",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
```

#### Bottom Sheet Integration

```kotlin
@Composable
fun HomeWithBottomSheet() {
    val bottomSheetState = rememberModalBottomSheetState()
    var showTFLStatus by remember { mutableStateOf(false) }
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showTFLStatus = true }
            ) {
                Icon(Icons.Default.Train, contentDescription = "TFL Status")
            }
        }
    ) { paddingValues ->
        // Your app content
        YourAppContent(modifier = Modifier.padding(paddingValues))
    }
    
    if (showTFLStatus) {
        ModalBottomSheet(
            onDismissRequest = { showTFLStatus = false },
            sheetState = bottomSheetState,
            modifier = Modifier.fillMaxHeight()
        ) {
            TFLStatusUI(
                onBackPressed = { showTFLStatus = false }
            )
        }
    }
}
```

### Option 2: Core Library with Custom UI

#### Dependencies

```kotlin
dependencies {
    implementation("com.intsoftdev:tflstatus:1.0.0")
    // No UI library - build your own interface
}
```

#### Custom Implementation

```kotlin
@Composable
fun CustomTFLStatusScreen(
    viewModel: TubeStatusViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        viewModel.getLineStatuses("victoria,central,northern,piccadilly")
    }
    
    when (val state = uiState) {
        is TubeStatusUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        
        is TubeStatusUiState.Success -> {
            LazyColumn {
                items(state.lineStatuses) { line ->
                    // Your custom UI design
                    CustomLineItem(line = line)
                }
            }
        }
        
        is TubeStatusUiState.Error -> {
            ErrorMessage(
                message = state.message,
                onRetry = { 
                    viewModel.getLineStatuses("victoria,central,northern,piccadilly")
                }
            )
        }
    }
}

@Composable
private fun CustomLineItem(line: TFLStatusResponseItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = line.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            if (line.lineStatuses.isNotEmpty()) {
                Text(
                    text = line.lineStatuses.first().statusSeverityDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    color = getCustomStatusColor(line.lineStatuses.first().statusSeverity)
                )
            }
        }
    }
}
```

## 🍎 iOS Integration

### Option 1: SwiftUI with Complete UI

#### Add Dependencies

```swift
// In your iOS project, add the KMP framework
import tflstatus_ui
import tflstatus
```

#### SwiftUI Integration

```swift
// ContentView.swift
import SwiftUI

struct ContentView: View {
    @State private var showTFLStatus = false
    
    var body: some View {
        NavigationView {
            List {
                Section("Transport") {
                    Button(action: {
                        showTFLStatus = true
                    }) {
                        HStack {
                            Image(systemName: "tram")
                                .foregroundColor(.blue)
                            
                            VStack(alignment: .leading) {
                                Text("London Tube Status")
                                    .font(.headline)
                                
                                Text("Check real-time status")
                                    .font(.caption)
                                    .foregroundColor(.secondary)
                            }
                            
                            Spacer()
                            
                            Image(systemName: "chevron.right")
                                .foregroundColor(.secondary)
                        }
                    }
                    .foregroundColor(.primary)
                }
                
                Section("Other Features") {
                    // Your other menu items
                }
            }
            .navigationTitle("Your App")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Menu("Options") {
                        Button("TFL Status") {
                            showTFLStatus = true
                        }
                        
                        Button("Settings") {
                            // Settings action
                        }
                    }
                }
            }
        }
        .fullScreenCover(isPresented: $showTFLStatus) {
            TFLStatusViewWrapper(isPresented: $showTFLStatus)
        }
    }
}

// TFLStatusViewWrapper.swift
struct TFLStatusViewWrapper: UIViewControllerRepresentable {
    @Binding var isPresented: Bool
    
    func makeUIViewController(context: Context) -> UIViewController {
        let hostingController = TFLStatusHostingController()
        
        // Configure the KMP Compose UI
        let composeView = TFLStatusUIKt.TFLStatusUI(
            modifier: ModifierKt.fillMaxSize(),
            onBackPressed: { [weak hostingController] in
                DispatchQueue.main.async {
                    self.isPresented = false
                }
            }
        )
        
        hostingController.setComposeView(composeView)
        return hostingController
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // Updates if needed
    }
}

// TFLStatusHostingController.swift
import UIKit
import tflstatus_ui

class TFLStatusHostingController: UIViewController {
    private var composeViewController: UIViewController?
    
    func setComposeView(_ composeView: Any) {
        // Create hosting controller for Compose content
        let hostingController = ComposeUIViewController()
        hostingController.setContent(composeView)
        
        addChild(hostingController)
        view.addSubview(hostingController.view)
        
        hostingController.view.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            hostingController.view.topAnchor.constraint(equalTo: view.topAnchor),
            hostingController.view.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            hostingController.view.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            hostingController.view.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])
        
        hostingController.didMove(toParent: self)
        composeViewController = hostingController
    }
}
```

#### Navigation-Based Integration

```swift
struct NavigationApp: View {
    var body: some View {
        NavigationView {
            List {
                NavigationLink("Home", destination: HomeView())
                
                NavigationLink("TFL Status") {
                    TFLStatusNavigationView()
                }
                
                NavigationLink("Settings", destination: SettingsView())
            }
            .navigationTitle("Menu")
        }
    }
}

struct TFLStatusNavigationView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        let controller = UIViewController()
        
        // Create KMP UI without back button (navigation handles it)
        let composeView = TFLStatusUIKt.TFLStatusScreen(
            modifier: ModifierKt.fillMaxSize(),
            onBackPressed: nil
        )
        
        let hostingController = ComposeUIViewController()
        hostingController.setContent(composeView)
        
        controller.addChild(hostingController)
        controller.view.addSubview(hostingController.view)
        hostingController.view.frame = controller.view.bounds
        hostingController.didMove(toParent: controller)
        
        return controller
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
```

### Option 2: Core Library with Custom SwiftUI

```swift
// Custom SwiftUI implementation using just the core library
import SwiftUI
import tflstatus

struct CustomTFLStatusView: View {
    @StateObject private var viewModel = TFLStatusViewModel()
    @State private var tubeLines: [TFLStatusResponseItem] = []
    @State private var isLoading = false
    @State private var error: String?
    
    var body: some View {
        NavigationView {
            Group {
                if isLoading {
                    ProgressView("Loading tube status...")
                } else if let error = error {
                    VStack {
                        Text("Error: \(error)")
                            .foregroundColor(.red)
                        
                        Button("Retry") {
                            loadTubeStatus()
                        }
                    }
                } else {
                    List(tubeLines, id: \.id) { line in
                        TubeLineRow(line: line)
                    }
                }
            }
            .navigationTitle("TFL Status")
            .onAppear {
                loadTubeStatus()
            }
        }
    }
    
    private func loadTubeStatus() {
        isLoading = true
        error = nil
        
        // Use the KMP ViewModel
        viewModel.getLineStatuses("victoria,central,northern,piccadilly")
        
        // Observe state changes
        // Implementation depends on how you bridge StateFlow to SwiftUI
    }
}

struct TubeLineRow: View {
    let line: TFLStatusResponseItem
    
    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(line.name)
                .font(.headline)
            
            if !line.lineStatuses.isEmpty {
                Text(line.lineStatuses[0].statusSeverityDescription)
                    .font(.subheadline)
                    .foregroundColor(getStatusColor(line.lineStatuses[0].statusSeverity))
            }
        }
        .padding(.vertical, 4)
    }
    
    private func getStatusColor(_ severity: Int32) -> Color {
        switch severity {
        case 10: return .green  // Good service
        case 20: return .orange // Minor delays
        case 30: return .red    // Severe delays
        default: return .primary
        }
    }
}
```

## 🔧 Advanced Integration Patterns

### Dependency Injection Setup

```kotlin
// Android - In your Application class
class YourApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidContext(this@YourApplication)
            modules(
                // Your app modules
                yourAppModule,
                // TFL Status modules (automatically included with UI library)
                TFLStatusDiModule.module
            )
        }
    }
}
```

### Custom Theming

```kotlin
// Android - Custom theme integration
@Composable
fun YourAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = yourCustomColorScheme,
        typography = yourCustomTypography
    ) {
        content()
    }
}

// Usage with TFL Status UI
@Composable
fun ThemedTFLStatus() {
    YourAppTheme {
        TFLStatusScreenContent(
            onBackPressed = { /* handle back */ }
        )
    }
}
```

### Error Handling and Retry Logic

```kotlin
@Composable  
fun TFLStatusWithCustomErrorHandling() {
    val viewModel: TubeStatusViewModel = koinInject()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var retryCount by remember { mutableStateOf(0) }
    
    LaunchedEffect(retryCount) {
        viewModel.getLineStatuses(TFL_LINE_IDS)
    }
    
    when (val state = uiState) {
        is TubeStatusUiState.Error -> {
            CustomErrorScreen(
                error = state.message,
                onRetry = { 
                    retryCount++
                },
                onCancel = { /* handle cancel */ }
            )
        }
        else -> {
            TFLStatusScreenContent()
        }
    }
}
```

## 📊 Performance Considerations

### Lazy Loading

```kotlin
@Composable
fun LazyTFLStatusScreen() {
    var isLoaded by remember { mutableStateOf(false) }
    
    if (isLoaded) {
        TFLStatusUI()
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Tap to load TFL Status")
            Button(onClick = { isLoaded = true }) {
                Text("Load")
            }
        }
    }
}
```

### Memory Management

```swift
// iOS - Proper memory management
class TFLStatusManager: ObservableObject {
    private var viewModel: TubeStatusViewModel?
    
    func initializeViewModel() {
        guard viewModel == nil else { return }
        
        // Initialize only when needed
        viewModel = TubeStatusViewModel(
            getTFLStatusUseCase: // inject dependencies
        )
    }
    
    func cleanup() {
        viewModel = nil
    }
}
```

This comprehensive integration guide should help developers easily incorporate your TFL Status
libraries into their existing Android and iOS applications with various UI patterns and integration
approaches.