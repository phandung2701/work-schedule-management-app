package com.example.btl_nhom4;

import android.os.Bundle;
import android.os.Handler;
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

public class LateForWorkActivity extends AppCompatActivity {
    private RecyclerView rcv_lateForWork;
    private EmployeeAdapter mLateForWorkAdapter;
    private ImageView btnBack;

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private List<User> mListLateForWork;
    private TextView tv_dateNow,tv_amount;
    private int idWsp;
    private int year;
    private int month;
    private int day;
    private Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_late_for_work);

        rcv_lateForWork = findViewById(R.id.rcv_employee);
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

        mListLateForWork = new ArrayList<>();

        idWsp = bundle.getInt("wspID");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcv_lateForWork.setLayoutManager(linearLayoutManager);

        mLateForWorkAdapter = new EmployeeAdapter(mListLateForWork,getApplicationContext());
        rcv_lateForWork.setAdapter(mLateForWorkAdapter);

        getListLateForWork();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String amount = mListLateForWork.size() + " người";
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

    private void getListLateForWork(){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        reference.child("Calendar").child(String.valueOf(idWsp)).child(String.valueOf(year)).child(String.valueOf(month+1))
                .child(String.valueOf(day)).child("LateForWork").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListLateForWork.clear();
                for (DataSnapshot data : snapshot.getChildren()){
                    User user = data.getValue(User.class);
                    mListLateForWork.add(user);
                }
                mLateForWorkAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // show error when calling api failed
            }
        });
    }

}