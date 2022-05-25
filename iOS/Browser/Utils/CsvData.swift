import Foundation

public class CsvDataList {
    var head: CsvListNode?
    var tail: CsvListNode?
    
    init(csvText: String) {
        var dataInLineArray = csvText.components(separatedBy: "\n")
        dataInLineArray.removeAll(where: {
            $0 == ""
        })
        for dataInLine in dataInLineArray {
            Append(dataInLine: dataInLine)
        }
    }
    
    private func Append(dataInLine: String) {
        let newCsvListNode = CsvListNode(dataInLine: dataInLine)
        if let tailNode = tail {
            tailNode.next = newCsvListNode
        } else {
            head = newCsvListNode
        }
        tail = newCsvListNode
    }
}

public class CsvListNode {
    var csvData: CsvData
    var next: CsvListNode? = nil
    
    init(dataInLine: String) {
        let dataArray = dataInLine.components(separatedBy: ",")
        self.csvData = CsvData(taxiID: dataArray[0], dateTime: dataArray[1], longitude: dataArray[2], latitude: dataArray[3])
    }
}

public class CsvData {
    var taxiID: String?
    var dateTime: String?
    var longitude: String?
    var latitude: String?
    
    init(taxiID: String, dateTime: String, longitude: String, latitude: String) {
        self.taxiID = taxiID
        self.dateTime = dateTime
        self.longitude = longitude
        self.latitude = latitude
    }
}
