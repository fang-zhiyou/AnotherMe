import SwiftUI

struct SystemSettingView: View {
    @ObservedObject var userData: UserData
    var body: some View {
        Form {
            
        }
        .navigationBarTitle("System Settings", displayMode: .inline)
    }
}

struct SystemSettingView_Previews: PreviewProvider {
    static var previews: some View {
        SystemSettingView(userData: UserData())
    }
}
