package com.example.contest.Utils.tools.http;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.contest.Utils.algorithm.stayPoint.CityNameWithAnchorPoint;
import com.example.contest.Common.CommonVar;
import com.example.contest.Utils.algorithm.geography.Calculations;
import com.example.contest.Utils.algorithm.stayPoint.StayPoint;
import com.example.contest.Utils.algorithm.stayPoint.StayPointwithType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import okhttp3.Response;

public class MappingTools {
    public static ArrayList<ArrayList<ArrayList<StayPointwithType>>> KvirtualCity_has_allpoint=new ArrayList<>();

    public static ArrayList<ArrayList<StayPointwithType>> KvirtualmindisPoint=new ArrayList<>();
    public static void caltruepoint(Context mcontext,ArrayList<StayPointwithType> spwt,
                                    ArrayList<ArrayList<StayPointwithType>> virtualStayPointList){
        //每个城市虚拟城市根据真实城市映射一次
        double mindis=10000000.0;
        int num_spwt= spwt.size();
        Log.d("num_spwt",""+num_spwt);

        StringBuilder sb=new StringBuilder();
        //真实stay point的distance矩阵,即所有的stay point相互之间
        Double [][]dis=new Double[num_spwt][num_spwt];
        for(int i=0;i<num_spwt;i++){
            for(int j=0;j<num_spwt;j++){
                double dd=Calculations.getDistFromGeo(spwt.get(i), spwt.get(j));
                sb.append(dd+" ");
                dis[i][j]=dd;
            }
            sb.append("\n");
        }
        //Log.e("dis",sb.toString());

        //find 虚拟点的序列中 与dis矩阵相减绝对值最小的 的虚拟点序列
        Random r=new Random();
        //Log.d("kvirtualpoint",""+KvirtualCity_has_allpoint.toString());
        ArrayList<ArrayList<StayPointwithType>> allpoint=new ArrayList<>(virtualStayPointList);

        if(allpoint==null||allpoint.size()==0) return;
        ArrayList<StayPointwithType> mindisPoint = new ArrayList<>();

        for (int i=0;i<allpoint.size();i++){

                if(allpoint.get(i)!=null&&allpoint.get(i).size()!=0)
                mindisPoint.add(allpoint.get(i).get(0));

        }

            for (int i = 0; i < 128; i++) {
                double sum = 0;
                //每一个i都是一个选法

                ArrayList<StayPointwithType> spwttemp = new ArrayList<>();

                for (int j = 0; j < allpoint.size(); j++) {
                    if(allpoint.get(j)!=null&& allpoint.get(j).size()!=0){
                        int tmp=r.nextInt(allpoint.get(j).size());
                        spwttemp.add(allpoint.get(j).get(tmp));
                    }
//                      Log.e("spwttemp",spwttemp.get(j).latitude+" "+" long="+spwttemp.get(j).longitude);
                }

                int nn = spwttemp.size();
                double[][] dis_ = new double[nn][nn];
                //   Log.e("all point size"," "+nn);
                for (int j = 0; j < spwttemp.size(); j++) {
                    for (int k = 0; k <  spwttemp.size(); k++) {

                        double dd = Calculations.getDistFromGeo(spwttemp.get(j), spwttemp.get(k));
                        dis_[j][k] = dd;
                    }
                }

                for (int j = 0; j < spwttemp.size()&&j<spwt.size(); j++) {
                    for (int k = 0; k <  spwttemp.size()&&k<spwt.size(); k++) {

                        sum += Math.abs(dis_[j][k] - dis[j][k]);
                    }
                }
                if (mindis > sum) {


                    mindis = sum;
                    mindisPoint = new ArrayList<>();
                    mindisPoint.addAll(spwttemp);
                }
                //Log.d("subsum",sum+" ");

        }
        Log.d("mindis", mindis+"");
        Log.d("mindisPoint", mindisPoint + " ");
        Toast.makeText(mcontext,"mindisPoint"+mindisPoint,Toast.LENGTH_LONG).show();
        KvirtualmindisPoint.add(mindisPoint);

    }
    public static void usetypegetPoint(Context mContext,ArrayList<StayPointwithType> spwType,CityNameWithAnchorPoint sp) throws Exception {
        //KvirtualCity_has_allpoint.clear();

        //使用spwType记录了原始poi以及type
        ArrayList<ArrayList<StayPointwithType>> allpoint=new ArrayList<>();
        for (int ii = 0; ii < spwType.size(); ii++) {
            String type = spwType.get(ii).typeGBK;
            ArrayList<StayPointwithType> pointlist = null;
            try {
                pointlist = getVirtualPointSynchronized(sp, type);
            } catch (Exception e) {
                e.printStackTrace();
            }
            allpoint.add(pointlist);
        }
        KvirtualCity_has_allpoint.add(new ArrayList<>(allpoint));

        //开始虚拟poi映射 根据type映射到新城市的poi点

    }
    public static ArrayList<StayPointwithType> getVirtualPointSynchronized(CityNameWithAnchorPoint sp,String typeGbk) throws Exception {
        double splong=sp.longitude;
        double splan=sp.latitude;

        String url = "http://restapi.amap.com/v3/place/around?key=9c5b91e0113ea3e626682c1b891490fc&keywords=" + typeGbk +
                "&location="+splong+","+splan+"&children=1&offset=5&page=1&extensions=base";
        Response r=HttpRequest.get(url);

        JSONObject ele = new JSONObject(r.body().string());
        JSONArray p = ele.getJSONArray("pois");
        Log.e("ppp", p.toString());
        ArrayList<StayPointwithType> pointlist=new ArrayList<>();
        //对于某一个type 我们多获取4个poi，并返回到arraylist里
        for (int i = 0; i < 4; i++) {
            String location = p.getJSONObject(i).getString("location");
            String[] splitans = location.split(",");
            StayPointwithType p1 = new StayPointwithType(Double.valueOf(splitans[0]),Double.valueOf(splitans[1]),typeGbk);

            p1.setType(typeGbk);
            Log.d("location", location);
            Log.d("p1withtype", p1+" ");
            pointlist.add(p1);
        }
        return pointlist;
    }

