import Foundation

public class UserData: ObservableObject {
    @Published var webAPIKey = "73df26a3aa615bf9f294bbcba69afe44"
    @Published var selectedCityNum = 5
    @Published var csvDataList: CsvDataList?
    @Published var points: [Point] = []
    @Published var userStayPoints: [StayPoint] = [StayPoint(point: Point(latitude: 30.752058, longitude: 103.930219), arriveTime: 123456, leaveTime: 123456)]
    @Published var csvPois: [UserDataPOI] = []
    @Published var selectedAnchors: [Anchor] = [Anchor(name: "Chengdu", coordinate: CityCoordinate().Chengdu)]
    @Published var mappingPoints = [MappingPoint]()
    @Published var displayPoints = [[DisplayPoint]]()
    @Published var updateAvalible = true
}
