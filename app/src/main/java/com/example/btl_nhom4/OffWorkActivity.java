package com.example.btl_nhom4;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_nhom4.adapter.EmployeeAdapter;
import com.example.btl_nhom4.model.user.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OffWorkActivity extends AppCompatActivity {
    private RecyclerView rcv_offWork;
    private EmployeeAdapter mOffWorkAdapter;
    private ImageView btnBack;

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private List<User> mListOffWork;
    private int idWsp;
    private TextView tv_dateNow,tv_amount;
    private int year;
    private int month;
    private int day;
    private Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_off_work);

        rcv_offWork = findViewById(R.id.rcv_employee);
        tv_dateNow = findViewById(R.id.tv_dateNow);
        tv_amount = findViewById(R.id.amount);
        btnBack = findViewById(R.id.btnBackPressed);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        String strDate = day+"/"+(month+1)+"/"+year;

        tv_dateNow.setText(strDate);

        mListOffWork = new ArrayList<>();

        idWsp = bundle.getInt("wspID");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcv_offWork.setLayoutManager(linearLayoutManager);

        mOffWorkAdapter = new EmployeeAdapter(mListOffWork,getApplicationContext());
        rcv_offWork.setAdapter(mOffWorkAdapter);

        getListOffWork();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String amount = mListOffWork.size() + " người";
                tv_amount.setText(amount);
            }
        },300);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

    }

    private void getListOffWork(){

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        List<User> all = new ArrayList<>();
        reference.child("Calendar").child(String.valueOf(idWsp)).child(String.valueOf(year)).child(String.valueOf(month+1))
                .child(String.valueOf(day)).child("WorkOnTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                all.clear();
                for (DataSnapshot data : snapshot.getChildren()){
                    User user = data.getValue(User.class);
                    all.add(user);
                }
                reference.child("Calendar").child(String.valueOf(idWsp)).child(String.valueOf(year)).child(String.valueOf(month+1))
                        .child(String.valueOf(day)).child("LateForWork").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()){
                            User user = data.getValue(User.class);
                            all.add(user);
                        }
                        reference.child("Workspaces").child(String.valueOf(idWsp)).child("Employees")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        mListOffWork.clear();
                                        for (DataSnapshot data : snapshot.getChildren()){
                                            User user = data.getValue(User.class);
                                           if(!checkOffWork(user,all)){
                                               mListOffWork.add(user);
                                           }
                                        }
                                        mOffWorkAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // show error when calling api failed
            }
        });

    }
    private boolean checkOffWork(User user,List<User> all){
        for(User item : all){
            if(user.getUid().equals(item.getUid())){
                return true;
            }

        }
        return false;
    }

}