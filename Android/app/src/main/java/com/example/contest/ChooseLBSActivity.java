package com.example.contest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.contest.Common.CommonVar;

public class ChooseLBSActivity extends AppCompatActivity {
    private Button bt0;
    private Button bt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_lbsactivity);
        bt0=findViewById(R.id.caiyun);
        bt1=findViewById(R.id.windy);
        if(CommonVar.lbs_id==0){
            bt0.setBackgroundResource(R.drawable.btn_bg);
            bt1.setBackgroundResource(R.drawable.btn_bg_unpress);


        }else {
            bt1.setBackgroundResource(R.drawable.btn_bg);
            bt0.setBackgroundResource(R.drawable.btn_bg_unpress);

        }
        bt0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonVar.lbs_id=0;
                bt0.setBackgroundResource(R.drawable.btn_bg);
                bt1.setBackgroundResource(R.drawable.btn_bg_unpress);

            }
        });

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonVar.lbs_id=1;
                bt1.setBackgroundResource(R.drawable.btn_bg);
                bt0.setBackgroundResource(R.drawable.btn_bg_unpress);
            }
        });

    }
}