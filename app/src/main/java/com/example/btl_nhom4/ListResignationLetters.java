package com.example.btl_nhom4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_nhom4.adapter.ResignationLettersAdapter;
import com.example.btl_nhom4.model.letter.Letter;
import com.example.btl_nhom4.model.user.User;
import com.example.btl_nhom4.types.TypeOfLetter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListResignationLetters extends AppCompatActivity {
    // variables for features
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ArrayList<Letter> listResignationLettersFromDatabase;
    private int workspaceId;

    // variables for view
    private String txtViewTypeOfLetters;
    private String txtViewNameStaff;
    private ResignationLettersAdapter resignationLettersAdapter;
    private RecyclerView recyclerViewListResignationLetters;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_resignation_letters);

        listResignationLettersFromDatabase = new ArrayList<Letter>();

        // set by id
        recyclerViewListResignationLetters = findViewById(R.id.recyclerViewListResignationLetters);
        btnBack = findViewById(R.id.btnBackPressed);

        // get data in bundle
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        workspaceId = bundle.getInt("wspID");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewListResignationLetters.setLayoutManager(linearLayoutManager);

        // set view adapter
        resignationLettersAdapter = new ResignationLettersAdapter(listResignationLettersFromDatabase, getApplicationContext());
        recyclerViewListResignationLetters.setAdapter(resignationLettersAdapter);

        // processing
        getListResignationLettersFromFirebase();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void getListResignationLettersFromFirebase() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        reference.child("Letters").child(String.valueOf(workspaceId)).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listResignationLettersFromDatabase.clear();
                for (DataSnapshot data : snapshot.getChildren()){
                    Letter letter = data.getValue(Letter.class);
                    listResignationLettersFromDatabase.add(letter);
                }

                resignationLettersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { // if error
                Toast.makeText(getApplicationContext(),"Get list resignation letters from firebase is fail",Toast.LENGTH_SHORT).show();
            }
        });

    }
}