package com.example.contest.Utils.button;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.contest.ChooseCityActivity;

import com.example.contest.ChooseCityActivity;
import com.example.contest.Common.CommonVar;
import com.example.contest.Utils.tools.http.MappingTools;


public class buttonCityOnClickListener implements View.OnClickListener{
    boolean hasPutdown;
    int i;
    Button btn;
    TextView tv;
    Activity ac;

    public buttonCityOnClickListener(int a, Button button, TextView textview, Activity activity){
        ac=activity;
        i=a;
        btn=button;
        tv=textview;
        hasPutdown=false;
    }
    @Override
    public void onClick(View view) {
        btn.setTextColor(Color.WHITE);
        if(hasPutdown==false&&ChooseCityActivity.leastnum>0) {
            hasPutdown=true;
            ChooseCityActivity.leastnum--;
            CommonVar.chosenName.add(CommonVar.cityName.get(i));
            if (ChooseCityActivity.leastnum == 0) {

                CommonVar.cityNameWithAnchorPoints.clear();
                StringBuffer sb = new StringBuffer();

                for(int i = 0; i<CommonVar.num_of_k_virtual_city; i++){
                    String ss = CommonVar.chosenName.get(i);
                    Log.e("cityname", ss);
                    sb.append(ss+"\n");

                    MappingTools.getAnchorPoint(ss, "141201");
                }
                dialog(sb.toString());

               // Toast.makeText(ac, sb.toString(), Toast.LENGTH_LONG).show();


            }
            tv.setText("还需选择"+ ChooseCityActivity.leastnum +"个城市");
            btn.setBackgroundColor(Color.parseColor("#0084ff"));
        }
    }
    void dialog(String sb){
        AlertDialog.Builder ab=new AlertDialog.Builder(btn.getContext());
        ab.setTitle("确定你选择的城市");
        ab.setMessage(sb.toString());
        ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ac.finish();
            }
        });
        ab.show();

    }
}
