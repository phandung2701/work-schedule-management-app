package com.example.btl_nhom4.fragment;



import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_nhom4.Create_workspace;
import com.example.btl_nhom4.NotificationActivity;
import com.example.btl_nhom4.R;
import com.example.btl_nhom4.adapter.WorkspaceAdapter;
import com.example.btl_nhom4.model.user.User;
import com.example.btl_nhom4.model.user.Workspace;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private TextView userName,email;
    private CardView createWorkspace;
    private RecyclerView rcv_workspace;
    private WorkspaceAdapter mWorkspaceAdapter;
    private List<Workspace> mListWorkspace;
    private ImageView icon_notification;
    ProgressBar progressBar;
    String uiId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        userName = view.findViewById(R.id.fragment_home_userName);
        email = view.findViewById(R.id.fragment_home_email);
        createWorkspace = view.findViewById(R.id.CreateWorkspace);
        rcv_workspace = view.findViewById(R.id.rcv_workspace);
        icon_notification = view.findViewById(R.id.call_notification);
        progressBar = view.findViewById(R.id.progressBar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcv_workspace.setLayoutManager(linearLayoutManager);


        mListWorkspace = new ArrayList<>();
        mWorkspaceAdapter = new WorkspaceAdapter(mListWorkspace,getContext());
        rcv_workspace.setAdapter(mWorkspaceAdapter);
        uiId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        progressBar.setVisibility(View.VISIBLE);
        getListWorkspace();
        progressBar.setVisibility(View.GONE);


        FirebaseDatabase.getInstance().getReference().child("Users").child(uiId)
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                userName.setText(user.getUsername());
                email.setText(user.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


       createWorkspace.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getActivity().getApplication(), Create_workspace.class);
               startActivity(intent);
           }
       });
        icon_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), NotificationActivity.class);
                startActivity(intent);
            }
        });
        ImageView imageViewShowBottomSheetListWorkspace = view.findViewById(R.id.ImageViewShowBottomSheetListWorkspace);

        imageViewShowBottomSheetListWorkspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheet = new BottomSheetDialog();
                bottomSheet.show(getActivity().getSupportFragmentManager(), "ModalBottomSheet");
            }
        });

        return view;
    }
    private void getListWorkspace(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        reference.child("Workspaces").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot workspaceSnapshot: snapshot.getChildren()) {
                    Workspace workspace = workspaceSnapshot.getValue(Workspace.class);
                    if (workspace.getAdmin().equals(uiId)){
                        mListWorkspace.add(workspace);
                    }
                }
                mWorkspaceAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"Get list workspace fail",Toast.LENGTH_SHORT).show();
                Log.e("firebase", "fail");
            }
        });
        reference.child("Users").child(uiId).child("Workspaces").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot workspaceSnapshot: snapshot.getChildren()) {
                    Workspace workspace = workspaceSnapshot.getValue(Workspace.class);
                        mListWorkspace.add(workspace);
                }
                mWorkspaceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}