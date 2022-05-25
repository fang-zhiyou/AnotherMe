package com.example.contest.Utils.noUse;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.contest.R;


public class AnotherFragment extends Fragment {
    //no use 暂时未使用




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("s","onCreate Here is another fragment");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("s","onCreateAnotherFragment");
        return inflater.inflate(R.layout.fragment_another, container, false);
    }
}