package com.example.contest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.example.contest.Utils.algorithm.geography.Point;
import com.example.contest.Utils.webview.InterceptingWebViewClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BrowerActivity extends AppCompatActivity {
    private   WebView mWebView;
    public static EditText tv_url;
    private Button btn_wv_back;
    private Button btn_wv_foreward;
    private Button btn_wv_flash;
    private void initWebView(){
        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        WebView.setWebContentsDebuggingEnabled(true);

        mWebSettings.setUseWideViewPort(true);//web1就是你自己定义的窗口对象。
        mWebSettings.setLoadWithOverviewMode(true);

        mWebSettings.setDomStorageEnabled(true);


        mWebView.setWebViewClient(new InterceptingWebViewClient(tv_url));
        //   mWebView.setWebViewClient(new WebViewClient());

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });
        mWebView.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent keyEvent){
                if((keyCode==KeyEvent.KEYCODE_BACK)&&mWebView.canGoBack()){
                    mWebView.goBack();
                    return true;
                }
                return  false;
            }
        });



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brower);
    //获得intent传递的信息;
        Intent intent=getIntent();
        String msg=intent.getStringExtra("js");
        JSONObject js;
        StringBuilder sb=new StringBuilder();
        String cityName="";
        ArrayList<Point> points = new ArrayList<Point>();
        String kvitual_one_city=msg;
        double longitude=116.4020;
        double latitude=32.9363;
        try {
            js = new JSONObject(kvitual_one_city);
            cityName=js.getString("cityName");
            JSONArray jsonArray=js.getJSONArray("one_city_poi");
            sb.append("城市名："+cityName+"\n");
            sb.append("虚拟映射点：\n");

            for(int j=0;j<jsonArray.length();j++){
               longitude=jsonArray.getJSONObject(j).getDouble("longitude");
                latitude=jsonArray.getJSONObject(j).getDouble("latitude");
                String type=jsonArray.getJSONObject(j).getString("type");
                points.add(new Point( longitude, latitude));

                sb.append("经度："+longitude+" 纬度："+latitude+" 类型："+type+"\n");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
Log.d("cityName",cityName);
        tv_url=(EditText) findViewById(R.id.text_url_0) ;

        mWebView=(WebView) findViewById(R.id.webview_2);
        initWebView();
        mWebView.loadUrl("https://caiyunapp.com/wx_share/?#"+longitude+","+latitude);

        btn_wv_back= findViewById(R.id.wv_back);
        btn_wv_foreward= findViewById(R.id.wv_foreward);
        btn_wv_flash= findViewById(R.id.wv_flash);

        btn_wv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mWebView.canGoBack()){
                    mWebView.goBack();
                }
            }
        });
        btn_wv_foreward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("forward","ddd");
                //urlload("http://www.caiyunapp.com/wx_share/?#116.4020,39.9363");
                //   mWebView.loadUrl();
                   mWebView.goForward();
            }
        });
        btn_wv_flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s_url=tv_url.getText().toString();
                Log.e("url",s_url);

                mWebView.loadUrl(s_url);
            }
        });


        Log.d("s","onCreate:webview");
    }
}