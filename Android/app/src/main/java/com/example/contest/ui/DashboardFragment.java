package com.example.contest.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.contest.MainActivity;
import com.example.contest.Utils.algorithm.stayPoint.CityNameWithAnchorPoint;
import com.example.contest.Common.CommonVar;
import com.example.contest.R;
import com.example.contest.Utils.algorithm.stayPoint.StayPointwithType;
import com.example.contest.Utils.tools.http.HttpRequest;
import com.example.contest.Utils.tools.http.MappingTools;
import com.example.contest.Utils.webview.UrlTools;
import com.example.contest.Utils.webview.InterceptingWebViewClient;

public class DashboardFragment extends Fragment {


    public  WebView mWebView;
    public static EditText tv_url;
    private Button btn_wv_back;
    private Button btn_wv_foreward;
    private Button btn_wv_flash;

    private LinearLayout linearLayout;
    private Button[] btn_list=new Button[16];


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("s","onCreateView");
        ((MainActivity) getActivity()).setActionBarTitle("Another Me");


        return inflater.inflate(R.layout.fragment_dashboard,container,false);
    }
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


    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("s","onCreate");
        super.onActivityCreated(savedInstanceState);




        tv_url=(EditText) getActivity().findViewById(R.id.text_url_0) ;

        mWebView=(WebView) getActivity().findViewById(R.id.webview_0);
        initWebView();
        mWebView.loadUrl("https://caiyunapp.com/wx_share/?#116.4020,32.9363");

        btn_wv_back= getActivity().findViewById(R.id.wv_back);
        btn_wv_foreward= getActivity().findViewById(R.id.wv_foreward);
        btn_wv_flash= getActivity().findViewById(R.id.wv_flash);

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
                urlload("http://www.caiyunapp.com/wx_share/?#116.4020,39.9363");
             //   mWebView.loadUrl();
       //   mWebView.goForward();
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


        linearLayout=getActivity().findViewById(R.id.l);
        LinearLayout.LayoutParams btParams = new  LinearLayout.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        for(int btn_i = 0; btn_i < CommonVar.num_of_k_virtual_city +1; btn_i++){
            btn_list[btn_i]=new Button(getContext());
            btn_list[btn_i].setId(2000+ btn_i);
            btn_list[btn_i].setText(Integer.toString(btn_i +1));
           // btParams.width=linearLayout.getWidth()/;
            Log.d("width",mWebView.getWidth()+"");
            btParams.width=1100/(CommonVar.num_of_k_virtual_city +1);
            btParams.height=140;

            btn_list[btn_i].setTextColor(Color.WHITE);
            btn_list[btn_i].setWidth(10);
            btn_list[btn_i].setLayoutParams(btParams);

            setOnClick(btn_list[btn_i],btn_i,generateReplaceUrl(btn_i));

            linearLayout.addView(btn_list[btn_i]);

        }

    }
//
    private void setOnClick(final Button btn,final int btn_ii,final String url){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                //   btn_wv_foreward.performClick();

                    for (int i1 = 0; i1 < CommonVar.num_of_k_virtual_city + 1; i1++) {
                        btn_list[i1].setTextColor(Color.WHITE);
                    }

                    btn_list[btn_ii].setTextColor(Color.BLUE);
                    urlload(url);

                    // Do whatever you want(str can be used here)

                }
            });
    }

    void urlload(String url){
        //mWebView.clearCache(true);
        mWebView.clearView();
        mWebView.stopLoading();

        mWebView.loadUrl("about:blank");
      //  mWebView.loadData(url);
        Log.e("startloadingurl",url);
        mWebView.loadUrl(url);
    }
    public String generateReplaceUrl(int i){
        String replaceurl="";

//        if(CommonUrl.url==null) {
//            CommonUrl.url=Http.buildCaiyun(103.933711, 30.749622);
//            //return;
//        }
//        else {
//            url=CommonUrl.url;
         replaceurl= UrlTools.buildCaiyun(103.933711, 30.749622);
//        }
        if(i==0){

            if(CommonVar.sp!=null&CommonVar.sp.size()!=0){
                double longitude=CommonVar.sp.get(0).longitude;
                double latitude=CommonVar.sp.get(0).latitude;

                replaceurl= UrlTools.buildCaiyun(longitude,latitude);
            }
            //后台请求虚拟点,不做任何处理
            if (MappingTools.KvirtualmindisPoint != null && MappingTools.KvirtualmindisPoint.size() != 0) {
                int size = MappingTools.KvirtualmindisPoint.size();
                Toast.makeText(getContext(), "已发送虚拟请求",Toast.LENGTH_LONG).show();

                for(int j=0;j< size;j++) {
                    StayPointwithType ans = MappingTools.KvirtualmindisPoint.get(j).get(0);
                    Toast.makeText(getContext(), "已发送虚拟请求"+ans.longitude+" "+ans.latitude+"",Toast.LENGTH_LONG).show();
                    String Vurl= UrlTools.buildCaiyun(ans.longitude, ans.latitude);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HttpRequest.get(Vurl);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    // Toast.makeText(view.getContext(), ans.longitude+" "+ans.latitude+"",Toast.LENGTH_LONG).show();

                }
            } else {

                    replaceurl = UrlTools.buildCaiyun(103.9319, 30.7559);
                    //Toast.makeText(view.getContext(), ans.longitude+" "+ans.latitude+"",Toast.LENGTH_LONG).show();
            }


        }else {
            Log.e("id", Integer.toString(i));

            if (MappingTools.KvirtualmindisPoint != null && MappingTools.KvirtualmindisPoint.size() != 0) {
                int size = MappingTools.KvirtualmindisPoint.size();
                if (i  < size) {
                    StayPointwithType ans = MappingTools.KvirtualmindisPoint.get(i).get(0);

                    replaceurl =UrlTools.buildCaiyun(ans.longitude, ans.latitude);

                }
            } else {
                if(i-1<CommonVar.cityNameWithAnchorPoints.size()) {
                    CityNameWithAnchorPoint ans = CommonVar.cityNameWithAnchorPoints.get(i-1);
                    replaceurl = UrlTools.buildCaiyun(ans.longitude, ans.latitude);
                    //Toast.makeText(view.getContext(), ans.longitude+" "+ans.latitude+"",Toast.LENGTH_LONG).show();
                }
            }

        }
        Log.e("replaceUrl", replaceurl);

        DashboardFragment.tv_url.setText(replaceurl);


        return replaceurl;
    }



    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("s","onStart");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("s","onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("s","onResume");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
