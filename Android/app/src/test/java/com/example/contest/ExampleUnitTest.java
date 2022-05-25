package com.example.contest;

import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

import com.example.contest.Utils.algorithm.TrajectorySimulator.TrajectorySimulator;
import com.example.contest.Utils.algorithm.geography.Calculations;
import com.example.contest.Utils.algorithm.geography.Point;
import com.example.contest.Utils.algorithm.stayPoint.StayPoint;

import java.util.ArrayList;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test_obfuscation() {
        Point p1_geo=new Point(116.303937,39.924058);
        Point p2_geo=new Point(116.334414,39.922863);

        double[] p1_cart = {0, 0};
        double[] p2_cart=Calculations.Geo2Cart(p1_geo, p2_geo);
//        double dis=Calculations.getDisFromCart(p1_cart,p2_cart);

        double[] insert_p_cart={0,0};
        insert_p_cart[0]=(p1_cart[0]+p2_cart[0])/2;
        insert_p_cart[1]=(p1_cart[1]+p2_cart[1])/2;


        Point insert_p_geo=Calculations.Cart2Geo(p1_geo,insert_p_cart);
        System.out.print(Double.toString(insert_p_geo.longitude)+","+Double.toString(insert_p_geo.latitude));
    }
    @Test
    public void test_shape_obfuscation() {
        Point p1_geo=new Point(116.303937,39.924058);
        Point p2_geo=new Point(116.310018,39.908059);

        ArrayList<Point> points=new ArrayList<>();
        points.add(p1_geo);
        points.add(p2_geo);

        TrajectorySimulator simulator = new TrajectorySimulator(points, points,60,2);
        ArrayList<Point> trajectory=simulator.trajectorySimulate();

        System.out.print("123");
    }
}