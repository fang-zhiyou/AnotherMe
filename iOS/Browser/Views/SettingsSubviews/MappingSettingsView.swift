import SwiftUI

struct MappingSettingsView: View {
    @ObservedObject var userData: UserData
    var body: some View {
        Form {
            Section(content: {
                Stepper(value: $userData.selectedCityNum, in: self.userData.selectedAnchors.count...5) {
                    Text("Number of positions: \(userData.selectedCityNum)", tableName: "LocalizableWithVariable")
                }
            }, footer: {
                Text("Set the number of virtual positions. When you send a request with location, the browser will pop up the corresponding number of interfaces.")
            })
            
            Section(content: {
                ChooseAnchorView(userData: userData)
                ForEach(anchors, id: \.self) { anchor in
                    
                }
            }, header: {
                Text("Choose Anchors")
            })
        }
        .navigationBarTitle("Mapping Settings", displayMode: .inline)
    }
}

struct SetKValueView_Previews: PreviewProvider {
    static var previews: some View {
        MappingSettingsView(userData: UserData())
    }
}
