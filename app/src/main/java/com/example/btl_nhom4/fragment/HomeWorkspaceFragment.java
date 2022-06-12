package com.example.btl_nhom4.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.btl_nhom4.AddEmployees;
import com.example.btl_nhom4.Calendar;
import com.example.btl_nhom4.R;
import com.example.btl_nhom4.ResignationLetterActivity;
import com.example.btl_nhom4.StatisticByDay;
import com.example.btl_nhom4.login;
import com.example.btl_nhom4.model.user.Workspace;


public class HomeWorkspaceFragment extends Fragment {





    public HomeWorkspaceFragment() {
        // Required empty public constructor
    }

    private LinearLayout calendarWorkspace,statisticByDay,add_employees,resignation_letter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_workspace, container, false);
        calendarWorkspace = view.findViewById(R.id.calendarWorkspace);
        statisticByDay = view.findViewById(R.id.statistic);
        add_employees = view.findViewById(R.id.add_employee);
        resignation_letter = view.findViewById(R.id.resignnation_letter);

        calendarWorkspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), Calendar.class);
                startActivity(intent);
            }
        });

        statisticByDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), StatisticByDay.class);
                startActivity(intent);
            }
        });
        add_employees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), AddEmployees.class);
                startActivity(intent);
            }
        });

        resignation_letter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), ResignationLetterActivity.class);
                startActivity(intent);
            }
        });
        return view;


    }
}