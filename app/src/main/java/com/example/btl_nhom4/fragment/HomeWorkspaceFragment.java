package com.example.btl_nhom4.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.btl_nhom4.AddEmployees;
import com.example.btl_nhom4.Calendar;
import com.example.btl_nhom4.EmployeeActivity;
import com.example.btl_nhom4.ListResignationLetters;
import com.example.btl_nhom4.R;
import com.example.btl_nhom4.ResignationLetterActivity;
import com.example.btl_nhom4.StatisticByDay;
import com.example.btl_nhom4.SummaryEmployeeActivity;
import com.example.btl_nhom4.SummaryForAdmin;
import com.example.btl_nhom4.WorkspaceActivityAdmin;
import com.example.btl_nhom4.login;
import com.example.btl_nhom4.model.user.User;
import com.example.btl_nhom4.model.user.Workspace;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeWorkspaceFragment extends Fragment {





    public HomeWorkspaceFragment() {
        // Required empty public constructor
    }

    private LinearLayout calendarWorkspace,statisticByDay,add_employees,resignation_letter,list_employee;
    private TextView tv_name_workspace,tv_email_workspace,summary_year;
    private CardView cv_administrator,browse_app,single_newspaper,staff,summary;
    private  WorkspaceActivityAdmin mWorkspaceActivityAdmin;
    private ImageView btnBackPressed;

    private String uid;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    private LinearLayout noCheckIn;
    private TextView btnCheckIn;
    private java.util.Calendar mCalendar;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ProgressBar progressBar;
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
        progressBar = view.findViewById(R.id.progressBar);
        btnBackPressed = view.findViewById(R.id.btnBackPressed);
        summary_year = view.findViewById(R.id.summary_year);
        summary = view.findViewById(R.id.summary);

        //
        mCalendar = java.util.Calendar.getInstance();
        year = mCalendar.get(java.util.Calendar.YEAR);
        summary_year.setText("Nhìn lại năm "+year);
        noCheckIn = view.findViewById(R.id.no_checkin);
        btnCheckIn = view.findViewById(R.id.btn_checkin);
        //

        mCalendar = java.util.Calendar.getInstance();
        year = mCalendar.get(java.util.Calendar.YEAR);
        month = mCalendar.get(java.util.Calendar.MONTH);
        day = mCalendar.get(java.util.Calendar.DAY_OF_MONTH);
        hour = mCalendar.get(java.util.Calendar.HOUR_OF_DAY);
        minute = mCalendar.get(java.util.Calendar.MINUTE);

        //
        mWorkspaceActivityAdmin = (WorkspaceActivityAdmin) getActivity();

        tv_name_workspace.setText(mWorkspaceActivityAdmin.getName());
        tv_email_workspace.setText(mWorkspaceActivityAdmin.getEmail());
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        showCheckIn();


        progressBar.setVisibility(View.VISIBLE);
        if(uid.equals(mWorkspaceActivityAdmin.getAdmin())){
            single_newspaper.setVisibility(View.GONE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    noCheckIn.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            },400);
        }
        else {
            if(hour < 8 || hour > 17 ){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    noCheckIn.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            },400);
            }
            cv_administrator.setVisibility(View.GONE);
            browse_app.setVisibility(View.GONE);
            single_newspaper.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }

        single_newspaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplication(), ListResignationLetters.class);
                Bundle bundle = new Bundle();
                bundle.putInt("wspID",mWorkspaceActivityAdmin.getIdWsp());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckIn();
            }
        });
        calendarWorkspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), Calendar.class);
                Bundle bundle = new Bundle();
                bundle.putInt("wspID",mWorkspaceActivityAdmin.getIdWsp());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        statisticByDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), StatisticByDay.class);
                Bundle bundle = new Bundle();
                bundle.putInt("wspID",mWorkspaceActivityAdmin.getIdWsp());
                intent.putExtras(bundle);
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
                Bundle bundle = new Bundle();
                bundle.putInt("wspID",mWorkspaceActivityAdmin.getIdWsp());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btnBackPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWorkspaceActivityAdmin.finish();
            }
        });
        // duyệt đơn
        browse_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), ListResignationLetters.class);
                Bundle bundle = new Bundle();
                bundle.putInt("wspID",mWorkspaceActivityAdmin.getIdWsp());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uid.equals(mWorkspaceActivityAdmin.getAdmin())){
                    // tổng kết của admin
                    Intent intent = new Intent(getActivity().getApplication(), SummaryForAdmin.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("wspID",mWorkspaceActivityAdmin.getIdWsp());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getActivity().getApplication(), SummaryEmployeeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("wspID",mWorkspaceActivityAdmin.getIdWsp());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    private void CheckIn(){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        reference.child("Workspaces").child(String.valueOf(mWorkspaceActivityAdmin.getIdWsp())).child("Employees")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data : snapshot.getChildren()){
                            User user = data.getValue(User.class);
                            if(user.getUid().equals(uid) ){
                                if( (hour == 8 && minute <= 15)){
                                    WorkOnTime(user);
                                    Toast.makeText(getContext(),"Bạn đã checkIn thành công",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    LateForWork(user);
                                    Toast.makeText(getContext(),"Bạn đã checkIn thành công",Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // show error when calling api failed
                    }
                });




    }

    private void showCheckIn(){

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();


        reference.child("Calendar")
                .child(String.valueOf(mWorkspaceActivityAdmin.getIdWsp()))
                .child(String.valueOf(year))
                .child(String.valueOf(month+1))
                .child(String.valueOf(day))
                .child("WorkOnTime")
                .child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user != null ){
                    noCheckIn.setVisibility(View.GONE);
                }
                else {
                    reference.child("Calendar")
                            .child(String.valueOf(mWorkspaceActivityAdmin.getIdWsp()))
                            .child(String.valueOf(year))
                            .child(String.valueOf(month+1))
                            .child(String.valueOf(day))
                            .child("LateForWork")
                            .child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            if(user != null  ){
                                noCheckIn.setVisibility(View.GONE);
                            }
                            else {
                                noCheckIn.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }


    private void WorkOnTime(User user){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        reference.child("Calendar")
                .child(String.valueOf(mWorkspaceActivityAdmin.getIdWsp()))
                .child(String.valueOf(year))
                .child(String.valueOf(month+1))
                .child(String.valueOf(day))
                .child("WorkOnTime")
                .child(user.getUid()).setValue(user);
    }

    private void LateForWork(User user){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        reference.child("Calendar")
                .child(String.valueOf(mWorkspaceActivityAdmin.getIdWsp()))
                .child(String.valueOf(year))
                .child(String.valueOf(month+1))
                .child(String.valueOf(day))
                .child("LateForWork")
                .child(user.getUid()).setValue(user);
    }

}