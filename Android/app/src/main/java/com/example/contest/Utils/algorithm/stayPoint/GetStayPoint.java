package com.example.contest.Utils.algorithm.stayPoint;

import com.example.contest.Utils.algorithm.geography.Calculations;
import com.example.contest.Utils.algorithm.geography.Point;

import java.io.IOException;
import java.util.ArrayList;

public class GetStayPoint {
    public static double distThrsh = 1000; //距离间隔 单位:米
    public static double timeThrsh = 60*10; //时间间隔 单位: 秒

    public static ArrayList<StayPoint> getStayPoint(ArrayList<Point> points) {
        ArrayList<StayPoint> stayPoints = new ArrayList<StayPoint>();
        int pointNum = points.size();
        for (int i = 0,j; i < pointNum; ) {
            Point p1 = points.get(i);
            for (j = i + 1; j < pointNum; j++) {
                Point p2 = points.get(j);
                double dist = Calculations.getDistFromGeo(p1, p2);
                if (dist > distThrsh) {
                    double deltaT = p2.timestamp - p1.timestamp;
                    if (deltaT > timeThrsh) {
                        Point coordination = Calculations.computeMeanCoord(points, i, j);
                        long arriveTime = p1.timestamp, leaveTime = p2.timestamp;
                        stayPoints.add(new StayPoint(coordination, arriveTime, leaveTime));
                    }
                    i = j;
                    break;
                }
            }
            if(j==pointNum){
                break;
            }
        }
        return stayPoints;
    }



}

