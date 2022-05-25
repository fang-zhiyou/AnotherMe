import SwiftUI
import SafariServices

struct ContentView: View {
    @StateObject var userData = UserData()
    
    var body: some View {
        TabView {
            HomeView(userData: userData)
                .tabItem {
                    Image(systemName: "house")
                    Text("Home")
                }
            
            POIView(userData: userData)
                .tabItem {
                    Image(systemName: "location.circle")
                    Text("Mapping")
                }
            
            ThroughView(userData: userData)
                .tabItem {
                    Image(systemName: "squareshape.split.2x2")
                    Text("Browser")
                }
            
            SettingsView(userData: userData)
                .tabItem {
                    Image(systemName: "gearshape")
                    Text("Settings")
                }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
