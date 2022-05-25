package com.example.contest.Utils.algorithm.preprocess;

import com.example.contest.Utils.algorithm.preprocess.CSVReader;
import com.example.contest.Utils.algorithm.geography.Point;
import com.example.contest.Utils.algorithm.preprocess.DBSCAN;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class Preprocess {
    public static ArrayList<Point> preprocess(BufferedReader reader, double eps, int minPts) {

        ArrayList<Point> points = null;
        try {
            points = CSVReader.read(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        DBSCAN dbscan=new DBSCAN(points,eps,minPts);
//        points.removeAll(dbscan.noise);

        return points;
    }
}
