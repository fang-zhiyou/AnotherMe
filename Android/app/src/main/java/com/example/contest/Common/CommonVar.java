package com.example.contest.Common;

import com.example.contest.Utils.algorithm.geography.Point;
import com.example.contest.Utils.algorithm.stayPoint.CityNameWithAnchorPoint;
import com.example.contest.Utils.algorithm.stayPoint.StayPoint;
import com.example.contest.Utils.algorithm.stayPoint.StayPointwithType;

import java.util.ArrayList;
import java.util.Arrays;

public class CommonVar {
    public static String token="";
    public static String usr_id="";
    public static int num_of_k_virtual_city =3;
    public static ArrayList<String> cityName=new ArrayList<String>(Arrays.asList(new String[]{
            "成都","上海","广州","深圳","武汉",
            "南京","杭州","惠州","厦门","海口"
            ,"苏州","石家庄","东莞","珠海","福州","郑州"}));
    public static ArrayList<String> chosenName=new ArrayList<>();
    public static ArrayList<CityNameWithAnchorPoint> cityNameWithAnchorPoints=new ArrayList<>();
    public static ArrayList<String> poi=new ArrayList<>();
    public static ArrayList<StayPoint> sp=new ArrayList<>();
    public static ArrayList<Point> points=new ArrayList<>();
    public static ArrayList<StayPointwithType> spwType=new ArrayList<>();


    public static ArrayList<Point> trajectory;

}
