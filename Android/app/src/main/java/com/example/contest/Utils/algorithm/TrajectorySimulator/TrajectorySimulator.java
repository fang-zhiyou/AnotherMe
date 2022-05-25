package com.example.contest.Utils.algorithm.TrajectorySimulator;

import com.example.contest.Utils.algorithm.geography.Calculations;
import com.example.contest.Utils.algorithm.geography.Point;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrajectorySimulator { //输入轨迹点和对应轨迹的POI，输出模拟后的轨迹点
    ArrayList<Point> points;
    ArrayList<Point> pois;
    ArrayList<Double> speedSeq;
    double averageSpeed;
    double insertInterval;
    int k_shapeObfuscation;

    private  double getAverageSpeed(ArrayList<Point> points) {
        double averageSpeed = 0;
        for (int i = 0; i < speedSeq.size() - 1; i++) {
            averageSpeed += speedSeq.get(i);
        }
        return averageSpeed / (points.size() - 1);
    }

    public TrajectorySimulator(ArrayList<Point> points, ArrayList<Point> pois, double insertInterval,int k_shapeObfuscation) {
        this.points = points;
        this.pois = pois;
        this.speedSeq = new ArrayList<>();
        this.insertInterval=insertInterval;
        this.k_shapeObfuscation=k_shapeObfuscation;
    }

    public ArrayList<Point> trajectorySimulate() {
        for (int i = 0; i < points.size()-1; i++) {
            Point p1 = points.get(i), p2 = points.get(i + 1);
            double dis = Calculations.getDistFromGeo(p1, p2);
            double duration = Math.abs(p1.timestamp - p2.timestamp);
            if(duration<=0){
                duration=10;
            }
            speedSeq.add(dis / duration);
        }
        averageSpeed = getAverageSpeed(points);
        int transport_type; //出行方式 0步行 1骑行 2驾车

        if (averageSpeed < 1.2) {
            transport_type = 0;
        } else if (averageSpeed < 5) {
            transport_type = 1;
        } else {
            transport_type = 2;
        }


        transport_type=2;
        ArrayList<Point> navPoints=new ArrayList<>();
        try{
            navPoints = GetNavTrajectories.getNavTrajectories(pois, transport_type);
        }catch (Exception e){
            return navPoints;
        }
//        return  navPoints;

        SpeedObfuscation speedObfuscation = new SpeedObfuscation(insertInterval, speedSeq);

        HashMap<String, List> set = speedObfuscation.speedObfuscation(navPoints);


        ArrayList<Point> trajectory = (ArrayList<Point>) set.get("trajectory");


        ArrayList<Integer> inflection_index = (ArrayList<Integer>) set.get("inflection_index");

        trajectory=ShapeObfuscation.shapeObfuscation(trajectory, inflection_index, k_shapeObfuscation);

        return trajectory;
    }
}
