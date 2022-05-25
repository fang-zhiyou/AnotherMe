package com.example.contest;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.contest.Utils.algorithm.TrajectorySimulator.TrajectorySimulator;
import com.example.contest.Utils.noUse.DataBase;
import com.example.contest.Utils.algorithm.geography.Point;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.contest.databinding.ActivityMainBinding;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationListener mLocationListener = null;
    public static ArrayList<Point> trajectory_test;
    ArrayList<Point> wirteToFile = new ArrayList<>();
    FileOutputStream out = null;
    BufferedWriter writer = null;

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Point p1_geo = new Point(116.303937, 39.924058);
                Point p2_geo = new Point(116.310018, 39.908059);

                ArrayList<Point> points = new ArrayList<>();
                points.add(p1_geo);
                points.add(p2_geo);

                TrajectorySimulator simulator = new TrajectorySimulator(points, points, 60, 2);
                trajectory_test = simulator.trajectorySimulate();


            }
        }).start();

        mLocationClient.updatePrivacyAgree(getApplicationContext(), true);
        mLocationClient.updatePrivacyShow(getApplicationContext(), true, true);

        try {
            mLocationClient = new AMapLocationClient(getApplicationContext());
            Log.e("create", "mLocationCLient");
        } catch (Exception e) {
            e.printStackTrace();
        }

//初始化定位




//设置定位回调监听


        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {

                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        Log.e("xy", " " + aMapLocation.getLatitude() + " " + aMapLocation.getLongitude());
                        if (wirteToFile.size() < 10) {
                            Point point = new Point(System.currentTimeMillis(), aMapLocation.getLatitude(), aMapLocation.getLongitude());
                            wirteToFile.add(point);
                            Log.d("witreTOdb", " size=" + wirteToFile.size());

                        } else {
                            try {

                               // File[] files =getApplicationContext().getFilesDir().listFiles();
                                SimpleDateFormat formatter =new SimpleDateFormat("MM月dd日 HH时");

                                long nowtime=System.currentTimeMillis();
                                Date curDate=new Date((nowtime));
                                String nowFormat=formatter.format(curDate);
                                out = openFileOutput(nowFormat, Context.MODE_APPEND);


                                writer = new BufferedWriter(new OutputStreamWriter(out));
                                for (int i = 0; i < 10; i++) {

                                    StringBuffer sb = new StringBuffer();
                                    sb.append(wirteToFile.get(i).timestamp);
                                    sb.append(",");
                                    sb.append(wirteToFile.get(i).latitude);
                                    sb.append(",");
                                    sb.append(wirteToFile.get(i).longitude);

                                    writer.write(sb.toString());
                                    writer.newLine();
                                    writer.flush();
                                    //      writer.close();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    if (writer != null) {
                                        writer.close();
                                    }
                                } catch (IOException E) {
                                    E.printStackTrace();
                                }
                            }
                            Log.d("write", "add down!");
                            wirteToFile.clear();
                        }


                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }

            }
        });

//初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        AMapLocationClientOption option = new AMapLocationClientOption();
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);

        if (null != mLocationClient) {
            mLocationClient.setLocationOption(option);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(3000);
        mLocationOption.setNeedAddress(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        // View viewWebview= this.getLayoutInflater().inflate(R.layout.fragment_dashboard,null);

    }

}