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
import android.widget.Toast;

import com.example.btl_nhom4.adapter.NotificationAdapter;
import com.example.btl_nhom4.adapter.WorkspaceAdapter;
import com.example.btl_nhom4.model.user.Notification;
import com.example.btl_nhom4.model.user.Workspace;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private ImageView btnBack;
    private RecyclerView rcv_notification;
    private List<Notification> mListNotification;
    private NotificationAdapter mNotificationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        btnBack = findViewById(R.id.btnBackPressed);
        rcv_notification = findViewById(R.id.rcv_notification);

        mListNotification = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcv_notification.setLayoutManager(linearLayoutManager);

        mNotificationAdapter = new NotificationAdapter(mListNotification,getApplicationContext());
        rcv_notification.setAdapter(mNotificationAdapter);

        getListNotification();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getListNotification() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference.child("Users").child(uid).child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(mListNotification != null){
                    mListNotification.clear();
                }
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Notification notification = dataSnapshot.getValue(Notification.class);
                    mListNotification.add(notification);
                }
                mNotificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Get list notification fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
}