    public static StayPointwithType getTypeSynchronized(StayPoint sp,int index) throws Exception {
        //根据stay point通过访问高德api获得type

        double longitude=sp.longitude;
        double lan=sp.latitude;
        //https://restapi.amap.com/v3/geocode/regeo?output=xml&location=116.310003,39.991957&key=<用户的key>&radius=1000&extensions=all
        String url="https://restapi.amap.com/v3/geocode/regeo?output=json&location="+longitude+","+lan+"&key=9c5b91e0113ea3e626682c1b891490fc&radius=200&extensions=all";
        Response r=HttpRequest.get(url);

                //Log.e("http", "msg = " + msg);
        JSONObject ele = new JSONObject(r.body().string());
        JSONObject reg=ele.getJSONObject("regeocode");
                    //     Log.e("reg",reg.toString());
        JSONArray p=reg.getJSONArray("pois");
                    //     Log.e("pois",p.toString());
        String TYPE=p.getJSONObject(0).getString("type");
        String []type=TYPE.split(";");
        String type_3=type[2];
        String location=p.getJSONObject(0).getString("location");
        StayPointwithType stayPointwithType=new StayPointwithType(sp.longitude,sp.latitude,type_3);

        Log.e("原始位置",longitude+" "+lan);
        Log.e("poi位置",location);
        Log.e("TYPE",type_3);
        return stayPointwithType;
    }



