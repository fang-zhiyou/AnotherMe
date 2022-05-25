import SwiftUI
import MapKit
import CoreData

public func cityLocalizable(city: String) -> String {
    var lng: String {
        let userDefault = UserDefaults.standard
        let languages:NSArray = userDefault.object(forKey: "AppleLanguages") as! NSArray
        return languages.object(at: 0) as! String
    }
    var resCity = city
    if lng == "zh-Hans-CN" {
        switch resCity {
        case "Chengdu":
            resCity = "成都"
        case "Beijing":
            resCity = "北京"
        case "Shanghai":
            resCity = "上海"
        case "Xiamen":
            resCity = "厦门"
        default: break
        }
    }
    return resCity
}

struct DisplayProfile: Hashable {
    let id = UUID()
    var isReal: Bool?
    var city: String?
    var pois: [POI]?
    init() {}
    init(profile: Profile) {
        self.isReal = profile.isReal
        self.city = profile.city
        self.pois = (profile.pois?.allObjects as! [POI])
    }
}

struct POIView: View {
    @ObservedObject var userData: UserData
    
    @Environment(\.managedObjectContext) private var viewContext
    @FetchRequest(entity: Profile.entity(), sortDescriptors: [])
    var profiles: FetchedResults<Profile>
    var displayProfiles: [DisplayProfile] {
        var displayProfiles = [DisplayProfile]()
        for profile in profiles {
            displayProfiles.append(DisplayProfile(profile: profile))
        }
        return displayProfiles
    }
    
    @State var showSheet: Bool = false
    
    var body: some View {
        NavigationView {
            Form {
                if self.profiles.count > 1 {
                    Section(content: {
                        ForEach(self.displayProfiles, id: \.id) { profile in
                            if profile.isReal! {
                                NavigationLink(destination: POIMapView(pois: GetUserDataPois(pois: profile.pois!)), label: {
                                    Text("\(cityLocalizable(city: profile.city!))")
                                })
                            }
                        }
                    }, header: {
                        Text("Real")
                    })
                    
                    Section(content: {
                        ForEach(self.displayProfiles, id: \.id) { profile in
                            if !profile.isReal! {
                                NavigationLink(destination: POIMapView(pois: GetUserDataPois(pois: profile.pois!)), label: {
                                    Text("\(cityLocalizable(city: profile.city!))")
                                })
                            }
                        }
                    }, header: {
                        Text("Virtual")
                    })
                }
            }
            .navigationBarTitle("Mapping", displayMode: .inline)
            .onAppear {
                self.userData.updateAvalible = false
            }
        }
    }
}


struct StayPointView_Previews: PreviewProvider {
    static var previews: some View {
        POIView(userData: UserData())
    }
}
