// ContentView.swift

import SwiftUI
import tflstatus_ui

struct ContentView: View {
    @State private var showTFLStatus = false
    @State private var showingMenu = false

    var body: some View {
        NavigationView {
            VStack(spacing: 20) {
                Text("Your iOS App")
                    .font(.largeTitle)
                    .padding()

                Button("Check TFL Status") {
                    showTFLStatus = true
                }
                .buttonStyle(.borderedProminent)

                Spacer()
            }
            .navigationTitle("Home")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Menu("Options") {
                        Button("TFL Tube Status") {
                            showTFLStatus = true
                        }

                        Button("Other Feature") {
                            // Other menu action
                        }
                    }
                    .foregroundColor(.primary)
                }
            }
        }
        .sheet(isPresented: $showTFLStatus) {
            TFLStatusView(isPresented: $showTFLStatus)
        }
    }
}

// TFLStatusView.swift - Wrapper for the KMP UI
struct TFLStatusView: UIViewControllerRepresentable {
    @Binding var isPresented: Bool

    func makeUIViewController(context: Context) -> UIViewController {
        let controller = TFLStatusViewController()
        controller.onBackPressed = {
            isPresented = false
        }
        return controller
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // No updates needed
    }
}

// TFLStatusViewController.swift - Bridge to KMP Compose

import UIKit
import tflstatus_ui

class TFLStatusViewController: UIViewController {
    var onBackPressed: (() -> Void)?

    override func viewDidLoad() {
        super.viewDidLoad()

        // Create the Compose UI
        let composeView = TFLStatusUIKt.TFLStatusUI(
            modifier: ModifierKt.fillMaxSize(),
            onBackPressed: { [weak self] in
                self?.onBackPressed?()
            }
        )

        // Add to view hierarchy
        let hostingController = ComposeUIViewController(rootView: composeView)
        addChild(hostingController)
        view.addSubview(hostingController.view)
        hostingController.view.frame = view.bounds
        hostingController.view.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        hostingController.didMove(toParent: self)
    }
}

// Alternative: Navigation-based approach
struct NavigationBasedApp: View {
    var body: some View {
        NavigationView {
            List {
                NavigationLink("Home", destination: HomeView())
                NavigationLink("TFL Status", destination: TFLStatusViewKMP())
                NavigationLink("Settings", destination: SettingsView())
            }
            .navigationTitle("Menu")
        }
    }
}

struct TFLStatusViewKMP: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        let controller = UIViewController()

        // Create KMP Compose UI
        let composeView = TFLStatusUIKt.TFLStatusUI(
            modifier: ModifierKt.fillMaxSize(),
            onBackPressed: nil // Navigation handles back button
        )

        let hostingController = ComposeUIViewController(rootView: composeView)
        controller.addChild(hostingController)
        controller.view.addSubview(hostingController.view)
        hostingController.view.frame = controller.view.bounds
        hostingController.view.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        hostingController.didMove(toParent: controller)

        return controller
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // No updates needed
    }
}