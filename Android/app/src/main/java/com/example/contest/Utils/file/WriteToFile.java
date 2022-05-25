package com.example.contest.Utils.file;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.contest.Common.CommonVar;
import com.example.contest.Utils.algorithm.geography.Point;
import com.example.contest.Utils.algorithm.preprocess.CSVReader;
import com.example.contest.Utils.algorithm.stayPoint.StayPoint;
import com.example.contest.Utils.algorithm.stayPoint.StayPointwithType;
import com.example.contest.Utils.tools.http.MappingTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.OpenOption;
import java.util.ArrayList;

public class WriteToFile {
    public static ArrayList<Point> readTrajectFile(BufferedReader reader) throws IOException {
        String line = "";
        ArrayList<Point> points = new ArrayList<Point>(); //保存文件中所有坐标点
        for (int i = 1; (line = reader.readLine()) != null; i++) {

            String[] datas = line.split(",");

            long timeStamp = Long.parseLong(datas[0]);
            double latitude = Double.parseDouble(datas[2]);
            double longitude = Double.parseDouble(datas[1]);

            points.add(new Point(timeStamp, longitude, latitude));
        }
        return points;
    }
    public static void showRealPoints(FileInputStream in){

        BufferedReader reader=null;
        StringBuilder content=new StringBuilder();

        try{

            reader=new BufferedReader(new InputStreamReader(in));
            String line="";
            while((line=reader.readLine())!=null){
                content.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JSONObject js;
        String name;
        String type;
        try {
            js = new JSONObject(content.toString());
            name = js.getString("pid");
            type = js.getString("type");
   //    JSONArray pointsarray = js.getJSONArray("AllRealpoints");
//            int lenofpointsarray = pointsarray.length();
//            ArrayList<Point> points = new ArrayList<>();
//            for (int i = 0; i < lenofpointsarray; i++) {
//                double longitude = pointsarray.getJSONObject(i).getDouble("longitude");
//                double lan = pointsarray.getJSONObject(i).getDouble("latitude");
//
//                points.add(new Point(longitude, lan));
//                Log.d("out points", "+" + longitude + " " + lan);
//
//
//            }
//            if (CommonVar.points == null || CommonVar.points.size() == 0)
//                CommonVar.points.addAll(points);


            JSONObject poi = js.getJSONObject("stay_point");
            int numarray = poi.getInt("spnum");
            JSONArray array = poi.getJSONArray("spvalue");


            ArrayList<StayPoint> listsp=new ArrayList<>();

            for(int i=0;i<numarray;i++){
                double longitude=array.getJSONObject(i).getDouble("longitude");
                double lan=array.getJSONObject(i).getDouble("latitude");
                long arrive=array.getJSONObject(i).getLong("arrive");
                long leave=array.getJSONObject(i).getLong("leave");
                StayPoint stayPoint=new StayPoint(longitude,lan,arrive,leave);

                listsp.add(stayPoint);
                Log.d("out", longitude + " " + lan);
            }
            if (CommonVar.sp == null || CommonVar.sp.size() == 0)
                CommonVar.sp.addAll(listsp);

            Log.d("jsobject", name);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Log.d("show data",name);


    }
    /**
     * 想直接返回ArrayList<String> 其中String包含有一个城市的所有信息
     *
     * **/
    public static ArrayList<String> showVirtulPoints(FileInputStream in){
        ArrayList<String> ans=new ArrayList<>();

        BufferedReader reader=null;
        StringBuilder content=new StringBuilder();

        try{

            reader=new BufferedReader(new InputStreamReader(in));
            String line="";
            while((line=reader.readLine())!=null){
                content.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JSONArray js;
        try {
            js = new JSONArray(content.toString());

            for(int i=0;i<js.length();i++){
                JSONObject cityK=js.getJSONObject(i);
                ans.add(cityK.toString());
                Log.d("decode",cityK.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Log.d("show data",name);


        return ans;

    }
    public static void saveKVirtualPoints(FileOutputStream out){
        BufferedWriter writer=null;




        JSONArray KvirtualProfile=new JSONArray();

        try {
            JSONObject js=new JSONObject();
            for(int i = 0; i< MappingTools.KvirtualmindisPoint.size(); i++) {
                String cityName=CommonVar.cityNameWithAnchorPoints.get(i).Cityname;
                JSONObject onevirtualProfile=new JSONObject();
                JSONArray spArray_of_one_city= new JSONArray();
                for (StayPointwithType spwt: MappingTools.KvirtualmindisPoint.get(i)) {
                    JSONObject tmp = new JSONObject();
                    tmp.put("longitude", spwt.longitude);
                    tmp.put("latitude", spwt.latitude);
                    tmp.put("type",spwt.typeGBK);


                    spArray_of_one_city.put(tmp);
                }
                int num_of_one_city_poi = MappingTools.KvirtualmindisPoint.size();
                onevirtualProfile.put("cityName",cityName);

                onevirtualProfile.put("num_of_one_city_poi", num_of_one_city_poi);

                onevirtualProfile.put("one_city_poi", spArray_of_one_city);

                KvirtualProfile.put(onevirtualProfile);
            }




            Log.e("js",KvirtualProfile.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try{

            writer=new BufferedWriter(new OutputStreamWriter(out));
            writer.write(KvirtualProfile.toString());
            if(writer!=null) writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void  saveRealPoints(FileOutputStream out){

        BufferedWriter writer=null;
        StringBuilder sb=new StringBuilder();
        JSONObject js=new JSONObject();
        JSONObject sp=new JSONObject();
        JSONArray spArray= new JSONArray();

        try {
            js.put("pid","profile_real_001");
            js.put("type","real");
            //js.put("city","beijing");
//            for(int i = 0; i< CommonVar.points.size(); i++){
//                JSONObject tmp=new JSONObject();
//                tmp.put("longitude",CommonVar.points.get(i).longitude);
//                tmp.put("latitude",CommonVar.points.get(i).latitude);
//                pointsArray.put(tmp);
//            }
            for(int i=0;i<CommonVar.sp.size();i++){
                JSONObject tmp=new JSONObject();
                tmp.put("longitude",CommonVar.sp.get(i).longitude);
                tmp.put("latitude",CommonVar.sp.get(i).latitude);
                tmp.put("arrive",CommonVar.sp.get(i).arriveTime);
                tmp.put("leave",CommonVar.sp.get(i).leaveTime);

                spArray.put(tmp);
            }
            int spnum=CommonVar.sp.size();
            sp.put("spnum",spnum);

            sp.put("spvalue",spArray);
            js.put("stay_point",sp);
            Log.e("js",js.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try{

            writer=new BufferedWriter(new OutputStreamWriter(out));
            writer.write(js.toString());
            if(writer!=null) writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }



}
