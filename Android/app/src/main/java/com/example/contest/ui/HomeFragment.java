package com.example.contest.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.contest.Common.CommonVar;
import com.example.contest.R;
import com.example.contest.Utils.algorithm.Pipline;
import com.example.contest.ChooseCityActivity;
import com.example.contest.Utils.algorithm.TrajectorySimulator.TrajectorySimulator;
import com.example.contest.Utils.algorithm.geography.Point;
import com.example.contest.Utils.algorithm.stayPoint.StayPoint;
import com.example.contest.Utils.algorithm.stayPoint.StayPointwithType;
import com.example.contest.Utils.file.WriteToFile;
import com.example.contest.Utils.tools.http.MappingTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private Button btn_openfile;
    private TextView tv_num;
    private EditText et_file_name;
    private Button btn_start_init;
    private Button btn_add;
    private Button btn_remove;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("s", "onCreateView");


        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        Uri uri = data.getData();

        Toast.makeText(getActivity(), "file name:" + uri.getPath(), Toast.LENGTH_LONG).show();
        Log.d("filename", uri.toString());
        et_file_name.setText(uri.getPath());
        BufferedReader reader = null;
        String message = "";
        try {
            BufferedReader br = null;
            String line = "";
            String cvsSplitBy = ",";
            reader = getBufferedReader(getContext(), uri);
            Toast.makeText(getActivity(), "file content:" , Toast.LENGTH_LONG).show();
            Pipline.process(reader,getContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static BufferedReader getBufferedReader(Context context, Uri uri) throws IOException {
        InputStream inputStream =
                context.getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(inputStream)));

        return reader;
    }

    public void openfile() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");

        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 0);
    }

    void getKcityName(int k){
        // get K virtual city names
        CommonVar.chosenName.clear();
        CommonVar.cityNameWithAnchorPoints.clear();
        for(int i = 0; i<k; i++){
            CommonVar.chosenName.add(CommonVar.cityName.get(i));
            String ss = CommonVar.chosenName.get(i);
            Log.e("cityname", ss);
            MappingTools.getAnchorPoint(ss, "141201");
        }
        Log.d("chosenCityName",""+CommonVar.chosenName);
        Log.e("cityNameWithAnchorPoints",CommonVar.cityNameWithAnchorPoints.toString());
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // button_confire_num = (Button) getActivity().findViewById(R.id.btn_confirm_num);
        tv_num = (TextView) getActivity().findViewById(R.id.tv_num);
        tv_num.setText(CommonVar.num_of_k_virtual_city+" ");
        et_file_name = (EditText) getActivity().findViewById(R.id.text_home);
        btn_openfile = (Button) getActivity().findViewById(R.id.btn_openfile);
        btn_start_init = (Button) getActivity().findViewById(R.id.start_init);
        btn_add =(Button) getActivity().findViewById(R.id.btn_add);
        btn_remove=(Button) getActivity().findViewById(R.id.btn_remove);
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CommonVar.num_of_k_virtual_city>0) {
                    CommonVar.num_of_k_virtual_city--;
                    getKcityName(CommonVar.num_of_k_virtual_city);
                }
                tv_num.setText(CommonVar.num_of_k_virtual_city+" ");
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CommonVar.num_of_k_virtual_city<6) {
                    CommonVar.num_of_k_virtual_city++;
                    getKcityName(CommonVar.num_of_k_virtual_city);
                }
                tv_num.setText(CommonVar.num_of_k_virtual_city+" ");

            }
        });
        //getKcityName(CommonVar.num_of_k_virtual_city);
        btn_start_init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWhiteDialog();
            }
        });
        btn_openfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openfile();
            }
        });

