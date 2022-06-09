package com.example.btl_nhom4;
        import androidx.appcompat.app.AppCompatActivity;
        import android.os.Bundle;
        import android.content.Intent;
        import android.util.Patterns;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;
        import android.widget.EditText;
        import com.google.android.material.textfield.TextInputLayout;
public class AddEmployees extends AppCompatActivity {
    EditText name, email;
    Button AddEmployees;
    ImageView back_workspace;
    boolean isNameValid, isEmailValid;
    TextInputLayout nameError, emailError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employees);

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        back_workspace = (ImageView) findViewById(R.id.back_workspace);
        AddEmployees = (Button) findViewById(R.id.AddEmployees);
        nameError = (TextInputLayout) findViewById(R.id.nameError);
        emailError = (TextInputLayout) findViewById(R.id.emailError);

       AddEmployees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetValidation();
            }
        });

        back_workspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //redirect to MainActivity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void SetValidation() {
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
    }
}
