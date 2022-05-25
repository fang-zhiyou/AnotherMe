package com.example.contest.Utils.tools.http;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class OkHttpCallback implements Callback {
    private final String TAG = OkHttpCallback.class.getSimpleName();

    public String url;
    public String result;

    /**
     * 接口调用失败回调此方法
     * @param call
     * @param e
     */
    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//       Log.d(TAG,"url:" + url);
//       Log.d(TAG,"请求失败" + e.toString());
//       onFinish("failure",result);
//
//       if (e instanceof SocketTimeoutException) {
//           //判断超时异常
//
//
//       }
//       if (e instanceof ConnectException) {
//           //连接异常
//
//       }


    }

    /**
     * 接口调用成功回调此方法
     * @param call
     * @param response  接口返回的数据
     * @throws IOException
     */
    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        Log.d(TAG,"url:" + url);
        result = Objects.requireNonNull(response.body()).string();

        Log.d(TAG,"请求成功" + result);
        try {
            onFinish("success",result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String onFinish(String status, String msg) throws JSONException {
        Log.d(TAG,"url:" + url + "status:" + status);
        return status;
    }


}

