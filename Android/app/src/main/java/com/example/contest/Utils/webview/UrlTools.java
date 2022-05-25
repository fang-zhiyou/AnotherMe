package com.example.contest.Utils.webview;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.example.contest.Common.CommonUrl;
import com.example.contest.Common.CommonVar;
import com.example.contest.R;

import java.util.Random;

public class UrlTools {

    public static boolean ifUrlHasLocation(String url){

        if (url.contains("location")) {
          //  CommonUrl.url=new String(url);
            return true;
        }else return false;

    }
    public static String locationDetails(String url){
        String loc;

        if (url.contains("ulocation=")) {

            loc=url.substring(url.indexOf("ulocation="),url.indexOf("ulocation=")+30);

          return loc;
        }
        return null;

    }
    public static String buildWindyUrl(double longti,double lanti){
        double splong= ((int)(longti*100))/100.0;
        double splan= ((int)(lanti*100))/100.0;
        String url="https://www.windy.com/?"+splan+","+splong;
        return url;
    }
    public static String replaceCtrip(String url,double longti,double lanti){
//        double splong= ((int)(longti*10000))/10000.0;
//        double splan= ((int)(lanti*10000))/10000.0;

        url=url.replaceAll("&ulocation=.*&ucity=","&ulocation="+lanti+"_"+longti+"&ucity=");
        url=url.replaceAll("&ulat=.*&ulon","&ulat="+lanti+"&ulon");
        url=url.replaceAll("&ulon=.*&geo","&ulon="+longti+"&geo");
        return url;
    }


    public static String buildCaiyun(double longti,double lanti){


        double splong= ((int)(longti*10000))/10000.0;
        double splan= ((int)(lanti*10000))/10000.0;
        String url="";
        Random r=new Random();

        url="http://www.caiyunapp.com/wx_share/?"+r.nextInt(100)+"?#"+splong+","+splan+"/";

        return url;
    }



    public static String replaceUrl(double longti,double lanti){
//        StringBuilder url=new StringBuilder("https://www.booking.com/searchresults.html?src=index&rows=20&&label=gen173bo-1DCAEoggI46AdIM1gDaDGIAQKYATG4AQnIARHYAQPoAQH4AQKIAgGYAgKoAgO4AqHJqpIGwAIB0gIkMGJhM2Q4NTktYzNiYy00OGI5LTllNGYtN2U4ZjBkM2U5MTIx2AIE4AIB" +
//                "&lang=en-us&sid=bd4536b6562d9d5d1fe1a4b130909ea1&sb=1&sb_lp=1&search_form_id=1f2f37d04ed60074&ss=&checkin_monthday=4" +
//                "&group_adults=2&group_children=0&no_rooms=1&group_adults_overlay=2&group_children_overlay=0&no_rooms_overlay=1&lpsr=1&is_aroundme=1&latitude=30.758593&longitude=103.929802");
//
        StringBuilder url=new StringBuilder("https://www.booking.com/searchresults.html?ss=Chengdu&group_adults=2&group_children=0&no_rooms=1&sb_travel_purpose=leisure&ssne=Chengdu&ssne_untouched=Chengdu&label=gen173bo-1DCAEoggI46AdIM1gDaDGIAQKYATG4AQnIARHYAQPoAQH4AQOIAgGYAgKoAgO4AuLksJIGwAIB0gIkOTlmMjEwNDktOWRjMS00YTY1LWFhZDUtOTg2MmMzNzYyZTRl2AIE4AIB&sid=87daccf59882f262d007baadae2bda97&aid=304142&lang=en-us&sb=1&src_elem=sb&src=searchresults&dest_id=-1900349&dest_type=city&checkin=2022-04-05&checkout=2022-04-06&activeTab=search&latitude=");
        //30.75859&longitude=103.4332");
        Random r=new Random();
        lanti=30+r.nextDouble();
        longti=103+r.nextDouble();
        url.append(lanti+"&longitude="+longti);
        Log.d("url replace",url.toString());
        return url.toString();
    }

}
