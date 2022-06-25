package com.example.btl_nhom4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class SummaryForAdmin extends AppCompatActivity {
    // variables view
    RecyclerView recyclerViewSummaryForAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_for_admin);

        // set by ID
        recyclerViewSummaryForAdmin = findViewById(R.id.recyclerViewSummaryForAdmin);

    }
}