package com.example.contest.Utils.algorithm.geography;

import com.example.contest.Utils.algorithm.stayPoint.StayPoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Point { //定义地理点的数据结构，包含 时间，经度，纬度

    public long timestamp;
    public double longitude;
    public double latitude;
    public Point(){}
    public Point(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
//        Log.e("Point",Long.toString(timestamp)+Double.toString(longitude)+Double.toString(latitude));
    }

    public static Point phrasePoint(StayPoint stayPoint) {
        return new Point(stayPoint.arriveTime, stayPoint.longitude, stayPoint.latitude);
    }

    public Point(long timestamp, double longitude, double latitude) {
        this.timestamp = timestamp;
        this.longitude = longitude;
        this.latitude = latitude;
//        Log.e("Point",Long.toString(timestamp)+Double.toString(longitude)+Double.toString(latitude));
    }
}
