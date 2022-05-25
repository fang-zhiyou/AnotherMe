package com.example.contest.Utils.algorithm.TrajectorySimulator;

import com.example.contest.Utils.algorithm.geography.Calculations;
import com.example.contest.Utils.algorithm.geography.Point;

import java.util.ArrayList;

public class ShapeObfuscation {
    public ShapeObfuscation() {
    }

    public static ArrayList<Point> shapeObfuscation(ArrayList<Point> trajectory, ArrayList<Integer> inflection_index, int k) {
        for (Integer index : inflection_index) {
            Point inflection_point = trajectory.get(index);
            int xn = Math.min(index + k, trajectory.size() - 1);
            int yn = Math.max(index - k, 0);
            Point p_xn = trajectory.get(xn);
            Point p_yn = trajectory.get(yn);
            double[] origin_point = {0, 0};
            double[] p_xn_cart = Calculations.Geo2Cart(inflection_point, p_xn);
            double[] p_yn_cart = Calculations.Geo2Cart(inflection_point, p_yn);
            double[] center_point = new double[2]; //平分线中点坐标

            center_point[0] = (p_xn_cart[0] + p_yn_cart[0]) / 2;
            center_point[1] = (p_xn_cart[1] + p_yn_cart[1]) / 2;

            double dis = Calculations.getDisFromCart(center_point, origin_point);
            double random_dis = dis * Math.random();
            double alpha = -random_dis / dis + 1;
            double[] p_hat = {center_point[0] * alpha, center_point[1] * alpha};


            for (int i = yn; i < index; i++) {
                double j = index - i;
                double[] p_j = {0, 0};
                p_j[0] = p_hat[0] + j * (p_yn_cart[0] - p_hat[0]) / k; //k平分点位置
                p_j[1] = p_hat[1] + j * (p_yn_cart[1] - p_hat[1]) / k;


                Point rectified = Calculations.Cart2Geo(inflection_point, p_j);
                trajectory.set(i, rectified);

            }
            trajectory.set(index, Calculations.Cart2Geo(inflection_point, p_hat));
            for (int i = xn; i > index; i--) {
                double j = i - index;
                double[] p_j = {0, 0};
                p_j[0] = p_hat[0] + j * (p_xn_cart[0] - p_hat[0]) / k;
                p_j[1] = p_hat[1] + j * (p_xn_cart[1] - p_hat[1]) / k;

                Point rectified = Calculations.Cart2Geo(inflection_point, p_j);
                trajectory.set(i, rectified);
            }
        }
        return trajectory;
    }
}
