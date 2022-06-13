package com.example.btl_nhom4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Calendar extends AppCompatActivity {


    CalendarView calendar;
    TextView selectDate;
    SimpleDateFormat sdf;
    ImageView btnBackPressed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendar = (CalendarView)findViewById(R.id.calendar);
        selectDate = findViewById(R.id.selectDate);
        btnBackPressed = findViewById(R.id.btnBackPressed);

        sdf = new SimpleDateFormat("EEEE", Locale.US);
        Date d = new Date();
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String day          = (String) df.format("dd",d);
        String monthNumber  = (String) df.format("MM",d);
        String year         = (String) df.format("yyyy",d);

        String dayOfTheWeek = sdf.format(d);
        String dateString = dayOfTheWeek + ", "+day+"/"+(monthNumber)+"/"+year;
        selectDate.setText(dateString);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                try {
                    Date date = new SimpleDateFormat("dd/MM/yyyy",Locale.US).parse(dayOfMonth + "/"+(month+1)+"/"+year);
                    String dayOfTheWeek = sdf.format(date);
                    String dateSelect = dayOfTheWeek+", "+dayOfMonth + "/"+(month+1)+"/"+year;
                    selectDate.setText(dateSelect);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btnBackPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}