package com.example.contest.Utils.tools.GPStoGaodeCoordinateConverter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.amap.api.maps.model.LatLng;
import com.example.contest.Utils.algorithm.geography.Point;
import com.example.contest.Utils.algorithm.stayPoint.StayPoint;

import java.util.ArrayList;

public class GPStoGaode {
    public static ArrayList<StayPoint> getStayPoint(Context context, ArrayList<StayPoint> pointslist) throws Exception {
        CoordinateConverter converter  = new CoordinateConverter(context.getApplicationContext());
// CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.GPS);
// sourceLatLng待转换坐标点 LatLng类型
        ArrayList<StayPoint> ans=new ArrayList<>();
        for(StayPoint sp:pointslist){
            DPoint sourceLatLng=new DPoint(sp.latitude,sp.longitude);

            converter.coord(sourceLatLng);
            // 执行转换操作
            DPoint desLatLng = converter.convert();
            double splong= ((int)(sp.longitude*1000000))/1000000.0;
            double splan= ((int)(sp.latitude*1000000))/1000000.0;
            double dessplong= ((int)(desLatLng.getLongitude()*1000000))/1000000.0;
            double dessplan= ((int)(desLatLng.getLatitude()*1000000))/1000000.0;
            ans.add(new StayPoint(dessplong,dessplan,sp.arriveTime,sp.leaveTime));
//            Log.d("gps",splong+" "+splan);
//            Log.d("gaode",dessplong+" "+ dessplan);
        }
        return ans;

      //  Log.d("gps gaode",desLatLng.getLongitude()+" "+ desLatLng.getLatitude());

    }
    public static ArrayList<Point> getPoint(Context context, ArrayList<Point> pointslist) throws Exception {
        CoordinateConverter converter  = new CoordinateConverter(context.getApplicationContext());
// CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.GPS);
// sourceLatLng待转换坐标点 LatLng类型
        ArrayList<Point> ans=new ArrayList<>();
        for(Point sp:pointslist){
            DPoint sourceLatLng=new DPoint(sp.latitude,sp.longitude);
            converter.coord(sourceLatLng);
            // 执行转换操作
            DPoint desLatLng = converter.convert();
            ans.add(new Point(desLatLng.getLongitude(),desLatLng.getLatitude()));

        }
        return ans;
        //  Log.d("gps gaode",desLatLng.getLongitude()+" "+ desLatLng.getLatitude());

    }

}
