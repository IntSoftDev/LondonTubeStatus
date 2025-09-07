// In your MainActivity.kt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            YourAppTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            onTFLStatusClick = {
                                navController.navigate("tfl_status")
                            }
                        )
                    }

                    composable("tfl_status") {
                        TFLStatusUI(
                            onBackPressed = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

// Fragment-based integration (for existing Fragment apps)
class TFLServicesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                // Apply your app's theme
                YourAppTheme {
                    TFLStatusUI(
                        onBackPressed = {
                            // Handle back navigation in Fragment
                            findNavController().popBackStack()
                            // Or if using FragmentManager:
                            // parentFragmentManager.popBackStack()
                        },
                        showTitle = false // Hide title since Fragment manages toolbar
                    )
                }
            }
        }
    }
}

// Alternative: If you want to keep your existing Fragment structure but use the KMP UI
class TFLServicesFragment : NRFragment() {

    private var _composeView: ComposeView? = null
    private val composeView get() = _composeView!!

    override fun refresh(forceRefresh: Boolean) {
        // The KMP library handles refresh automatically
        // No need to manually trigger refresh
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        with(requireActivity() as MainActivity) {
            setToolbarTitle(R.string.screen_tfl)
            displayNRLogo(false)
        }

        _composeView = ComposeView(requireContext()).apply {
            setContent {
                // Make sure to initialize Koin with TFL Status modules in your Application class
                TFLStatusUI(
                    onBackPressed = {
                        // Handle back navigation
                        parentFragmentManager.popBackStack()
                    },
                    showTitle = false, // Don't show title since we're setting it in MainActivity
                    title = "TFL Service status" // Custom title (only used if showTitle = true)
                )
            }
        }

        return composeView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _composeView = null
    }
}

// Don't forget to add this to your Application class:
class YourApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@YourApplication)
            modules(
                // Your existing modules
                yourAppModules,
                // TFL Status KMP modules (add this line)
                TFLStatusDiModule.module
            )
        }
    }
}

// In your HomeScreen.kt (with menu)
@Composable
fun HomeScreen(
    onTFLStatusClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your App") },
                actions = {
                    var showMenu by remember { mutableStateOf(false) }

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
                                onTFLStatusClick()
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Train, contentDescription = null)
                            }
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
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onTFLStatusClick) {
                Text("Check TFL Status")
            }
        }
    }
}

// Alternative: If you want to show it as a dialog/bottom sheet
@Composable
fun HomeScreenWithDialog() {
    var showTFLStatus by remember { mutableStateOf(false) }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Button(onClick = { showTFLStatus = true }) {
                Text("Show TFL Status")
            }
        }
    }

    if (showTFLStatus) {
        Dialog(
            onDismissRequest = { showTFLStatus = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                TFLStatusUI(
                    onBackPressed = { showTFLStatus = false },
                    showTitle = true,
                    title = "TFL Service status" // Custom title matching screenshot
                )
            }
        }
    }
}

// Example with custom title and showing title bar
class StandaloneTFLFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                YourAppTheme {
                    TFLStatusUI(
                        onBackPressed = {
                            findNavController().popBackStack()
                        },
                        showTitle = true,
                        title = "TFL Service status" // Custom title matching screenshot
                    )
                }
            }
        }
    }
}