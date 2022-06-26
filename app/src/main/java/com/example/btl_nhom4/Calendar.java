package com.example.btl_nhom4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.btl_nhom4.model.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class Calendar extends AppCompatActivity {


    private CalendarView calendar;
    private TextView selectDate;
    private SimpleDateFormat sdf;
    private ImageView btnBackPressed;
    private LinearLayout noCheckIn;
    private TextView btnCheckIn,checked,overtime;
    private ProgressBar progressBar;

    private int idWsp;
    private String uid;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;



    private java.util.Calendar mCalendar;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendar = (CalendarView)findViewById(R.id.calendar);
        selectDate = findViewById(R.id.selectDate);
        btnBackPressed = findViewById(R.id.btnBackPressed);
        checked = findViewById(R.id.checked);
        noCheckIn = findViewById(R.id.no_checkin);
        btnCheckIn = findViewById(R.id.btn_checkin);
        progressBar = findViewById(R.id.progressBar);
        overtime = findViewById(R.id.overtime);


        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        idWsp = bundle.getInt("wspID");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        sdf = new SimpleDateFormat("EEEE", Locale.US);

        mCalendar = java.util.Calendar.getInstance();
        year = mCalendar.get(java.util.Calendar.YEAR);
         month = mCalendar.get(java.util.Calendar.MONTH);
         day = mCalendar.get(java.util.Calendar.DAY_OF_MONTH);
         hour = mCalendar.get(java.util.Calendar.HOUR_OF_DAY);
         minute = mCalendar.get(java.util.Calendar.MINUTE);
        Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(day + "/"+(month+1)+"/"+year);
            String dayOfTheWeek = sdf.format(date);
            String dateString = dayOfTheWeek + ", "+day+"/"+(month+1)+"/"+year;
            selectDate.setText(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                try {
                    Date date = new SimpleDateFormat("dd/MM/yyyy",Locale.US).parse(dayOfMonth + "/"+(month+1)+"/"+year);
                    String dayOfTheWeek = sdf.format(date);
                    String dateSelect = dayOfTheWeek+", "+dayOfMonth + "/"+(month+1)+"/"+year;
                    selectDate.setText(dateSelect);
                    showCheckIn(year,month,dayOfMonth);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        showCheckIn(year,month,day);
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                CheckIn();
                checked.setVisibility(View.VISIBLE);
                noCheckIn.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        });

        btnBackPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void CheckIn(){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        reference.child("Workspaces").child(String.valueOf(idWsp)).child("Employees")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data : snapshot.getChildren()){
                            User user = data.getValue(User.class);
                            if(user.getUid().equals(uid) ){
                                if( (hour == 8 && minute <= 15)){
                                    WorkOnTime(user);
                                }
                                else{
                                    LateForWork(user);
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

    private void showCheckIn(int selectYear, int selectMonth, int selectDay){

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();


        reference.child("Calendar")
                 .child(String.valueOf(idWsp))
                .child(String.valueOf(year))
                .child(String.valueOf(month+1))
                .child(String.valueOf(day))
                .child("WorkOnTime")
                .child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user != null && day == selectDay && month== selectMonth && year == selectYear  ){
                    checked.setVisibility(View.VISIBLE);
                    noCheckIn.setVisibility(View.GONE);
                    overtime.setVisibility(View.GONE);
                }
                else if(day == selectDay && month== selectMonth && year == selectYear && (hour >= 8 && hour <= 20) ){
                    noCheckIn.setVisibility(View.VISIBLE);
                    checked.setVisibility(View.GONE);
                    overtime.setVisibility(View.GONE);

                }
                else {
                    noCheckIn.setVisibility(View.GONE);
                    checked.setVisibility(View.GONE);
                    if(hour > 17){
                        overtime.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("Calendar")
                .child(String.valueOf(idWsp))
                .child(String.valueOf(year))
                .child(String.valueOf(month+1))
                .child(String.valueOf(day))
                .child("LateForWork")
                .child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user != null && day == selectDay && month== selectMonth && year == selectYear){
                    checked.setVisibility(View.VISIBLE);
                    noCheckIn.setVisibility(View.GONE);
                    overtime.setVisibility(View.GONE);
                }
                else if(day == selectDay && month== selectMonth && year == selectYear && (hour >= 8 && hour <= 17 )){
                    noCheckIn.setVisibility(View.VISIBLE);
                    checked.setVisibility(View.GONE);
                    overtime.setVisibility(View.GONE);
                }
                else {
                    noCheckIn.setVisibility(View.GONE);
                    checked.setVisibility(View.GONE);
                    if(hour > 17){
                        overtime.setVisibility(View.VISIBLE);
                    }

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("Workspaces").child(String.valueOf(idWsp)).child("admin")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(uid.equals(task.getResult().getValue().toString())){
                        noCheckIn.setVisibility(View.GONE);
                        checked.setVisibility(View.GONE);
                        overtime.setVisibility(View.GONE);
                    }
                }
            }
        });

    }


    private void WorkOnTime(User user){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        reference.child("Calendar")
                .child(String.valueOf(idWsp))
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
                .child(String.valueOf(idWsp))
                .child(String.valueOf(year))
                .child(String.valueOf(month+1))
                .child(String.valueOf(day))
                .child("LateForWork")
                .child(user.getUid()).setValue(user);
    }


}