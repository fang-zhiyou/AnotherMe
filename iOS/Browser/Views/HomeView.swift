import SwiftUI
import CoreData

struct HomeView: View {
    @ObservedObject var userData: UserData
    
    @Environment(\.managedObjectContext) private var viewContext
    @FetchRequest(entity: Profile.entity(), sortDescriptors: [])
    var profiles: FetchedResults<Profile>
    @FetchRequest(entity: POI.entity(), sortDescriptors: [])
    var pois: FetchedResults<POI>
    
    @State private var showImport: Bool = false
    @State private var updateLocationTimer = Timer.publish(every: 5, on: .main, in: .common).autoconnect()
    
    var locationManager = LocationManager()
    
    var body: some View {
        VStack {
            // Title
            HStack {
                Image(systemName: "pause.circle")
                    .foregroundColor(.blue)
                    .font(.homeTitle)
                
                Text("AnotherMe")
                    .font(.system(size: 45))
            }
            .padding()
            
            // City Num Stepper
            Stepper(value: $userData.selectedCityNum, in: self.userData.selectedAnchors.count...5) {
                Text("Number of positions: \(userData.selectedCityNum)", tableName: "LocalizableWithVariable")
                    .font(.headline)
            }
            .padding(.horizontal, 80)
            
            // File Importer
            VStack(alignment: .leading) {
                HStack {
                    Button(action: {
                        showImport.toggle()
                    }, label: {
                        Text("Select File")
                            .padding(5)
                            .background(.blue)
                            .foregroundColor(.white)
                            .cornerRadius(5)
                    })
                    .fileImporter(isPresented: $showImport, allowedContentTypes: [.commaSeparatedText], onCompletion: { result in
                        switch result {
                        case .success(let url):
                            do {
                                _ = url.startAccessingSecurityScopedResource()
                                let fileData = try Data(contentsOf: url)
                                if let text = String(data: fileData, encoding: .utf8) {
                                    DeleteAllPois()
                                    self.userData.mappingPoints = [MappingPoint]()
                                    DeleteVirtualProfiles()
                                    userData.csvDataList = CsvDataList(csvText: text)
                                    InsertCsvToPois()
                                    let fetchRequest = NSFetchRequest<Profile>(entityName: "Profile")
                                    fetchRequest.fetchLimit = 10
                                    fetchRequest.fetchOffset = 0
                                    let predicate = NSPredicate(format: "isReal = 1", "")
                                    fetchRequest.predicate = predicate
                                    do {
                                        let userProfiles = try self.viewContext.fetch(fetchRequest)
                                        for userProfile in userProfiles {
                                            for resPoi in self.userData.csvPois {
                                                let poi = POI(context: self.viewContext)
                                                poi.id = UUID()
                                                poi.latitude = resPoi.point.latitude
                                                poi.longitude = resPoi.point.longitude
                                                poi.type = resPoi.typeCode
                                                poi.profile = userProfile
                                            }
                                            try? self.viewContext.save()
                                        }
                                    } catch {
                                        print(error)
                                    }
                                    
                                    for anchor in self.userData.selectedAnchors {
                                        let poiAnchorIndex = GetPoiAnchorIndex(pois: self.userData.csvPois)
                                        let poiAnchor = self.userData.csvPois[poiAnchorIndex]
                                        self.userData.mappingPoints = []
                                        for poi in self.userData.csvPois {
                                            self.userData.mappingPoints.append(GetMappingPoint(mappingPOI: poi, poiAnchor: poiAnchor, virtualAnchor: anchor))
                                        }
                                        
                                        let virtualProfile = Profile(context: self.viewContext)
                                        virtualProfile.id = UUID()
                                        virtualProfile.city = anchor.name
                                        virtualProfile.isReal = false
                                        try? self.viewContext.save()
                                        
                                        let fetchRequestVirtual = NSFetchRequest<Profile>(entityName: "Profile")
                                        fetchRequestVirtual.fetchLimit = 10
                                        fetchRequestVirtual.fetchOffset = 0
                                        let predicateVirtual = NSPredicate(format: "isReal = 0 && city = '\(anchor.name)'", "")
                                        fetchRequestVirtual.predicate = predicateVirtual
                                        do {
                                            let virtualProfiles = try self.viewContext.fetch(fetchRequestVirtual)
                                            for virtualProfile in virtualProfiles {
                                                for mappingPoint in self.userData.mappingPoints {
                                                    let poi = POI(context: self.viewContext)
                                                    poi.id = UUID()
                                                    poi.latitude = mappingPoint.point.latitude
                                                    poi.longitude = mappingPoint.point.longitude
                                                    poi.type = mappingPoint.typeCode
                                                    poi.profile = virtualProfile
                                                }
                                                try? self.viewContext.save()
                                            }
                                        } catch {
                                            print(error)
                                        }
                                    }

                                }
                                url.stopAccessingSecurityScopedResource()
                            }
                            catch let error {
                                print(error)
                            }
                        case .failure(let error):
                            print(error)
                        }
                        $showImport.wrappedValue = false
                    })
                    .padding()
                    
                    if profiles.count > 1 {
                        Text("File selected")
                    } else {
                        Text("No file selected")
                    }
                }
            }
            
//            Button("Create Virtual Profile") {
//                let virtualProfile = Profile(context: self.viewContext)
//                virtualProfile.id = UUID()
//                virtualProfile.isReal = false
//                try? self.viewContext.save()
//            }
//            Button("Delete Virtual Profile") {
//                DeleteVirtualProfiles()
//            }
//            Button("Print all Profiles") {
//                for profile in profiles {
//                    print("isReal:", profile.isReal)
//                    print("city:", profile.city ?? "none")
//                    print("Capacity of GPS Logs:", profile.gpsLogs!.count)
//                    print("Capacity of POIs:", profile.pois!.count)
//                    print()
//                }
//            }
//            Button("Print Pois") {
//                for poi in pois {
//                    print("city:", poi.profile?.city ?? "none")
//                    print("coordinate:", poi.latitude, poi.longitude)
//                    print()
//                }
//            }
//            Button("Print GPS Logs") {
//                let fetchRequest = NSFetchRequest<Profile>(entityName: "Profile")
//                fetchRequest.fetchLimit = 10
//                fetchRequest.fetchOffset = 0
//                let predicate = NSPredicate(format: "isReal = 1", "")
//                fetchRequest.predicate = predicate
//                do {
//                    let userProfiles = try self.viewContext.fetch(fetchRequest)
//                    for userProfile in userProfiles {
//                        let gpsLogs = (userProfile.gpsLogs?.allObjects as? [GPSLog])!.sorted(by: {return $0.timestamp < $1.timestamp})
//                        for gpsLog in gpsLogs {
//                            print("latitude:", gpsLog.latitude)
//                            print("longitude:", gpsLog.longitude)
//                            print("timestamp:", timeIntervalChangeToTimeStr(time: gpsLog.timestamp))
//                            print()
//                        }
//                    }
//                } catch {
//                    print(error)
//                }
//            }
//            Button("Delete all Pois") {
//                DeleteAllPois()
//            }
        }
        .onAppear {
            self.userData.updateAvalible = true
        }
        .onReceive(updateLocationTimer) { _ in
            if self.userData.updateAvalible {
                
                // User Profile Initialization
                if profiles.isEmpty {
                    let userProfile = Profile(context: self.viewContext)
                    userProfile.id = UUID()
                    userProfile.city = "Beijing"
                    userProfile.isReal = true
                }
                
                let fetchRequest = NSFetchRequest<Profile>(entityName: "Profile")
                fetchRequest.fetchLimit = 10
                fetchRequest.fetchOffset = 0
                let predicate = NSPredicate(format: "isReal = 1", "")
                fetchRequest.predicate = predicate
                if userData.points.isEmpty {
                    do {
                        let userProfiles = try self.viewContext.fetch(fetchRequest)
                        for userProfile in userProfiles {
                            let gpsLogs = (userProfile.gpsLogs?.allObjects as? [GPSLog])!
                            for gpsLog in gpsLogs {
                                let latitude = gpsLog.latitude
                                let longitude = gpsLog.longitude
                                let timestamp = gpsLog.timestamp
                                userData.points.append(Point(latitude: latitude, longitude: longitude, timeStamp: timestamp))
                            }
                        }
                    } catch {
                        print(error)
                    }
                }
                let latitude = locationManager.lastLocation?.coordinate.latitude ?? 0
                let longitude = locationManager.lastLocation?.coordinate.longitude ?? 0
                let timestamp = NSDate().timeIntervalSince1970
                userData.points.append(Point(latitude: latitude, longitude: longitude, timeStamp: timestamp))
                do {
                    let userProfiles = try self.viewContext.fetch(fetchRequest)
                    for userProfile in userProfiles {
                        let point = GPSLog(context: self.viewContext)
                        point.id = UUID()
                        point.latitude = latitude
                        point.longitude = longitude
                        point.timestamp = timestamp
                        point.profile = userProfile
                    }
                    try? self.viewContext.save()
                } catch {
                    print(error)
                }
            }
        }
    }
    