/**
 * 预加载保存的staypoint
 */
        FileInputStream in=null;
        try {
            in=getContext().openFileInput("profile");
            WriteToFile.showRealPoints(in);
            Log.d("预加载保存的staypoint","succeed!");

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    /**
     * 进度条Dialog
     */
    private void showWhiteDialog(){
        /* @setProgress 设置初始进度
         * @setProgressStyle 设置样式（水平进度条）
         * @setMax 设置进度最大值
         */
        final int Max = 100;
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgress(0);

        progressDialog.setTitle("正在初始化");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(Max);
        progressDialog.show();
        /**
         * 开个线程
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pro = 0;
                        //get staypoints' types
                try {
                    CommonVar.spwType.clear();
                        for(int i=0;i<CommonVar.sp.size();i++){
                            StayPoint sp1=CommonVar.sp.get(i);
                               // StayPointwithType spwt=new StayPointwithType(sp1);


                                CommonVar.spwType.add(MappingTools.getTypeSynchronized(sp1,i));
                            }
                        pro+=20;
                        progressDialog.setProgress(pro);
                            Looper.prepare();
                            //Toast.makeText(getContext(),"成功获取type",Toast.LENGTH_SHORT).show();
                            //get virtual staypoints (mapping)
                            for(int i=0;i<CommonVar.cityNameWithAnchorPoints.size();i++) {
                                MappingTools.usetypegetPoint(getContext(), CommonVar.spwType,CommonVar.cityNameWithAnchorPoints.get(i));
                            }

                        pro+=20;
                        progressDialog.setProgress(pro);
                            //Toast.makeText(getContext(),"成功获取virtual",Toast.LENGTH_SHORT).show();
                        for(int index = 0; index< MappingTools.KvirtualCity_has_allpoint.size(); index++) {
                                MappingTools.caltruepoint(getContext(),CommonVar.spwType, MappingTools.KvirtualCity_has_allpoint.get(index));
                            }

                        pro+=20;
                        progressDialog.setProgress(pro);
                            Toast.makeText(getContext(),"成功计算virtual",Toast.LENGTH_SHORT).show();

                            Log.e("pipline", "begin");

                            ArrayList<Point> pois = new ArrayList<Point>();
                            for (StayPoint stayPoint : MappingTools.KvirtualmindisPoint.get(0)) {
                                pois.add(Point.phrasePoint(stayPoint));
                            }
                            ArrayList<Point> points=CommonVar.points;

                            TrajectorySimulator simulator = new TrajectorySimulator(points, pois,60,2);
                            CommonVar.trajectory= simulator.trajectorySimulate();
                            Log.e("pipline", "end");
                            //Toast.makeText(getContext(),"成功生成轨迹",Toast.LENGTH_LONG).show();
                        pro+=20;
                        progressDialog.setProgress(pro);
                    FileOutputStream out = null;
//                            File externalFilesDir =  Environment.getExternalStorageDirectory();;
//                            Log.d("gatsby", "externalFilesDirPath->" + externalFilesDir);
//
//
//                            String fileName = "0";
//                            File file = null;
//                            for (int i = 1; i <= 200; i++) {
//                                // out=openFileOutput("profile", Context.MODE_PRIVATE);
//                                file = new File("data/data/com.example.contest/files", fileName);
//                                if (file.exists()) {
//                                    fileName = String.valueOf(i);
//                                } else {
//                                    break;
//                                }
//                            }
//
//                            FileOutputStream fos = null;
//                            try {
//                                fos = new FileOutputStream(file);
//
//                                //获取要写出的文件内容
//                                StringBuilder content = new StringBuilder();
//                                for (Point p:CommonVar.trajectory) {
//                                    content.append(String.valueOf(p.latitude)+","+String.valueOf(p.longitude)+"\n");
//                                }
//                                Log.d("string",content.toString());
//                                fos.write(content.toString().getBytes("UTF-8"));
//                                if (fos != null) {
//                                    fos.close();
//                                }
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                    try {
                        out=getContext().openFileOutput("vup", Context.MODE_PRIVATE);
                        WriteToFile.saveKVirtualPoints(out);


                    }catch (Exception e){
                        e.printStackTrace();
                    }

                            pro+=20;



                        progressDialog.setProgress(pro);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                progressDialog.cancel();//达到最大就消失
            }

        }).start();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle
                                 savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("s", "onCreate");


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onStart() {

        super.onStart();

        Log.d("s", "onStart");
        //tv_num.setText(CommonVar.num_of_k_virtual_city);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("s", "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("s", "onResume");
    }


}