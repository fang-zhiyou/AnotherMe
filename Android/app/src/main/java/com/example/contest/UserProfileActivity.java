package com.example.contest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.contest.Common.CommonVar;
import com.example.contest.Utils.algorithm.geography.Point;
import com.example.contest.Utils.algorithm.stayPoint.StayPoint;
import com.example.contest.Utils.algorithm.stayPoint.StayPointwithType;
import com.example.contest.Utils.file.WriteToFile;
import com.example.contest.Utils.tools.http.MappingTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Button calcu = (Button) findViewById(R.id.calcuvirtual);
        Button savedata = (Button) findViewById(R.id.savedata);
        Button showdata = (Button) findViewById(R.id.showdata);
        Button genvitual = (Button) findViewById(R.id.getvirtual);
        Button getType = (Button) findViewById(R.id.getType);
        Button save_virtual = (Button) findViewById(R.id.save_virtual);
        Button virtual_tragectry = (Button) findViewById(R.id.virtual_traject);
        virtual_tragectry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), CheckPermissionsActivity.class);
                startActivity(intent);

            }
        });
        save_virtual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //虚拟点需要保存吗，待定

                FileOutputStream out = null;
                BufferedWriter writer = null;
                StringBuilder sb = new StringBuilder();
                JSONObject js = new JSONObject();
                JSONObject sp = new JSONObject();
                JSONArray spArray = new JSONArray();

                try {
                    js.put("pid", "profile_real_001");
                    js.put("type", "virtual");

                    js.put("city", "beijing");
                    for (int i = 0; i < MappingTools.KvirtualmindisPoint.size(); i++) {
//                        JSONObject tmp = new JSONObject();
//                        tmp.put("longitude", UseTypeGetsCityPois.KvirtualmindisPoint.get(i).get(i)longitude);
//                        tmp.put("latitude", UseTypeGetsCityPois.mindisPoint.get(i).latitude);
//                        tmp.put("type", UseTypeGetsCityPois.mindisPoint.get(i).type);
//

//                        spArray.put(tmp);
                    }
                    int spnum = CommonVar.sp.size();
                    sp.put("spnum", spnum);
                    sp.put("spvalue", spArray);
                    //sp.put()
                    js.put("stay_point", sp);
                    Log.e("js", js.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    out = openFileOutput("vup", Context.MODE_PRIVATE);
                    writer = new BufferedWriter(new OutputStreamWriter(out));
                    writer.write(js.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (writer != null) writer.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        calcu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  UseTypeGetsCityPois.caltruepoint(getApplicationContext());
            }
        });
        getType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    for(int i=0;i<CommonVar.sp.size();i++){
                        StayPoint sp1=CommonVar.sp.get(i);
                        StayPointwithType spwt=new StayPointwithType(sp1);

                        CommonVar.spwType.add(MappingTools.getTypeSynchronized(sp1,i));
                    }
                    Toast.makeText(getApplicationContext(),"成功获取type",Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });
        genvitual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), ChooseKVirtualActivity.class);
                startActivity(intent);


            }
        });

        savedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FileOutputStream out=null;
                try {
                    out=openFileOutput("profile", Context.MODE_PRIVATE);
                    WriteToFile.saveRealPoints(out);
                    Toast.makeText(getApplicationContext(),"保存成功",Toast.LENGTH_LONG).show();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        showdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileInputStream in=null;
                try {
                    in=openFileInput("profile");
                    WriteToFile.showRealPoints(in);

                    in=openFileInput("vup");
                    WriteToFile.showVirtulPoints(in);

                }catch (Exception e){
                    e.printStackTrace();
                }


                Toast.makeText(getApplicationContext(), "加载成功", Toast.LENGTH_LONG).show();
            }
        });


    }
}