import SwiftUI

struct MultipleSelectionRow: View {
    var title: String
    var isSelected: Bool
    var isAvalible: Bool
    var action: () -> Void
    
    var body: some View {
        Button(action: self.action) {
            HStack {
                SelectState()
                Text(LocalizedStringKey(self.title))
                    .foregroundColor(LabelColor())
                Spacer()
            }
        }
        .disabled(true)
    }
    
    func LabelColor() -> Color {
        if self.isAvalible == true {
            return .black
        } else {
            return .gray
        }
    }
    
    func SelectState() -> some View {
        if self.isSelected {
            return Image(systemName: "checkmark.circle.fill").font(.title2)
        } else {
            if self.isAvalible {
                return Image(systemName: "circle").font(.title2)
            } else {
                return Image(systemName: "circle").font(.title2)
            }
        }
    }
}

struct ChooseAnchorView: View {
    @ObservedObject var userData: UserData
    
    var body: some View {
        ForEach(anchors, id: \.self) { anchor in
            MultipleSelectionRow(title: anchor.name,
                                 isSelected: userData.selectedAnchors.contains(anchor),
                                 isAvalible: userData.selectedCityNum != userData.selectedAnchors.count || userData.selectedAnchors.contains(anchor)) {
                if userData.selectedAnchors.contains(anchor) {
                    userData.selectedAnchors.removeAll(where: {
                        $0 == anchor
                    })
                }
                else {
                    userData.selectedAnchors.append(anchor)
                }
            }
        }
    }
}

struct StayPoints_Previews: PreviewProvider {
    static var previews: some View {
        ChooseAnchorView(userData: UserData())
    }
}
