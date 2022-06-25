package com.example.btl_nhom4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_nhom4.model.letter.Letter;
import com.example.btl_nhom4.model.user.User;
import com.example.btl_nhom4.types.TypeOfLetter;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class ResignationLetterActivity extends AppCompatActivity {
    TextInputEditText textInputEditTextReasonLetter;
    TextView textViewNameLeader;
    AutoCompleteTextView autoCompleteTextViewTypeLetter, autoCompleteTextViewTimeLetter;
    Button btnSubmitLetter;
    Calendar calendar;
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    private TypeOfLetter txtTypeOfLetter;
    private String txtDateOfResignation = "";
    private String txtReasonResignation = "";
    private String nameLeader = "Lê Thanh Sơn";
    private ImageView back_pressed;
    private Letter letter;
    private ArrayList<User> users;
    private int idWsp;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    private String[] listTypeNameLetters = {
        "Nghỉ phép",
        "Nghỉ phép nửa ngày",
        "Nghỉ thai sản",
        "Nghỉ không lương",
        "Làm việc tại nhà",
        "Đi học, đào tạo (Do công ty yêu cầu)",
        "Đi công tác",
        "Đi gặp Khách hàng/LV bên ngoài cty",
        "Nghỉ phép và Làm việc tại nhà nửa ngày",
        "Xin đi làm muộn",
        "Xin về sớm"
    };

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // create activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resignation_letter);

        // Find and assign variable by ID
        btnSubmitLetter = findViewById(R.id.buttonSubmitLetter);
        textViewNameLeader = findViewById(R.id.textViewNameLeader);
        textViewNameLeader.setText(nameLeader);

        textInputEditTextReasonLetter = findViewById(R.id.textInputEditTextReasonLetter);
        back_pressed = findViewById(R.id.btnBackPressed);

        autoCompleteTextViewTypeLetter = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewTypeLetter);
        autoCompleteTextViewTimeLetter = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewTimeLetter);

        // setup some features before run the activity

        back_pressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //// set for button
        btnSubmitLetter.setEnabled(false);
        btnSubmitLetter.setBackgroundTintList(ContextCompat.getColorStateList(ResignationLetterActivity.this, R.color.black_gray));

        //// fill list type of letters
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, R.id.textViewListNameTypeLetters, listTypeNameLetters);
        autoCompleteTextViewTypeLetter.setAdapter(adapter);


        // set on event for listener
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                autoCompleteTextViewTimeLetter.setText(day + "/" + month + "/" + year);
            }
        };

        autoCompleteTextViewTimeLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), dateSetListener, year, month, day);
                datePickerDialog.show();
            }
        });


        textInputEditTextReasonLetter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (
                    autoCompleteTextViewTypeLetter.getText().toString().length() > 0 &&
                    autoCompleteTextViewTimeLetter.getText().toString().length() > 0 &&
                    textInputEditTextReasonLetter.getText().toString().length() > 0
                ) {
                    btnSubmitLetter.setEnabled(true);
                    btnSubmitLetter.setBackgroundTintList(ContextCompat.getColorStateList(ResignationLetterActivity.this, R.color.green_3));
                } else {
                    btnSubmitLetter.setEnabled(false);
                    btnSubmitLetter.setBackgroundTintList(ContextCompat.getColorStateList(ResignationLetterActivity.this, R.color.black_gray));
                }
            }
        });

        autoCompleteTextViewTypeLetter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (
                        textInputEditTextReasonLetter.getText().toString().length() > 0 &&
                        autoCompleteTextViewTimeLetter.getText().toString().length() > 0 &&
                        autoCompleteTextViewTypeLetter.getText().toString().length() > 0
                ) {
                    btnSubmitLetter.setEnabled(true);
                    btnSubmitLetter.setBackgroundTintList(ContextCompat.getColorStateList(ResignationLetterActivity.this, R.color.green_3));
                } else {
                    btnSubmitLetter.setEnabled(false);
                    btnSubmitLetter.setBackgroundTintList(ContextCompat.getColorStateList(ResignationLetterActivity.this, R.color.black_gray));
                }
            }
        });

        autoCompleteTextViewTimeLetter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (
                        textInputEditTextReasonLetter.getText().toString().length() > 0 &&
                        autoCompleteTextViewTypeLetter.getText().toString().length() > 0 &&
                        autoCompleteTextViewTimeLetter.getText().toString().length() > 0
                ) {
                    btnSubmitLetter.setEnabled(true);
                    btnSubmitLetter.setBackgroundTintList(ContextCompat.getColorStateList(ResignationLetterActivity.this, R.color.green_3));
                } else {
                    btnSubmitLetter.setEnabled(false);
                    btnSubmitLetter.setBackgroundTintList(ContextCompat.getColorStateList(ResignationLetterActivity.this, R.color.black_gray));
                }
            }
        });
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        idWsp = bundle.getInt("wspID");
        //name manage
        reference.child("Workspaces").child(String.valueOf(idWsp)).child("admin").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        Log.e("firebase",task.getResult().getValue().toString() );
                        if(task.isSuccessful()){
                            reference.child("Users").child(task.getResult().getValue().toString())
                                    .child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if(task.isSuccessful()){
                                        textViewNameLeader.setText(task.getResult().getValue().toString());
                                    }
                                }
                            });
                        }
                    }
                });
        btnSubmitLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (
                    autoCompleteTextViewTypeLetter.getText().toString().length() > 0 &&
                    autoCompleteTextViewTimeLetter.getText().toString().length() > 0 &&
                    textInputEditTextReasonLetter.getText().toString().length() > 0
                ) {
                    int indexTypeOfLetter = Arrays.asList(listTypeNameLetters).indexOf(autoCompleteTextViewTypeLetter.getText().toString());

                    txtTypeOfLetter = TypeOfLetter.values()[indexTypeOfLetter];
                    txtDateOfResignation = autoCompleteTextViewTimeLetter.getText().toString();
                    txtReasonResignation = textInputEditTextReasonLetter.getText().toString();

                    // get data in bundle


                    // Config firebase

                    String userId = mFirebaseAuth.getUid().toString();

                    reference.child("Users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            users = new ArrayList<User>();
                            for (DataSnapshot data : snapshot.getChildren()){
                                User user = data.getValue(User.class);
                                users.add(user);
                                if (user.getUid().equals(userId)) {
                                    letter =
                                        new Letter(
                                                txtTypeOfLetter,
                                                txtDateOfResignation,
                                                txtReasonResignation,
                                                idWsp,
                                                userId,
                                                user.getUsername()
                                        );
                                }
                            }
                            String hashCode = userId + '.' + idWsp + '.' + txtDateOfResignation + '.' + txtReasonResignation;
                            hashCode = String.valueOf(hashCode.hashCode());

                            // saving data in database firebase
                            reference
                                .child("Letters")
                                .child(String.valueOf(idWsp))
                                .child(hashCode)
                                .setValue(letter)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(ResignationLetterActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finishAffinity();
                                        } else {
                                            Toast.makeText(
                                                    ResignationLetterActivity.this,
                                                    "Error!.", Toast.LENGTH_SHORT
                                            ).show();
                                        }
                                    }
                                });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

    }
} );
    }}