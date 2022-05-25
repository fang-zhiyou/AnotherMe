package com.example.contest.Utils.webview;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class InterceptingWebViewClient extends WebViewClient {
    TextView tv_url;


    public InterceptingWebViewClient(TextView tv){
        tv_url=tv;
    }

//    @Override
//    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//        Log.d("detail",request.getUrl().toString());
//        return false;
//    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {

        Log.d("e222",url);
        tv_url.setText(url);
        if(UrlTools.ifUrlHasLocation(url)){
            String locdetail= UrlTools.locationDetails(url);
            Log.d("detail",locdetail);
        }


        super.onPageStarted(view, url, favicon);
    }

}
