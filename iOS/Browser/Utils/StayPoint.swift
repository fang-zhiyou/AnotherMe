import Foundation
import CoreLocation
import SwiftyJSON

public class UserDataPOI {
    var point: Point
    var typeCode: String
    var stayTime: Double
    init(stayPoint: StayPoint) {
        self.point = stayPoint.point
        self.typeCode = stayPoint.typeCode
        self.stayTime = stayPoint.leaveTime - stayPoint.arriveTime
    }
    init(poi: POI) {
        self.point = Point(latitude: poi.latitude, longitude: poi.longitude)
        self.typeCode = poi.type ?? "none"
        self.stayTime = 0
    }
}

public func GetUserDataPois(pois: [POI]) -> [UserDataPOI] {
    var userDataPois = [UserDataPOI]()
    for poi in pois {
        userDataPois.append(UserDataPOI(poi: poi))
    }
    return userDataPois
}

public func GetPoiAnchorIndex(pois: [UserDataPOI]) -> Int {
    var poiAnchorIndex: Int = 0
    var longestStayTime: Double = 0
    for i in 0 ..< pois.count {
        if pois[i].stayTime > longestStayTime {
            poiAnchorIndex = i
            longestStayTime = pois[i].stayTime
        }
    }
    return poiAnchorIndex
}

public class StayPoint {
    var point: Point
    var arriveTime: Double
    var leaveTime: Double
    var typeCode: String = ""
    
    private var JSONString: String = ""
    private let semaphore = DispatchSemaphore(value: 0)
    
    init(point: Point, arriveTime: Double, leaveTime: Double) {
        self.point = point
        self.arriveTime = arriveTime
        self.leaveTime = leaveTime
    }
    
    func GetTypeCode() {
        self.typeCode = CoordinateToTypeCode(latitude: self.point.latitude, longitude: self.point.longitude)
    }
    
    private func PeripheralSearchAPI(latitude: Double, longitude: Double) {
        let url: URL = URL(string: "https://restapi.amap.com/v3/geocode/regeo?key=\(UserData().webAPIKey)&location=\(longitude),\(latitude)&poitype=&radius=&extensions=all&batch=false&roadlevel=0")!
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
    
    private func CoordinateToTypeCode(latitude: Double, longitude: Double) -> String {
        PeripheralSearchAPI(latitude: latitude, longitude: longitude)
        _ = self.semaphore.wait(timeout: DispatchTime.distantFuture)
        let jsonData = JSONString.data(using: .utf8)!
        let transferData = JSON(jsonData)
        let typeCode = transferData["regeocode"]["pois"][0]["type"].stringValue
        return typeCode
    }
}

public struct Point: Hashable {
    var latitude: Double
    var longitude: Double
    var timeStamp: Double = 0
}

public func GetRadian(degree: Double) -> Double {
    return degree * Double.pi / 180.0
}

public func GetDistance(p1: Point, p2: Point) -> Double {
    let EARTH_RADIUS: Double = 6378137
    let dlat: Double = GetRadian(degree: p1.latitude) - GetRadian(degree: p2.latitude)
    let dlon: Double = GetRadian(degree: p1.longitude) - GetRadian(degree: p2.longitude)
    let distance: Double = 2 * asin(sqrt(pow(sin(dlat / 2), 2) + cos(p1.latitude) * cos(p2.latitude) * pow(sin(dlon / 2), 2)))
    return distance * EARTH_RADIUS
}

public func GetMeanCoordination(points: [Point], i: Int, j: Int) -> Point {
    var coordination = Point(latitude: 0, longitude: 0)
    
    for k in i ..< j {
        coordination.latitude += points[k].latitude
        coordination.longitude += points[k].longitude
    }
    
    let count = j - i
    coordination.latitude /= Double(count)
    coordination.longitude /= Double(count)
    
    return coordination
}

public func GetStayPoint(points: [Point]) -> [StayPoint] {
    let distThreh: Double = 200
    let timeThreh: Double = 1800
    
    var stayPoints = [StayPoint]()
    
    let pointsNum = points.count
    var i = 0
    
    while i < pointsNum {
        let p1 = points[i]
        
        for j in (i + 1) ..< pointsNum {
            let p2 = points[j]
            let distance = GetDistance(p1: p1, p2: p2)
            
            if distance > distThreh {
                let timeDifference = p2.timeStamp - p1.timeStamp
                
                if timeDifference > timeThreh {
                    let coordination = GetMeanCoordination(points: points, i: i, j: j)
                    let stayPoint = StayPoint(point: coordination, arriveTime: p1.timeStamp, leaveTime: p2.timeStamp)
                    stayPoint.GetTypeCode()
                    stayPoints.append(stayPoint)
                }
                
                i = j - 1
                break
            }
        }
        
        i += 1
    }
    
    return stayPoints
}
