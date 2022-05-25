package com.example.contest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.contest.Common.CommonVar;
import com.example.contest.Utils.algorithm.geography.Point;
import com.example.contest.Utils.button.buttonCityOnClickListener;
import com.example.contest.Utils.file.WriteToFile;
import com.example.contest.Utils.tools.http.MappingTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * 打开浏览器
 * */
public class ChooseVirtualActivity extends AppCompatActivity {
    private Button[] et=new Button[16];
    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_virtual);

        frameLayout=findViewById(R.id.linearChooseProfile);

        FileInputStream in=null;
        //读取vup文件实时初始化
        ArrayList<String> kvirtual=new ArrayList<>();
        try {


            in=openFileInput("vup");
            kvirtual= WriteToFile.showVirtulPoints(in);
            Log.d("kvirtual",kvirtual.toString());

        }catch (Exception e){
            e.printStackTrace();
        }

        RelativeLayout.LayoutParams btParams = new  RelativeLayout.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        for(int i = 0; i< kvirtual.size(); i++){
            JSONObject js;
            StringBuilder sb=new StringBuilder();
            String cityName="";
            ArrayList<Point> points = new ArrayList<Point>();
            String kvitual_one_city=kvirtual.get(i);
            try {
                js = new JSONObject(kvitual_one_city);
                cityName=js.getString("cityName");
                JSONArray jsonArray=js.getJSONArray("one_city_poi");
                sb.append("城市名："+cityName+"\n");
                sb.append("虚拟映射点：\n");

                for(int j=0;j<jsonArray.length();j++){
                    double longitude=jsonArray.getJSONObject(j).getDouble("longitude");
                    double latitude=jsonArray.getJSONObject(j).getDouble("latitude");
                    String type=jsonArray.getJSONObject(j).getString("type");
                    points.add(new Point( longitude, latitude));

                    sb.append("经度："+longitude+" 纬度："+latitude+" 类型："+type+"\n");

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("kvirtual",sb.toString());
            et[i]=new Button(this);
            et[i].setHint("使用"+cityName+"访问");

            btParams.width=450;
            btParams.height=150;
            et[i].setBackgroundResource(R.drawable.btn_bg);
            et[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("js",kvitual_one_city);
                    Intent intent=new Intent(ChooseVirtualActivity.this,BrowerActivity.class);
                    intent.putExtra("js",kvitual_one_city);
                    startActivity(intent);
                }
            });
            et[i].setBackgroundColor(Color.WHITE);
//           et[i].setWidth(10);
            //  btParams.set;
            btParams.setMargins(20+600*(i%2), 10+(i/2)*150, 10, 10);
            et[i].setLayoutParams(btParams);
            frameLayout.addView(et[i]);

        }

    }
}