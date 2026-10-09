//
//  WebView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 27/09/24.
//

import SwiftUI
import WebKit

struct WebPageView: UIViewRepresentable {
    let urlString: String

    func makeUIView(context: Context) -> WKWebView {
        let webView = WKWebView()
        webView.navigationDelegate = context.coordinator
        loadURL(in: webView)
        return webView
    }

    func updateUIView(_ uiView: WKWebView, context: Context) {
        // Only load if not already loaded
        if uiView.url == nil {
            loadURL(in: uiView)
        }
    }

    private func loadURL(in webView: WKWebView) {
        guard let url = URL(string: urlString) else {
            return
        }
        let request = URLRequest(url: url)
        webView.load(request)
    }

    func makeCoordinator() -> Coordinator {
        Coordinator()
    }

    class Coordinator: NSObject, WKNavigationDelegate {
        func webView(_ webView: WKWebView, didFailProvisionalNavigation navigation: WKNavigation!, withError error: Error) {
            AppLog.debug("WebView failed to load: \(error.localizedDescription)")
        }

        func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
            AppLog.debug("WebView navigation failed: \(error.localizedDescription)")
        }
    }
}

/// Owns the tutorial video web view so the page can start loading before the
/// setup flow reaches `VideoTutorialView`.
@MainActor
final class YoutubeWebViewPreloader {

    static let shared = YoutubeWebViewPreloader()

    private var webView: WKWebView?
    private var loadedURL: URL?

    private init() {}

    /// Starts loading `url` in the background if it is not already loaded or loading.
    func preload(_ url: URL) {
        _ = webView(for: url)
    }

    /// Returns the preloaded web view for `url`, creating and loading one when needed.
    func webView(for url: URL) -> WKWebView {
        if let webView, loadedURL == url {
            // Retry when an earlier preload failed (e.g. no network on the welcome screen).
            if webView.url == nil && !webView.isLoading {
                webView.load(URLRequest(url: url))
            }
            return webView
        }

        let configuration = WKWebViewConfiguration()
        // Keep the preloaded page silent until the user taps play.
        configuration.mediaTypesRequiringUserActionForPlayback = .all

        let webView = WKWebView(frame: .zero, configuration: configuration)
        webView.load(URLRequest(url: url))
        self.webView = webView
        loadedURL = url
        return webView
    }

    /// Drops the web view once the tutorial is no longer needed.
    func release() {
        webView?.stopLoading()
        webView = nil
        loadedURL = nil
    }
}

struct YoutubeWebView: UIViewRepresentable {

    let url: URL

    func makeUIView(context: Context) -> WKWebView {
        YoutubeWebViewPreloader.shared.webView(for: url)
    }

    func updateUIView(_ uiView: WKWebView, context: Context) {
        // No update required for now
    }
}

//#Preview {
//    YoutubeWebView(url: URL(string: "") ?? URL(string: ""))
//}
