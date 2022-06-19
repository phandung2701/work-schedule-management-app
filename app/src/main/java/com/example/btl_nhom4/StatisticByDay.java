package com.example.btl_nhom4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_nhom4.model.user.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StatisticByDay extends AppCompatActivity {
    private TextView txt_statisticByDay,tv_workOnTime,tv_lateForWork,tv_offWork;
    private ImageView imgBackActivity;
    private CardView workOnTime,offWork,lateForWork;
    private int idWsp;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private List<User> mListWorkOnTime;
    private List<User> mListLateForWork;
    private List<User> mListOffWork;
    private int dem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_by_day);

        txt_statisticByDay = findViewById(R.id.statisticByDay_text);
        imgBackActivity = findViewById(R.id.statisticBackActivity);
        workOnTime = findViewById(R.id.workOnTime);
        offWork = findViewById(R.id.offWork);
        lateForWork = findViewById(R.id.lateForWork);
        tv_workOnTime = findViewById(R.id.tv_workOnTime);
        tv_lateForWork = findViewById(R.id.tv_lateForWork);
        tv_offWork = findViewById(R.id.tv_offWork);


        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        idWsp = bundle.getInt("wspID");

        mListWorkOnTime = new ArrayList<>();
        mListLateForWork = new ArrayList<>();
        mListOffWork = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dateString = "Thời gian: "+day + "/" + (month+1) + "/" + year+"-"+day + "/" + (month+1) + "/" + year;

        txt_statisticByDay.setText(dateString);




        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        reference.child("Workspaces").child(String.valueOf(idWsp)).child("Employees")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mListOffWork.clear();
                        for (DataSnapshot data : snapshot.getChildren()){
                            User user = data.getValue(User.class);
                            mListOffWork.add(user);

                        }
                        dem = mListOffWork.size();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // show error when calling api failed
                    }

                });
        reference.child("Calendar").child(String.valueOf(idWsp)).child(String.valueOf(year)).child(String.valueOf(month+1))
                .child(String.valueOf(day)).child("WorkOnTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListWorkOnTime.clear();
                for (DataSnapshot data : snapshot.getChildren()){
                    User user = data.getValue(User.class);
                    mListWorkOnTime.add(user);

                }
                dem -= mListWorkOnTime.size();
                Log.e("firebase", String.valueOf(dem));
                String tv1 = "Có "+mListWorkOnTime.size()+" người đi làm đúng giờ";
                tv_workOnTime.setText(tv1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // show error when calling api failed
            }
        });
        reference.child("Calendar").child(String.valueOf(idWsp)).child(String.valueOf(year)).child(String.valueOf(month+1))
                .child(String.valueOf(day)).child("LateForWork").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListLateForWork.clear();
                for (DataSnapshot data : snapshot.getChildren()){
                    User user = data.getValue(User.class);
                    mListLateForWork.add(user);

                }
                dem -= mListLateForWork.size();
                String tv2 = "Có "+mListLateForWork.size()+" người đi làm muộn";
                tv_lateForWork.setText(tv2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // show error when calling api failed
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String tv3 = "Có "+dem+" người chưa checkin";
                tv_offWork.setText(tv3);
            }
        },350);

        workOnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticByDay.this,WorkOnTimeActivity.class);
                Bundle bundle1 = new Bundle();
               bundle1.putInt("wspID",bundle.getInt("wspID"));
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
        lateForWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticByDay.this,LateForWorkActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("wspID",bundle.getInt("wspID"));
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
        offWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticByDay.this,OffWorkActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("wspID",bundle.getInt("wspID"));
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
        imgBackActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}