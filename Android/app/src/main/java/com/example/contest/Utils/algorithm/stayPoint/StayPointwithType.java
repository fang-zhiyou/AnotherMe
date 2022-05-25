package com.example.contest.Utils.algorithm.stayPoint;

import com.example.contest.Utils.algorithm.geography.Point;

public class StayPointwithType extends StayPoint{
    public int type;
    public String typeGBK;

    @Override
    public String toString() {
        return "StayPointwithType{" +
                "long=" + longitude +
                " lati="+latitude+
                "type="+typeGBK+
                "}\n";
    }
    public StayPointwithType(double longitude, double latitude) {
        super(longitude,latitude);

        this.longitude=longitude;
        this.latitude=latitude;



    }
    public StayPointwithType(double longitude,double latitude, String typeGBK) {
        super(longitude, latitude);
        this.typeGBK=typeGBK;
    }

    public StayPointwithType(double longitude,double latitude, long arriveTime, long leaveTime) {
        super(longitude, latitude, arriveTime, leaveTime);
    }
    public StayPointwithType(StayPoint sp){
        super(sp);

    }


    public void setType(String type1){
        typeGBK=type1;
    }

}
