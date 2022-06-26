package com.example.btl_nhom4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.btl_nhom4.adapter.SummaryForAdminAdapter;
import com.example.btl_nhom4.model.user.MonthlySummary;
import com.example.btl_nhom4.model.user.User;
import com.example.btl_nhom4.model.user.Workspace;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

public class SummaryForAdmin extends AppCompatActivity {
    // variables for view
    RecyclerView recyclerViewSummaryForAdmin;
    private ImageView btnBack;

    // variables for features
    private String workspaceId;
    private String adminId;
    private String year;
    private int totalWorkOnTimes;
    private int totalLateForWorks;
    private int totalOffWorks;
    private Calendar calendar;
    private ArrayList<User> listUsers;
    private ArrayList<String> listMonthOfYear;
    private ArrayList<String> listDayOfMonth;
    private ArrayList<User> listUsersLateForWork;
    private ArrayList<User> listUsersWorkOnTime;
    private ArrayList<MonthlySummary> listMonthlySummary;
    private SummaryForAdminAdapter summaryForAdminAdapter;

    // constant variable
    private final int[] dayOfMonths = {31,29,31,30,31,30,31,31,30,31,30,31};

    // constant variables for child of firebase in database
    private final String CHILD_USERS = "Users";
    private final String CHILD_CALENDAR = "Calendar";
    private final String CHILD_EMPLOYEES = "Employees";
    private final String CHILD_LATE_FOR_WORK = "LateForWork";
    private final String CHILD_WORK_ON_TIME = "WorkOnTime";
    private final String CHILD_WORKSPACES = "Workspaces";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_for_admin);

        // set variable
        totalLateForWorks = 0;
        totalWorkOnTimes = 0;
        totalOffWorks = 0;
        listUsers = new ArrayList<User>();
        listMonthOfYear = new ArrayList<String>();
        listDayOfMonth = new ArrayList<String>();
        listUsersLateForWork = new ArrayList<User>();
        listUsersWorkOnTime = new ArrayList<User>();
        listMonthlySummary = new ArrayList<MonthlySummary>();

        // get data in bundle
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        workspaceId = String.valueOf(bundle.getInt("wspID"));

        // set by ID
        recyclerViewSummaryForAdmin = findViewById(R.id.recyclerViewSummaryForAdmin);
        btnBack = findViewById(R.id.btnBackPressed);

        // get date
        calendar = java.util.Calendar.getInstance();
        year = String.valueOf(calendar.get(java.util.Calendar.YEAR));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewSummaryForAdmin.setLayoutManager(linearLayoutManager);

        // set adapter
        summaryForAdminAdapter = new SummaryForAdminAdapter(getApplicationContext(), listMonthlySummary);
        recyclerViewSummaryForAdmin.setAdapter(summaryForAdminAdapter);

        // proccessing
        getDataFromFirebaseDatabase();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getDataFromFirebaseDatabase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        // get uid admin in workspace
        databaseReference.child(CHILD_WORKSPACES).child(workspaceId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Workspace workspace = task.getResult().getValue(Workspace.class);
                    adminId = workspace.getAdmin();
                }
            }
        });

        // get list the employee
        databaseReference.child(CHILD_WORKSPACES)
                .child(workspaceId)
                .child(CHILD_EMPLOYEES)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUsers.clear();
                for (DataSnapshot data: snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    listUsers.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // get month of year
        databaseReference
            .child(CHILD_CALENDAR)
            .child(workspaceId)
            .child(year)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listMonthOfYear.clear();
                    listDayOfMonth.clear();
                    listUsersWorkOnTime.clear();
                    listUsersLateForWork.clear();

                    // save all the months into list and filter in this list
                    for (DataSnapshot data : snapshot.getChildren()){
                        listMonthOfYear.add(data.getKey());
                    }

                    for(String month: listMonthOfYear) {
                        listUsersWorkOnTime.clear();
                        listUsersLateForWork.clear();

                        databaseReference
                            .child(CHILD_CALENDAR)
                            .child(workspaceId)
                            .child(year)
                            .child(month)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    // same the task save all the months
                                    // Use list days of the month to get all the user's purposes in this list
                                    for (DataSnapshot data: snapshot.getChildren()) {
                                        listDayOfMonth.add(data.getKey());
                                    }

                                    for (String day: listDayOfMonth) {
                                        // save the list users late for work
                                        databaseReference
                                            .child(CHILD_CALENDAR)
                                            .child(workspaceId)
                                            .child(year)
                                            .child(month)
                                            .child(day)
                                            .child(CHILD_LATE_FOR_WORK)
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot data: snapshot.getChildren()) {
                                                        User user = data.getValue(User.class);
                                                        listUsersLateForWork.add(user);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                        // save the list users work on time
                                        databaseReference
                                            .child(CHILD_CALENDAR)
                                            .child(workspaceId)
                                            .child(year)
                                            .child(month)
                                            .child(day)
                                            .child(CHILD_WORK_ON_TIME)
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot data: snapshot.getChildren()) {
                                                        User user = data.getValue(User.class);
                                                        listUsersWorkOnTime.add(user);
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

                        // delay in order to get data from firebase
                        // [Issue]: Async Firebase
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                checkTotalOnlWorkPerMonth(
                                    Integer.valueOf(month),
                                    listUsers,
                                    listUsersLateForWork,
                                    listUsersWorkOnTime
                                );
                            }
                        }, 350);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.println(
                        Log.ERROR,
                        "firebase",
                        error.getMessage()
                    );
                }
            });
    }

    private void checkTotalOnlWorkPerMonth(int month, ArrayList<User> users, ArrayList<User> lateForWorks, ArrayList<User> onWorkTimes) {
        for (User user: users) {
            if (user.getUid().equals(adminId)) continue;

            totalOffWorks = 0;
            totalWorkOnTimes = 0;
            totalLateForWorks = 0;

            // Check the number of days What the staff is working late
            for (User userLateForWork: lateForWorks) {
                if (user.getUid().equals(userLateForWork.getUid())) {
                    totalLateForWorks++;
                }
            }

            // Check the number of days What the user is work on time
            for (User userOnWorkTime: onWorkTimes) {
                if (user.getUid().equals(userOnWorkTime.getUid())) {
                    totalWorkOnTimes++;
                }
            }

            // off work = day of month - (total work on time + total late for work);
            totalOffWorks =
                dayOfMonths[month -1]
                - (totalLateForWorks + totalWorkOnTimes);

            // add monthly adapter
            MonthlySummary monthlySummary = new MonthlySummary(
                month,
                totalWorkOnTimes,
                totalLateForWorks,
                totalOffWorks,
                user.getUsername()
            );

            listMonthlySummary.add(monthlySummary);
        }

        summaryForAdminAdapter.notifyDataSetChanged();
    }
}