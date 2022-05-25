import SwiftUI
import CoreData

struct CacheView: View {
    @ObservedObject var userData: UserData
    
    @Environment(\.managedObjectContext) private var viewContext

    @FetchRequest(entity: GPSLog.entity(), sortDescriptors: [])
    
    var GPSLogs: FetchedResults<GPSLog>
    
    @State private var showingAlert = false
    
    var body: some View {

        Form {
            Button(action: {
                self.showingAlert = true
            }, label: {
                Text("Clear GPS Logs")
                    .foregroundColor(.red)
            })
            .alert(isPresented: $showingAlert, content: {
                .init(title: Text("All GPS Logs will be Deleted"),
                      message: Text("You cannot undo this operation."),
                      primaryButton: .cancel(),
                      secondaryButton: .destructive(Text("Delete"), action: DeleteAllGPSLogs))
                      })
            
            Button(action: {
                self.showingAlert = true
            }, label: {
                Text("Clear Virtual Profiles")
                    .foregroundColor(.red)
            })
            .alert(isPresented: $showingAlert, content: {
                .init(title: Text("All Virtual Profiles will be Deleted"),
                      message: Text("You cannot undo this operation."),
                      primaryButton: .cancel(),
                      secondaryButton: .destructive(Text("Delete"), action: DeleteVirtualProfiles))
                      })
            
            Section {
                Button(action: {
                    self.showingAlert = true
                }, label: {
                    Text("Clear All Data")
                        .foregroundColor(.red)
                })
                .alert(isPresented: $showingAlert, content: {
                    .init(title: Text("All Data will be Deleted"),
                          message: Text("You cannot undo this operation."),
                          primaryButton: .cancel(),
                          secondaryButton: .destructive(Text("Delete"), action: DeleteAllData))
                          })
            }
        }
        .navigationBarTitle("Cache", displayMode: .inline)
    }
    
    private func DeleteAllGPSLogs() {
        self.userData.points = [Point]()
        for i in 0 ..< GPSLogs.count {
            viewContext.delete(GPSLogs[i])
        }
        do {
            try viewContext.save()
        } catch {
            let nsError = error as NSError
            fatalError("Unresolved error \(nsError), \(nsError.userInfo)")
        }
    }
    private func DeleteVirtualProfiles() {
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
    private func DeleteAllData() {
        DeleteAllGPSLogs()
        let fetchRequest = NSFetchRequest<Profile>(entityName: "Profile")
        fetchRequest.fetchLimit = 10
        fetchRequest.fetchOffset = 0
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

struct CacheView_Previews: PreviewProvider {
    static var previews: some View {
        CacheView(userData: UserData())
    }
}
