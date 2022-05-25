package com.example.contest.Utils.algorithm;

import android.util.Log;
import android.content.Context;
import android.widget.Toast;

import com.example.contest.Common.CommonVar;
import com.example.contest.Utils.algorithm.TrajectorySimulator.TrajectorySimulator;
import com.example.contest.Utils.algorithm.preprocess.DBSCAN;
import com.example.contest.Utils.algorithm.geography.Point;
import com.example.contest.Utils.algorithm.preprocess.CSVReader;
import com.example.contest.Utils.algorithm.preprocess.Preprocess;
import com.example.contest.Utils.algorithm.stayPoint.GetStayPoint;
import com.example.contest.Utils.algorithm.stayPoint.StayPoint;
import com.example.contest.Utils.file.WriteToFile;
import com.example.contest.Utils.tools.GPStoGaodeCoordinateConverter.GPStoGaode;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class Pipline {
    public static ArrayList<Point> processPoints=new ArrayList<>();
    public static void process(BufferedReader reader,Context context) throws Exception {
        ArrayList<Point> points= Preprocess.preprocess(reader,2000,10);
        ArrayList<StayPoint> stayPoints= GetStayPoint.getStayPoint(points);
        ArrayList<StayPoint> stayPointsGaode= GPStoGaode.getStayPoint(context,stayPoints);
        ArrayList<Point> pointsGaode=GPStoGaode.getPoint(context,points);


        Log.e("size",stayPoints.size()+" ");
        CommonVar.sp=stayPointsGaode;
        CommonVar.points=pointsGaode;
        Log.e("size CommonVar",CommonVar.sp.size()+" ");


        processPoints.addAll(pointsGaode);
        FileOutputStream out=null;
        try {
            out=context.openFileOutput("profile", Context.MODE_PRIVATE);
            WriteToFile.saveRealPoints(out);
            Toast.makeText(context,"导入成功",Toast.LENGTH_LONG).show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
