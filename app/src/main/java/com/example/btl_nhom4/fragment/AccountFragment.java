package com.example.btl_nhom4.fragment;

import android.content.Intent;
import android.os.Bundle;


import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.example.btl_nhom4.R;
import com.example.btl_nhom4.login;


public class AccountFragment extends Fragment {

    public AccountFragment() {
        // Required empty public constructor
    }

    LinearLayout layout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        layout = view.findViewById(R.id.signout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplication(), login.class);
                startActivity(intent);
            }
        });
        return view;


    }
}