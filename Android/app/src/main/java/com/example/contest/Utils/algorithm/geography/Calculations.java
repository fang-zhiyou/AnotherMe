package com.example.contest.Utils.algorithm.geography;

import java.util.ArrayList;
import java.util.Random;

//包含一些地理位置计算的静态方法
public class Calculations {

    //计算两个经纬度坐标系的距离
    public static double getDistFromGeo(Point p1, Point p2) {
        double EARTH_RADIUS = 6378.137;// 地球半径,单位千米
        double radLat1 = Math.toRadians(p1.latitude);
        double radLat2 = Math.toRadians(p2.latitude);
        double a = radLat1 - radLat2;// 两点纬度差
        double b = Math.toRadians(p1.longitude) - Math.toRadians(p2.longitude);// 两点的经度差
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s * 1000;
    }

    public static Point computeMeanCoord(ArrayList<Point> points, int i, int j) {
        Point coordination = new Point();
        double longitude = 0, latitude = 0;
        for (int k = i; k <= j; k++) {
            longitude += points.get(k).longitude;
            latitude += points.get(k).latitude;
        }
        coordination.longitude = longitude / (j - i + 1);
        coordination.latitude = latitude / (j - i + 1);
        return coordination;
    }

    //根据距离计算出纬度的差值
    public static double getLatDeltaByDis(double dis) {
        return Math.abs(dis / (111000));
    }

    //根据距离计算出经度的差值
    public static double getLngDeltaByDis(double lat, double dis) {
        return Math.abs(dis / (111000 * Math.cos(Math.toRadians(Math.abs(lat)))));
    }

    //从笛卡尔坐标系获取距离
    public static double getDisFromCart(double[] p1, double[] p2) {
        return Math.sqrt(Math.pow(p1[0] - p2[0], 2) + Math.pow(p2[1] - p1[1], 2));
    }

    //将笛卡尔坐标系转换为地球坐标系
    public static Point Cart2Geo(Point origin, double[] p1) {
        double delta_lng = getLngDeltaByDis(origin.latitude, p1[0]);
        double delta_lat = getLatDeltaByDis(p1[1]);
        double lng = origin.longitude + (p1[0] > 0 ? delta_lng : -delta_lng);
        double lat = origin.latitude + (p1[1] > 0 ? delta_lat : -delta_lat);
        Point point = new Point(0, lng, lat);
        return point;
    }

    //将地球坐标系转换为笛卡尔坐标系
    public static double[] Geo2Cart(Point origin, Point point) {
        double delta_lng = Math.abs(origin.longitude - point.longitude);
        double delta_lat = Math.abs(origin.latitude - point.latitude);
        double dis = Calculations.getDistFromGeo(origin, point);
        double theta1 = Math.atan(delta_lat / delta_lng);
        double theta2 = Math.atan(delta_lng / delta_lat);
        double x = dis * Math.sin(theta2);
        double y = dis * Math.sin(theta1);
        double[] coor = new double[2];
        coor[0] = point.longitude > origin.longitude ? x : -x;
        coor[1] = point.latitude > origin.latitude ? y : -y;
        return coor;
    }

    public static double gaussianRandom(double mu, double sigma_square) {
        Random r = new Random();
        return Math.sqrt(sigma_square) * r.nextGaussian() + mu;
    }

    //计算向量p0->p1,p0->p2夹角，注意向量方向。角度单位，rad
    public static double getAngleFrom3GeoPoints(Point p0,Point p1,Point p2){
        double[] o={0,0};
        double[] a=Geo2Cart(p0,p1);
        double[] b=Geo2Cart(p0,p1);
        double angle=Math.acos((a[0]*b[0]+a[1]*b[1])/(getDisFromCart(o,a)+getDisFromCart(o,b)));
        return angle;
    }
}
