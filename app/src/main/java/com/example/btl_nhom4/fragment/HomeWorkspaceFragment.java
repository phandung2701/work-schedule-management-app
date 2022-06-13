package com.example.btl_nhom4.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.btl_nhom4.AddEmployees;
import com.example.btl_nhom4.Calendar;
import com.example.btl_nhom4.EmployeeActivity;
import com.example.btl_nhom4.R;
import com.example.btl_nhom4.ResignationLetterActivity;
import com.example.btl_nhom4.StatisticByDay;
import com.example.btl_nhom4.WorkspaceActivityAdmin;
import com.example.btl_nhom4.login;
import com.example.btl_nhom4.model.user.Workspace;
import com.google.firebase.auth.FirebaseAuth;


public class HomeWorkspaceFragment extends Fragment {





    public HomeWorkspaceFragment() {
        // Required empty public constructor
    }

    private LinearLayout calendarWorkspace,statisticByDay,add_employees,resignation_letter,list_employee;
    private TextView tv_name_workspace,tv_email_workspace;
    private CardView cv_administrator,browse_app,single_newspaper,staff;
    private  WorkspaceActivityAdmin mWorkspaceActivityAdmin;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_workspace, container, false);
        calendarWorkspace = view.findViewById(R.id.calendarWorkspace);
        statisticByDay = view.findViewById(R.id.statistic);
        add_employees = view.findViewById(R.id.add_employee);
        resignation_letter = view.findViewById(R.id.resignnation_letter);
        tv_name_workspace = view.findViewById(R.id.tv_name_workspace);
        tv_email_workspace = view.findViewById(R.id.tv_email_workspace);
        cv_administrator = view.findViewById(R.id.cv_administrator);
        browse_app = view.findViewById(R.id.browse_app);
        single_newspaper = view.findViewById(R.id.single_newspaper);
        list_employee = view.findViewById(R.id.list_employee);
        staff = view.findViewById(R.id.Staff);

        mWorkspaceActivityAdmin = (WorkspaceActivityAdmin) getActivity();

        tv_name_workspace.setText(mWorkspaceActivityAdmin.getName());
        tv_email_workspace.setText(mWorkspaceActivityAdmin.getEmail());
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(uid.equals(mWorkspaceActivityAdmin.getAdmin())){
            single_newspaper.setVisibility(View.GONE);
        }
        else {
            cv_administrator.setVisibility(View.GONE);
            browse_app.setVisibility(View.GONE);
            single_newspaper.setVisibility(View.VISIBLE);
        }
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
                Bundle bundle = new Bundle();
                bundle.putInt("wspID",mWorkspaceActivityAdmin.getIdWsp());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), EmployeeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("wspID",mWorkspaceActivityAdmin.getIdWsp());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        list_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), EmployeeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("wspID",mWorkspaceActivityAdmin.getIdWsp());
                intent.putExtras(bundle);
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