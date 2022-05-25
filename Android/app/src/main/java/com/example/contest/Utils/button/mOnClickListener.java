package com.example.contest.Utils.button;



import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.example.contest.Common.CommonUrl;
import com.example.contest.Utils.tools.http.HttpRequest;
import com.example.contest.Utils.algorithm.stayPoint.CityNameWithAnchorPoint;
import com.example.contest.Common.CommonVar;
import com.example.contest.Utils.algorithm.stayPoint.StayPointwithType;
import com.example.contest.Utils.tools.http.MappingTools;
import com.example.contest.Utils.webview.UrlTools;
import com.example.contest.ui.DashboardFragment;

//重写了 OnClickListener，用于动态添加button时传入参数，监听点击事件
public class mOnClickListener implements View.OnClickListener{
    int i;
    Button btn[];
    WebView mWebView;


    public mOnClickListener(int a, Button button[],WebView webView){
        i=a;
        btn=button;
        mWebView=webView;

    }
    @Override
    public void onClick(View view) {
        //全新的方法在public String generateReplaceUrl(int i)
      //  DashboardFragment.urlload(replaceurl);
        //mWebView.loadUrl("http://baidu.com");
    }
}

