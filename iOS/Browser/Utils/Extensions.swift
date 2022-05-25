import SwiftUI

extension Font {
    static let homeTitle: Font = system(size: 120, weight: .bold)
}

func timeStrChangeTotimeInterval(dateTime: String) -> Double {
    let format = DateFormatter.init()
    format.dateStyle = .medium
    format.timeStyle = .short
    format.dateFormat = "yyyy-MM-dd HH:mm:ss"
    let date = format.date(from: dateTime)
    return date!.timeIntervalSince1970
}

func timeIntervalChangeToTimeStr(time: Double) -> String {
    let date = Date(timeIntervalSince1970: time)
    let dateFormatter = DateFormatter()
    dateFormatter.timeStyle = .short
    dateFormatter.dateStyle = .medium
    dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
    dateFormatter.timeZone = .current
    let localDate = dateFormatter.string(from: date)
    return localDate
}
