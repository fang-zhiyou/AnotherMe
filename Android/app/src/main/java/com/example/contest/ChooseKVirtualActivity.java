package com.example.contest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.contest.Common.CommonVar;
import com.example.contest.Utils.algorithm.geography.Point;
import com.example.contest.Utils.file.WriteToFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ChooseKVirtualActivity extends AppCompatActivity {

    private TextView tv;
    private TextView[] bts = new TextView[10];
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_kvirtual);
        tv = findViewById(R.id.tv_ddd);

        linearLayout = findViewById(R.id.choosekv_line);
        LinearLayout.LayoutParams btParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        FileInputStream in=null;
        ArrayList<String> kvirtual=new ArrayList<>();
        try {


            in=openFileInput("vup");
            kvirtual=WriteToFile.showVirtulPoints(in);
            Log.d("kvirtual",kvirtual.toString());

        }catch (Exception e){
            e.printStackTrace();
        }
        for (int i = 0; i < kvirtual.size(); i++) {
            bts[i] = new TextView(getApplicationContext());
            bts[i].setId(4000 + i);

            JSONObject js;
            StringBuilder sb=new StringBuilder();
            ArrayList<Point> points = new ArrayList<Point>();
            try {
                js = new JSONObject(kvirtual.get(i).toString());
                String cityName=js.getString("cityName");
                JSONArray jsonArray=js.getJSONArray("one_city_poi");
                sb.append("城市名："+cityName+"\n");
                sb.append("虚拟映射点：\n");

                for(int j=0;j<jsonArray.length();j++){
                    double longitude=jsonArray.getJSONObject(j).getDouble("longitude");
                    double latitude=jsonArray.getJSONObject(j).getDouble("latitude");
                    String type=jsonArray.getJSONObject(j).getString("type");
                    points.add(new Point(0,longitude, latitude));

                    sb.append("经度："+longitude+" 纬度："+latitude+" 类型："+type+"\n");

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("kvirtual",sb.toString());

            bts[i].setText(sb.toString());

            // btParams.width=linearLayout.getWidth()/;
            bts[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), AMapActivity.class);//打开新activity：

                    ArrayList<Point> trajectory=new ArrayList<>(points);

                    double longituduList[]=new double[trajectory.size()];
                    double laitiduList[]=new double[trajectory.size()];
                    long timeList[]=new long[trajectory.size()];
                    for(int i=0;i<trajectory.size();i++){
                        longituduList[i]=trajectory.get(i).longitude;
                        laitiduList[i]=trajectory.get(i).latitude;
                        timeList[i]=trajectory.get(i).timestamp;
                    }

                    intent.putExtra("long",longituduList);
                    intent.putExtra("lati",laitiduList);
                    intent.putExtra("time",timeList);


                     startActivity(intent);
                }
            });
            btParams.width = 1000;
            btParams.height = 600;
            bts[i].setTextColor(Color.BLACK);
            bts[i].setWidth(10);
            bts[i].setLayoutParams(btParams);
            linearLayout.addView(bts[i]);
        }

    }
}