package com.example.contest;

import android.graphics.Color;
import android.os.Bundle;

import com.example.contest.Common.CommonVar;
import com.example.contest.Utils.button.buttonCityOnClickListener;
import com.example.contest.Utils.tools.http.MappingTools;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChooseCityActivity extends AppCompatActivity {

    private Button quit;
private FrameLayout linearChooseCity;
private Button[] et=new Button[16];
private TextView tv;
public static int leastnum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_city);//引用布局
        linearChooseCity=findViewById(R.id.linearChooseCity);
        CommonVar.chosenName.clear();

        leastnum=CommonVar.num_of_k_virtual_city;

        tv=findViewById(R.id.leastCityNum);
        tv.setText("还需选择"+leastnum+"个城市");


        RelativeLayout.LayoutParams btParams = new  RelativeLayout.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        for(int i = 0; i< 14; i++){
            et[i]=new Button(ChooseCityActivity.this);
            et[i].setHint(CommonVar.cityName.get(i));
            btParams.width=350;
            btParams.height=150;
            et[i].setBackgroundResource(R.drawable.btn_bg);
            et[i].setOnClickListener(new buttonCityOnClickListener(i,et[i],tv,this));
            et[i].setBackgroundColor(Color.WHITE);
//           et[i].setWidth(10);
          //  btParams.set;
            btParams.setMargins(20+600*(i%2), 10+(i/2)*150, 10, 10);
            et[i].setLayoutParams(btParams);
            linearChooseCity.addView(et[i]);

        }

        quit=findViewById(R.id.quitcity);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i< CommonVar.num_of_k_virtual_city; i++) {
                    CommonVar.cityName.add(et[i].getText().toString());
                }
                String []s=new String[CommonVar.chosenName.size()];
                CommonVar.cityNameWithAnchorPoints.clear();
                for(int i=0;i<CommonVar.chosenName.size();i++) {
                    String ss = CommonVar.chosenName.get(i);
                    Log.e("cityname", ss);
                   MappingTools.getAnchorPoint(ss, "141201");
                }
                Log.e("cityNameWithAnchorPoints",CommonVar.cityNameWithAnchorPoints.toString());
                finish();
            }
        });

    }



}