package com.example.contest.Utils.tools.http;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpRequest {
    public static Response get(String url) throws Exception{
        try {
            //创建OkHttpClient对象
            OkHttpClient client = new OkHttpClient();
            //创建Request
            Request request = new Request.Builder()
                    .url(url)//访问连接
                    .get()
                    .build();
            //创建Call对象
            Call call = client.newCall(request);
            //通过execute()方法获得请求响应的Response对象
            Response response = call.execute();
            if (response.isSuccessful()) {
                Log.e("response", response.toString());

                return response;
            }else{
                throw new Exception("network error");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
