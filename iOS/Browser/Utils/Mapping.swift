import Foundation
import SwiftyJSON

class MappingPoint {
    var point = Point(latitude: 0, longitude: 0)
    var typeCode: String?
    
    private var JSONString: String = ""
    private let semaphore = DispatchSemaphore(value: 0)
    
    init(typeCode: String) {
        self.typeCode = typeCode
    }
    
    init() {}
    
    func GetPoint(anchor: Anchor, anchorIndex: Int) {
        PeripheralSearchAPI(anchor: anchor)
        _ = self.semaphore.wait(timeout: DispatchTime.distantFuture)
        let jsonData = JSONString.data(using: .utf8)!
        let transferData = JSON(jsonData)
        let coordinate = transferData["pois"][anchorIndex]["location"].stringValue.components(separatedBy: ",")
        self.point = Point(latitude: Double(coordinate[1])!, longitude: Double(coordinate[0])!)
    }
    
    private func PeripheralSearchAPI(anchor: Anchor) {
        let url: URL = URL(string: "https://restapi.amap.com/v3/place/around?key=\(UserData().webAPIKey)&location=\(anchor.coordinate.latitude),\(anchor.coordinate.longitude)&keywords=&types=\(self.typeCode!)&radius=&offset=25&page=1&extensions=base".addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!)!
        var urlRequest = URLRequest(url: url)
        urlRequest.addValue("text/plain", forHTTPHeaderField: "Accept")
        URLSession.shared.dataTask(with: urlRequest) { data, response, error in
            if let data = data,
               let httpResponse = response as? HTTPURLResponse, (200..<300) ~= httpResponse.statusCode,
               let strData = String(bytes: data, encoding: .utf8) {
                self.JSONString = strData
            }
            self.semaphore.signal()
        }
        .resume()
    }
}

func GetMappingPoint(mappingPOI: UserDataPOI, poiAnchor: UserDataPOI, virtualAnchor: Anchor) -> MappingPoint {
    let realDistance = GetDistance(p1: mappingPOI.point, p2: poiAnchor.point)
    var shortestDistanceDifference = Double(truncating: kCFNumberPositiveInfinity)
    var resMappingPoint = MappingPoint()
    for i in 0 ..< 15 {
        let mappingPoint = MappingPoint(typeCode: mappingPOI.typeCode)
        mappingPoint.GetPoint(anchor: virtualAnchor, anchorIndex: i)
        let mappingDistance = GetDistance(p1: virtualAnchor.coordinate, p2: mappingPoint.point)
        let distanceDifference = abs(mappingDistance - realDistance)
        if distanceDifference < shortestDistanceDifference {
            shortestDistanceDifference = distanceDifference
            resMappingPoint = mappingPoint
        }
    }
    return resMappingPoint
}
