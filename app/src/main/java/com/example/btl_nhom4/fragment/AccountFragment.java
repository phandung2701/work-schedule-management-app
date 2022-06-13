package com.example.btl_nhom4.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.btl_nhom4.R;
import com.example.btl_nhom4.login;
import com.example.btl_nhom4.model.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AccountFragment extends Fragment {


    TextView userName,email;
    LinearLayout signout;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        signout = view.findViewById(R.id.signout);
        userName = view.findViewById(R.id.fragment_account_userName);
        email = view.findViewById(R.id.fragment_account_email);

        String uiId = FirebaseAuth.getInstance().getCurrentUser().getUid();
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


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Thông báo");
                alertDialog.setIcon(R.drawable.logo);
                alertDialog.setMessage("Bạn có muốn đăng xuất");

                alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                    }
                });
                alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBar.setVisibility(View.VISIBLE);
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity().getApplication(), login.class);
                        startActivity(intent);
                        getActivity().finishAffinity();
                        progressBar.setVisibility(View.GONE);
                    }
                });
                alertDialog.show();




            }
        });
        return view;


    }
}