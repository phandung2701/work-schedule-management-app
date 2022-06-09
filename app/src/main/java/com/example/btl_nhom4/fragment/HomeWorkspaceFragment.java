package com.example.btl_nhom4.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.btl_nhom4.Calendar;
import com.example.btl_nhom4.R;
import com.example.btl_nhom4.login;


public class HomeWorkspaceFragment extends Fragment {





    public HomeWorkspaceFragment() {
        // Required empty public constructor
    }

    LinearLayout calendarWorkspace;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_workspace, container, false);
        calendarWorkspace = view.findViewById(R.id.calendarWorkspace);
        calendarWorkspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), Calendar.class);
                startActivity(intent);
            }
        });
        return view;


    }
}