    public static void getAnchorPoint(String city, String poi){
        //测试http
        String s="https://restapi.amap.com/v3/place/text?key=9c5b91e0113ea3e626682c1b891490fc&keywords=&types="+poi+"&city="+city+"&children=1&offset=20&page=1&extensions=all";
 //"https://restapi.amap.com/v3/place/text?key=9c5b91e0113ea3e626682c1b891490fc&keywords=&types=141201&city=北京&children=1&offset=20&page=1&extensions=all"

        OkHttpUtils.get(s,new OkHttpCallback() {
            @Override
            public String onFinish(String status, String msg) throws JSONException {
                JSONObject ele = new JSONObject(msg);
                JSONArray p=ele.getJSONArray("pois");
                String p1=p.getJSONObject(0).getString("name");
                String p2=p.getJSONObject(0).getString("location");
                String []loc=p2.split(",");
                double longi=Double.valueOf(loc[0]);
                double lan=Double.valueOf(loc[1]);
                CommonVar.cityNameWithAnchorPoints.add(new CityNameWithAnchorPoint(city,longi,lan));
                //CommonVar.poi.add(p2);
                Log.e("location",p2+" "+longi+" "+lan);

                return p1;
            }
        });

    }



    public static void getVirtualPoint(CityNameWithAnchorPoint sp,String typeGbk){
        //此异步方法将不再使用
        double splong=sp.longitude;
        double splan=sp.latitude;
        //上海 121.5516,31.2307
        //杭州120.1988,30.2716
        //南京118.8497,32.0203
        //广州113.3424,23.1285
        String url = "http://restapi.amap.com/v3/place/around?key=9c5b91e0113ea3e626682c1b891490fc&keywords=" + typeGbk +
                "&location="+splong+","+splan+"&children=1&offset=5&page=1&extensions=base";

        OkHttpUtils.get(url, new OkHttpCallback() {
            @Override
            public String onFinish(String status, String msg) throws JSONException {
                Log.e("httpyyds", "msg = " + msg);
                JSONObject ele = new JSONObject(msg);
                JSONArray p = ele.getJSONArray("pois");
                Log.e("ppp", p.toString());
                ArrayList<StayPointwithType> pointlist=new ArrayList<>();
                //对于某一个type 我们多获取4个poi，并添加到allpoint里备用
                for (int i = 0; i < 4; i++) {
                    String location = p.getJSONObject(i).getString("location");
                    String[] splitans = location.split(",");
                    StayPointwithType p1 = new StayPointwithType(Double.valueOf(splitans[0]),Double.valueOf(splitans[1]),typeGbk);

                    p1.setType(typeGbk);
                    Log.d("location", location);
                    Log.d("p1withtype", p1+" ");
                    pointlist.add(p1);
                }
                //allpoint.add(new ArrayList<StayPointwithType>(pointlist));
                return "";
            }
        });

    }

    public static void getType(StayPoint sp,int index){
        //此异步方法将不再使用
        //根据stay point通过访问高德api获得type

        double longitude=sp.longitude;
        double lan=sp.latitude;
        //https://restapi.amap.com/v3/geocode/regeo?output=xml&location=116.310003,39.991957&key=<用户的key>&radius=1000&extensions=all
        String s="https://restapi.amap.com/v3/geocode/regeo?output=json&location="+longitude+","+lan+"&key=9c5b91e0113ea3e626682c1b891490fc&radius=200&extensions=all";

        OkHttpUtils.get(s,new OkHttpCallback() {
            @Override
            public String onFinish(String status, String msg) throws JSONException {
                //Log.e("http", "msg = " + msg);
                try {
                    JSONObject ele = new JSONObject(msg);
                    JSONObject reg=ele.getJSONObject("regeocode");
                    //     Log.e("reg",reg.toString());
                    JSONArray p=reg.getJSONArray("pois");
                    //     Log.e("pois",p.toString());
                    String TYPE=p.getJSONObject(0).getString("type");
                    String []type=TYPE.split(";");
                    String type_3=type[2];
                    String location=p.getJSONObject(0).getString("location");


                    CommonVar.spwType.get(index).setType(type_3);
                    Log.e("原始位置",longitude+" "+lan);
                    Log.e("poi位置",location);
                    Log.e("TYPE",type_3);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return "";
            }
        });
    }

}
