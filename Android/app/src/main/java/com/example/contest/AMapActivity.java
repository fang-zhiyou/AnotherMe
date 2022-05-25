package com.example.contest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.example.contest.Common.CommonVar;
import com.example.contest.Utils.algorithm.TrajectorySimulator.TrajectorySimulator;
import com.example.contest.Utils.algorithm.geography.Point;

import java.util.ArrayList;
import java.util.List;

public class AMapActivity extends AppCompatActivity {
        private MapView mMapView;
        private RelativeLayout relativeLayout;
        private  AMap aMap;



    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_amap);
            relativeLayout = (RelativeLayout) findViewById(R.id.amap);
        Intent intent=getIntent();
        ArrayList<Point> trajectory=new ArrayList<>();

        long time[]=intent.getLongArrayExtra("time");
        double longi[]=intent.getDoubleArrayExtra("long");
        double lanti[]=intent.getDoubleArrayExtra("lati");

        for(int i=0;i<time.length;i++){
            trajectory.add(new Point(time[i],longi[i],lanti[i]));
            Log.d("point",time[i]+" "+longi[i]+" "+lanti[i]);
        }

        CameraPosition LUJIAZUI =null;
            if(trajectory!=null&&trajectory.size()!=0){


                Point p=trajectory.get(0);
                LatLng startPosition=new LatLng(p.latitude,p.longitude);
                LUJIAZUI=new CameraPosition.Builder()
                        .target(startPosition).zoom(18).bearing(0).tilt(30).build();
            }
            AMapOptions aOptions = new AMapOptions();

            aOptions.camera(LUJIAZUI);
             mMapView = new MapView(this,aOptions);
            mMapView.onCreate(savedInstanceState);// 此方法必须重写
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

//设置中心点


           // aMap.setTrafficEnabled(true);// 显示实时交通状况
            //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
            aMap.setMapType(AMap.MAP_TYPE_BUS);// 卫星地图模式
           // aMap.setMapTextZIndex(1);

//            MyLocationStyle myLocationStyle;
//            myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//            myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

  //          aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
  //          aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
         //   aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        //    List<LatLng> latLngs = new ArrayList<LatLng>();

//            for(Point p:MainActivity.trajectory_test){
//                latLngs.add(new LatLng(p.latitude,p.longitude));
//            }
            ArrayList<MarkerOptions> almo=new ArrayList<>();

      //      new MarkerOptions().position(latLngs).title("北京").snippet("DefaultMarker")
            if(trajectory!=null&&trajectory.size()!=0){
                for(Point p:trajectory){
                 //   latLngs.add();
                 //   LatLng latLng = new LatLng(39.906901,116.397972);
                    almo.add(new MarkerOptions().position(new LatLng(p.latitude,p.longitude)));
                }
            }
            aMap.addMarkers(almo,false);
            LinearLayout.LayoutParams mParams; mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            relativeLayout.addView(mMapView, mParams);
            Log.d("draw","succeed");
          //  aMap.addPolyline(new PolylineOptions().
                 //   addAll(latLngs).width(10).color(Color.argb(255, 1, 1, 1)));
        }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
       finish();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

}