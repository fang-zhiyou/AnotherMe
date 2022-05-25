package com.example.contest.Utils.algorithm.TrajectorySimulator;

import com.example.contest.Utils.algorithm.geography.Calculations;
import com.example.contest.Utils.algorithm.geography.Point;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpeedObfuscation {
    private double insertInterval; //插入间隔，单位秒
    private ArrayList<Double> speedSeq;

    private ArrayList<Point> insertBetween2Points(Point navP1, Point navP2, int speed_index) {
        ArrayList<Point> trajectory = new ArrayList<>();
        trajectory.add(navP1);

        double[] navP1_cart = {0, 0};
        double[] navP2_cart = Calculations.Geo2Cart(navP1, navP2);
        double dis = Calculations.getDisFromCart(navP1_cart, navP2_cart);

        double advance_dis; //当前前进距离
        double v; //当前速度
        int i = speed_index;

        double[] alpha; //单位方向向量
        alpha = new double[]{navP2_cart[0] / dis, navP2_cart[1] / dis};

        for ( v = speedSeq.get(i),advance_dis = v * insertInterval; advance_dis < dis;
              v = speedSeq.get(i), i = Math.min(i + 1, speedSeq.size() - 1),advance_dis += v * insertInterval) {
            double[] insertP = {alpha[0] * advance_dis, alpha[1] * advance_dis};
            trajectory.add(Calculations.Cart2Geo(navP1, insertP));
        }
        trajectory.add(navP2);
        return trajectory;
    }

    public HashMap<String, List> speedObfuscation(ArrayList<Point> navPoints) {
        HashMap<String, List> set = new HashMap<>();
        ArrayList<Point> trajectory = new ArrayList<>();
        ArrayList<Integer> inflection_index = new ArrayList<>();
        int speed_index = 0;
        for (int i = 0; i < navPoints.size() - 1; i++) {
            Point navP1 = navPoints.get(i), navP2 = navPoints.get(i + 1);
            ArrayList<Point> inserted_points = insertBetween2Points(navP1, navP2, speed_index);

            speed_index = Math.min(speedSeq.size() - 1, speed_index + inserted_points.size());
            trajectory.addAll(inserted_points);
            inflection_index.add(Math.max(trajectory.size() - 1, 0));
        }
        set.put("trajectory", trajectory);
        set.put("inflection_index", inflection_index);
        return set;
    }

    public SpeedObfuscation(double insertInterval, ArrayList<Double> speedSeq) {
        this.speedSeq = speedSeq;
        this.insertInterval = insertInterval;
    }
}
