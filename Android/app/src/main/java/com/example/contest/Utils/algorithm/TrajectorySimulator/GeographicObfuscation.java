package com.example.contest.Utils.algorithm.TrajectorySimulator;

import com.example.contest.Utils.algorithm.geography.Calculations;
import com.example.contest.Utils.algorithm.geography.Point;

import java.util.ArrayList;

public class GeographicObfuscation {
    public static ArrayList<Point> geographicObfuscate(ArrayList<Point> points){
        for (Point p :points) {
            double alpha= Calculations.gaussianRandom(0,0.000000005);
            double beta=Math.random()*2*Math.PI;
            p.latitude+=alpha*Math.sin(beta);
            p.longitude+=alpha*Math.cos(beta);
        }
        return points;
    }
}
