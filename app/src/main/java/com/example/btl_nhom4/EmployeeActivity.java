package com.example.btl_nhom4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_nhom4.adapter.EmployeeAdapter;
import com.example.btl_nhom4.adapter.NotificationAdapter;
import com.example.btl_nhom4.model.user.Notification;
import com.example.btl_nhom4.model.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmployeeActivity extends AppCompatActivity {
    private RecyclerView rcv_employee;
    private List<User> mListUsers;
    private EmployeeAdapter mEmployeeAdapter;
    private ImageView btnBack;
    private TextView tv_nameWorkspace,tv_emailWorkspace;
    private int idWsp;
    private FirebaseDatabase database ;
    private DatabaseReference reference ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        rcv_employee = findViewById(R.id.rcv_employee);
        tv_nameWorkspace = findViewById(R.id.tv_nameWorkspace);
        tv_emailWorkspace = findViewById(R.id.tv_emailWorkspace);
        btnBack = findViewById(R.id.btnBackPressed);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        idWsp = bundle.getInt("wspID");
        if (savedInstanceState != null) {
            idWsp = savedInstanceState.getInt("idWsp");
        }
        mListUsers = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcv_employee.setLayoutManager(linearLayoutManager);

        mEmployeeAdapter = new EmployeeAdapter(mListUsers,getApplicationContext());
        rcv_employee.setAdapter(mEmployeeAdapter);

        getListEmployee();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        reference.child("Users").child(uid).child("email").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    tv_emailWorkspace.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });
        reference.child("Users").child(uid).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    tv_nameWorkspace.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

    }

    private void getListEmployee() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.e("firebase", String.valueOf(idWsp));
        reference.child("Workspaces").child(String.valueOf(idWsp)).child("Employees").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(mListUsers != null){
                    mListUsers.clear();
                }
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    mListUsers.add(user);
                    Log.e("firebase", String.valueOf(idWsp));
                }
                mEmployeeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Get list employees fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("idWsp",idWsp);
    }
}