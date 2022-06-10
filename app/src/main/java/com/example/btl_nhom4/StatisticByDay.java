package com.example.btl_nhom4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class StatisticByDay extends AppCompatActivity {
    TextView txt_statisticByDay;
    ImageView imgBackActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_by_day);

        txt_statisticByDay = findViewById(R.id.statisticByDay_text);
        imgBackActivity = findViewById(R.id.statisticBackActivity);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dateString = "Thời gian: "+day + "/" + (month+1) + "/" + year+"-"+day + "/" + (month+1) + "/" + year;

        txt_statisticByDay.setText(dateString);

        imgBackActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), WorkspaceActivityAdmin.class);
                startActivity(intent);
            }
        });

    }
}