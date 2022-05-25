package com.example.contest.Utils.algorithm.TrajectorySimulator;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.contest.Utils.algorithm.geography.Point;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetNavTrajectories {//给定一个POI数组，返回他们的导航点

    //将坐标点格式化
    public static String parseLocation(Point point) {
        return String.format("%.6f,%.6f", point.longitude, point.latitude);
    }

    //将json返回的polyline转为Point
    public static ArrayList<Point> parsePolyline(String polyline) {
        ArrayList<Point> trajectory = new ArrayList<>();
        String[] stringPoints = polyline.split(";");
        for (String stringPoint : stringPoints) {
            String[] coordination = stringPoint.split(",");
            trajectory.add(new Point(0, Double.parseDouble(coordination[0]), Double.parseDouble(coordination[1])));
        }
        return trajectory;
    }

    public static ArrayList<Point> parseJson(String jsonString) {
        JSONObject jsonobj = JSON.parseObject(jsonString);
        JSONObject path = jsonobj.getJSONObject("route")
                .getJSONArray("paths").getJSONObject(0);
        JSONArray steps = path.getJSONArray("steps");
        ArrayList<Point> trajectory = new ArrayList<>();

        for (int i = 0; i < steps.size(); i++) {
            JSONObject step = steps.getJSONObject(i);
            String polyline = step.getString("polyline");
//            Log.e("json", "json");
            trajectory.addAll(parsePolyline(polyline));
        }

        return trajectory;
    }

    public static ArrayList<Point> get2points(Point origin, Point destination, int transport_type, String key)throws Exception {
        String url = "https://restapi.amap.com/v3/direction/";
        switch (transport_type) {
            case 0:
                url += "walking";
                break;
            case 1:
                url += "bicycling";
                break;
            case 2:
                url += "driving";
                break;
        }

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("key", key);
        urlBuilder.addQueryParameter("origin", parseLocation(origin));
        urlBuilder.addQueryParameter("destination", parseLocation(destination));
        if (transport_type == 2) {
            urlBuilder.addQueryParameter("extensions", "all");
        }


        try {
            url = URLDecoder.decode(urlBuilder.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Response response = null;
        try {
            response = HttpRequest.get(url);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        ArrayList<Point> path = new ArrayList<>();
        try {
            path = parseJson(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path;
    }

    public static ArrayList<Point> getNavTrajectories(ArrayList<Point> points, int transport_type) throws Exception{
        ArrayList<Point> trajectory = new ArrayList<>();
        for (int i = 0; i < points.size() - 1; i++) {
            ArrayList<Point> path = get2points(points.get(i), points.get(i + 1), transport_type, "2d7f78022ae107ed310c5c23a4d89ef7");
            trajectory.addAll(path);
        }

        return trajectory;
    }
}
