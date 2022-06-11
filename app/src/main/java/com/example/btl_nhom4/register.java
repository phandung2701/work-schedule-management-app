package com.example.btl_nhom4;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Patterns;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_nhom4.model.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {
    EditText name, email, password;
    Button register;
    TextView login;
    boolean isNameValid, isEmailValid, isPasswordValid;
    TextInputLayout nameError, emailError, passError;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (TextView) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        nameError = (TextInputLayout) findViewById(R.id.nameError);
        emailError = (TextInputLayout) findViewById(R.id.emailError);
        passError = (TextInputLayout) findViewById(R.id.passError);
        progressBar = findViewById(R.id.progressBar);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( SetValidation()){
                    String strEmail = email.getText().toString().trim();
                    String strPass = password.getText().toString().trim();
                    String userName = name.getText().toString().trim();

                    mAuth = FirebaseAuth.getInstance();

                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(strEmail, strPass)
                            .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                            User user = new User(userName,strEmail);
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference ref = database.getReference("Users");;
                                            ref.child(mAuth.getCurrentUser().getUid())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Intent intent = new Intent(register.this,MainActivity.class);
                                                    startActivity(intent);
                                                    finishAffinity();
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                                else{
                                                    Toast.makeText(register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            }
                                        });

                                    } else {
                                        Toast.makeText(register.this, "Email already exists", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect to LoginActivity
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
            }
        });
    }

    public boolean SetValidation() {
        // Check for a valid name.
        if (name.getText().toString().isEmpty()) {
            nameError.setError(getResources().getString(R.string.name_error));
            isNameValid = false;
        } else  {
            isNameValid = true;
            nameError.setErrorEnabled(false);
        }

        // Check for a valid email address.
        if (email.getText().toString().isEmpty()) {
            emailError.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            emailError.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        } else  {
            isEmailValid = true;
            emailError.setErrorEnabled(false);
        }
        // Check for a valid password.
        if (password.getText().toString().isEmpty()) {
            passError.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        } else if (password.getText().length() < 6) {
            passError.setError(getResources().getString(R.string.error_invalid_password));
            isPasswordValid = false;
        } else  {
            isPasswordValid = true;
            passError.setErrorEnabled(false);
        }

        if (isNameValid && isEmailValid && isPasswordValid) {
           return true;
        }
        return false;
    }
}
