package com.example.contest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amap.api.maps.model.LatLng;
import com.example.contest.AMapActivity;
import com.example.contest.CheckPermissionsActivity;
import com.example.contest.ChooseCityActivity;
import com.example.contest.ChooseFileActivity;
import com.example.contest.ChooseVirtualActivity;
import com.example.contest.R;
import com.example.contest.UserProfileActivity;
import com.example.contest.Utils.algorithm.TrajectorySimulator.TrajectorySimulator;
import com.example.contest.Utils.algorithm.geography.Point;

import java.util.ArrayList;

public class settingFragment extends Fragment {



    private TextView tvuserproflie;
    private TextView tvchoosecity;
    private TextView tvshowmap;
    private TextView tvgetpromission;
    private TextView tvVirtualBrower;
    public ArrayList<Point> trajectory;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

       return inflater.inflate(R.layout.fragment_notifications,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvgetpromission=getActivity().findViewById(R.id.tv_getPromission);
        tvVirtualBrower=getActivity().findViewById(R.id.tv_virtual_brower);
        tvVirtualBrower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ChooseVirtualActivity.class);//打开新activity：
                startActivity(intent);

            }
        });



        tvgetpromission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), CheckPermissionsActivity.class);//打开新activity：
                startActivity(intent);
            }
        });
        tvchoosecity=getActivity().findViewById(R.id.tv_chcity);
        tvchoosecity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ChooseCityActivity.class);//打开新activity：
                startActivity(intent);
            }
        });
        tvuserproflie=getActivity().findViewById(R.id.showprofile);
        tvuserproflie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), UserProfileActivity.class);//打开新activity：
                startActivity(intent);
            }
        });
        tvshowmap=getActivity().findViewById(R.id.tv_showtrace);
        tvshowmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ChooseFileActivity.class);//打开新activity：
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}