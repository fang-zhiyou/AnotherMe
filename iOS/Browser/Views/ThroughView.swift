//import SwiftUI
//import SafariServices
//import WebKit
//
//let WebCitiesCoordinate = [CityCoordinate().Chengdu, CityCoordinate().Xiamen, CityCoordinate().Shanghai, CityCoordinate().Beijing]
//
//struct ThroughView: View {
//    @ObservedObject var userData: UserData
//    @State private var showWindows = true
//    
//    var body: some View {
//        VStack {
//            Button(action: {
//                showWindows.toggle()
//            }, label: {
//                HStack {
//                    Image(systemName: "arrow.clockwise")
//                    
//                    Text("Refresh Page")
//                }
//                .padding(5)
//                .background(.blue)
//                .foregroundColor(.white)
//                .cornerRadius(5)
//            })
//            if showWindows == true {
//                ThroughSubViews(selectedCityNum: userData.selectedCityNum)
//            } else {
//                ThroughSubViews(selectedCityNum: userData.selectedCityNum)
//            }
//        }
//    }
//}
//
//struct NaiveSafariView: UIViewControllerRepresentable {
//    var url: URL
//    func makeUIViewController(context: Context) -> SFSafariViewController {
//        SFSafariViewController(url: url)
//    }
//    func updateUIViewController(_ uiViewController: SFSafariViewController, context: Context) {
//    }
//}
//
//struct ThroughSubViews: View {
//    var selectedCityNum: Int
//    
//    var locationManager = LocationManager()
//    
//    var userLatitude: Double {
//        return locationManager.lastLocation?.coordinate.latitude ?? 0
//    }
//    var userLongitude: Double {
//        return locationManager.lastLocation?.coordinate.longitude ?? 0
//    }
//    
//    var body: some View {
//        TabView() {
////            ForEach(0 ..< selectedCityNum, id: \.self) { i in
////                NaiveSafariView(url: URL(string: "https://m.ctrip.com/webapp/hotel/cityD104_28/?days=1&atime=20220221&geo=1&ulocation=30.757704017297133_103.92946288059046_&ulat=\(arc4random_uniform(15) + 26)&ulon=\(arc4random_uniform(32) + 87)&ucity=28")!)
////                    .tabItem {
////                        Image(systemName: "\(i + 1).square")
////                    }
////            }
//            ForEach(0 ..< selectedCityNum, id: \.self) { i in
//                if i == 0 {
//                    NaiveSafariView(url: URL(string:"https://m.ctrip.com/webapp/hotel/cityD104_28/?days=1&atime=20220221&geo=1&ulocation=30.757704017297133_103.92946288059046_&ucity=28")!)
//                        .tabItem {
//                            Image(systemName: "\(i + 1).square")
//                        }
//                } else {
//                    NaiveSafariView(url: URL(string:"https://m.ctrip.com/webapp/hotel/cityD104_28/?days=1&atime=20220221&geo=1&ulocation=30.757704017297133_103.92946288059046_&ulat=\(WebCitiesCoordinate[i - 1].latitude)&ulon=\(WebCitiesCoordinate[i - 1].longitude)&ucity=28")!)
//                    .tabItem {
//                        Image(systemName: "\(i + 1).square")
//                    }
//                }
//            }
//        }
//    }
//}
//
//struct ThroughView_Previews: PreviewProvider {
//    static var previews: some View {
//        ThroughView(userData: UserData())
//    }
//}
