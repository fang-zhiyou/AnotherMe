import SwiftUI
import WebKit

let WebCitiesCoordinate = [CityCoordinate().Chengdu, CityCoordinate().Xiamen, CityCoordinate().Shanghai, CityCoordinate().Beijing]

class NavigationState: NSObject, ObservableObject {
    @Published var currentURL : URL?
    @Published var webViews : [WKWebView] = []
    @Published var selectedWebView : WKWebView?
    
    @discardableResult func createNewWebView(withRequest request: URLRequest) -> WKWebView {
        let webView = WKWebView()
        webView.navigationDelegate = self
        webViews.append(webView)
        selectedWebView = webView
        webView.load(request)
        return webView
    }
}

extension NavigationState: WKNavigationDelegate {
    func webView(_ webView: WKWebView, didCommit navigation: WKNavigation!) {
        if webView == selectedWebView {
            self.currentURL = webView.url
        }
    }
}

struct WebView: UIViewRepresentable {
    @ObservedObject var navigationState: NavigationState
    
    func makeUIView(context: Context) -> UIView  {
        return UIView()
    }
    
    func updateUIView(_ uiView: UIView, context: Context) {
        guard let webView = navigationState.selectedWebView else {
            return
        }
        if webView != uiView.subviews.first {
            uiView.subviews.forEach { $0.removeFromSuperview() }
            
            webView.frame = CGRect(origin: .zero, size: uiView.bounds.size)
            uiView.addSubview(webView)
        }
    }
}

struct ThroughView: View {
    @ObservedObject var userData: UserData
    
    var body: some View {
        TabView {
            ForEach(0 ..< self.userData.selectedAnchors.count + 1, id: \.self) { i in
                if i == 0 {
                    SubWebView(inputURL: "https://www.baidu.com")
                        .tabItem {
                            Image(systemName: "\(i + 1).square")
                        }
                } else {
//                    SubWebView(inputURL: "https://m.ctrip.com/webapp/hotel/cityD104_28/?days=1&atime=20220221&geo=1&ulocation=30.757704017297133_103.92946288059046_&ulat=\(WebCitiesCoordinate[i - 1].latitude)&ulon=\(WebCitiesCoordinate[i - 1].longitude)&ucity=28")
//                        .tabItem {
//                            Image(systemName: "\(i + 1).square")
//                        }
                    SubWebView(inputURL: "https://www.hotwire.com/hotels/search?destination=\(WebCitiesCoordinate[i - 1].latitude)%2C\(WebCitiesCoordinate[i - 1].longitude)&startDate=2022-05-25&endDate=2022-05-26&rooms=1&adults=2&children=0")
                        .tabItem {
                            Image(systemName: "\(i + 1).square")
                        }
                }
            }
        }
        .onAppear {
            self.userData.updateAvalible = true
        }
    }
}

struct SubWebView: View {
    @StateObject var navigationState = NavigationState()
    
    @State var isCtrip: Bool = false
    
    @State private var updateWebView = Timer.publish(every: 0.01, on: .main, in: .common).autoconnect()
    @State var isUpdateWebView = false
    
    @State var isGoBackDisabled = true
    @State var isGoForwardDisabled = true
    
    @State var inputURL: String
    @State var editing: Bool = false
    @State var editingFlag: Bool = true
    
    @State var textFieldAlignment: TextAlignment = .center
    
    var body: some View {
        VStack {
            HStack {
                if !self.editing {
                    Button(action: {
                        navigationState.selectedWebView?.goBack()
                    }, label: {
                        Image(systemName: "arrow.backward.circle")
                            .font(.title2)
                    })
                    .disabled(isGoBackDisabled)
                    
                    Button(action: {
                        navigationState.selectedWebView?.goForward()
                    }, label: {
                        Image(systemName: "arrow.forward.circle")
                            .font(.title2)
                    })
                    .disabled(isGoForwardDisabled)
                }
                
                TextField("Enter URL",
                          text: $inputURL
                ) { editing in
                    self.editing = editing
                } onCommit: {
                    let absoluteURL = self.inputURL
                    _ = delay(0.3) {
                        navigationState.createNewWebView(withRequest: URLRequest(url: URL(string: absoluteURL)!))
                    }
                    
                }
                .multilineTextAlignment(self.textFieldAlignment)
                .textFieldStyle(.roundedBorder)
                .keyboardType(.URL)
                .submitLabel(.go)
                
                if !self.editing {
                    Image(systemName: "lock.shield")
                        .padding(.leading, -7.0)
                        
                    Button(action: {
                        navigationState.selectedWebView?.reload()
                    }, label: {
                        Image(systemName: "arrow.counterclockwise.circle")
                            .font(.title2)
                    })
                }
                
//                Button("check") {
//                    CheckIsCtrip(URL: navigationState.currentURL?.absoluteString ?? "")
//                }
            }
            .onReceive(updateWebView) { _ in
                isGoBackDisabled = !(navigationState.selectedWebView?.canGoBack ?? true)
                isGoForwardDisabled = !(navigationState.selectedWebView?.canGoForward ?? true)
                if !self.editing && isUpdateWebView {
                    self.inputURL = navigationState.currentURL?.host ?? ""
                    self.textFieldAlignment = .center
                    self.editingFlag = true
                }
                if self.editing && self.editingFlag {
                    self.inputURL = navigationState.currentURL?.absoluteString ?? ""
                    self.textFieldAlignment = .leading
                    self.editingFlag = false
                }
            }
            .padding([.leading, .bottom, .trailing])
            .animation(.default)
            
            WebView(navigationState: navigationState)
            .onAppear {
                if !self.isUpdateWebView {
                    _ = delay(0.5) {
                        navigationState.createNewWebView(withRequest: URLRequest(url: URL(string: self.inputURL) ?? URL(string: "https://apple.cn")!))
                        isUpdateWebView = true
                    }
                }
            }
        
        }
        
    }
    
    private func CheckIsCtrip(URL: String) {
        if URL.contains("https://m.ctrip.com") && URL.contains("ulat=") && URL.contains("ulon=") {
            print("ctrip 位置请求")
        } else {
            print("🈚️")
        }
    }
}

struct ThroughView_Previews: PreviewProvider {
    static var previews: some View {
        ThroughView(userData: UserData())
    }
}