    func InsertCsvToPois() {
        var csvPoints = [Point]()
        var node = self.userData.csvDataList?.head
        
        while node?.next != nil {
            let latitude = Double(node?.csvData.latitude ?? "0") ?? 0
            let longitude = Double(node?.csvData.longitude ?? "0") ?? 0
            let csvDateTime = node?.csvData.dateTime ?? "1971-1-1 00:00:00"
            let timeStamp = timeStrChangeTotimeInterval(dateTime: csvDateTime)
            
            csvPoints.append(Point(latitude: latitude, longitude: longitude, timeStamp: timeStamp))
            node = node?.next
        }
        
        let csvStayPoints = GetStayPoint(points: csvPoints)
        for csvStayPoint in csvStayPoints {
            self.userData.csvPois.append(UserDataPOI(stayPoint: csvStayPoint))
        }

        for i in 0 ..< self.userData.csvPois.count {
            print(i)
            print(self.userData.csvPois[i].point.latitude)
            print(self.userData.csvPois[i].point.longitude)
            print(self.userData.csvPois[i].typeCode)
            print(self.userData.csvPois[i].stayTime)
            print()
        }
    }
    
    func DeleteAllPois() {
        self.userData.csvPois = [UserDataPOI]()
        for poi in pois {
            self.viewContext.delete(poi)
        }
        try? self.viewContext.save()
    }
    
    func DeleteVirtualProfiles() {
        let fetchRequest = NSFetchRequest<Profile>(entityName: "Profile")
        fetchRequest.fetchLimit = 10
        fetchRequest.fetchOffset = 0
        let predicate = NSPredicate(format: "isReal = 0", "")
        fetchRequest.predicate = predicate
        do {
            let virtualProfiles = try self.viewContext.fetch(fetchRequest)
            for virtualProfile in virtualProfiles {
                self.viewContext.delete(virtualProfile)
            }
            try? self.viewContext.save()
        } catch {
            print(error)
        }
    }
}

struct HomeView_Previews: PreviewProvider {
    static var previews: some View {
        HomeView(userData: UserData())
    }
}
