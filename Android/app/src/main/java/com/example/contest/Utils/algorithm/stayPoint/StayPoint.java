package com.example.contest.Utils.algorithm.stayPoint;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.contest.Utils.algorithm.geography.Point;

public class StayPoint extends Point{
    public long arriveTime;
    public long leaveTime;


    @Override
    public String toString() {

        return super.toString();
    }
    public StayPoint(double longitude,double latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public StayPoint(Point point, long arriveTime, long leaveTime){
        this.longitude = point.longitude;
        this.latitude = point.latitude;
        this.arriveTime = arriveTime;
        this.leaveTime = leaveTime;

    }
    public StayPoint(StayPoint sp){
        latitude=sp.latitude;
        longitude=sp.longitude;
        arriveTime=sp.arriveTime;
        leaveTime=sp.leaveTime;
    }
    public StayPoint(double longitude,double latitude, long arriveTime, long leaveTime){
        this.longitude = longitude;
        this.latitude = latitude;
        this.arriveTime = arriveTime;
        this.leaveTime = leaveTime;

    }
}
