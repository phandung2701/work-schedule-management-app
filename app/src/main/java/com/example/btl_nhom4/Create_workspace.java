package com.example.btl_nhom4;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;

public class Create_workspace extends AppCompatActivity {
    EditText nameCompany, email,name_workspace;
    Button CreateWorkspace;
    ImageView back_workspace;
    boolean isNameCompanyValid, isEmailValid, isNameWorkspaceValid;
    TextInputLayout nameCompanyError, emailError, name_workspaceError;
    AutoCompleteTextView autocomplete;

    String[] arr = { "An Giang", "Bà rịa – Vũng tàu", "Bắc Giang", "Bắc Kạn",
            "Bạc Liêu", "Bắc Ninh", "Bến Tre", "Bình Định", "Bình Dương", "Bình Phước", "Bình Thuận",
            "Cà Mau" , "Cần Thơ" , "Cao Bằng" , "Đà Nẵng" , "Điện Biên" , "Hà Giang" , "Hà Nam" ,
            "Hà Nội", "Hà Tĩnh" , "Hải Dương" , "Hải Phòng" , "Hậu Giang" , "Hòa Bình" , "Hưng Yên" ,
            "Lạng Sơn" , "Nam Định" , "Nghệ An" , "Ninh Bình" , "Quảng Bình" , "Quảng Ninh" ,
            "Quảng Trị" , "Sơn La" , "Tây Ninh" , "Thái Bình" , "Thái Nguyên" , "Thanh Hóa" , "Thừa Thiên Huế" ,
            "Tiền Giang" , "Thành phố Hồ Chí Minh" , "Trà Vinh" , "Tuyên Quang" , "Vĩnh Long" , "Vĩnh Phúc", "Yên Bái"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workspace);
        autocomplete = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, arr);

        autocomplete.setThreshold(2);
        autocomplete.setAdapter(adapter);

        nameCompany = (EditText) findViewById(R.id.nameCompany);
        email = (EditText) findViewById(R.id.email);
        name_workspace = (EditText) findViewById(R.id.name_workspace);
        back_workspace = (ImageView) findViewById(R.id.back_workspace);
        CreateWorkspace = (Button) findViewById(R.id.CreateWorkspace);
        nameCompanyError = (TextInputLayout) findViewById(R.id.nameCompanyError);
        emailError = (TextInputLayout) findViewById(R.id.emailError);
        name_workspaceError = (TextInputLayout) findViewById(R.id.name_workspaceError);

        CreateWorkspace.setOnClickListener(new View.OnClickListener() {
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
        // Check for a valid name company.
        if (nameCompany.getText().toString().isEmpty()) {
            nameCompanyError.setError(getResources().getString(R.string.name_error));
            isNameCompanyValid = false;
        } else  {
            isNameCompanyValid = true;
            nameCompanyError.setErrorEnabled(false);
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
        // Check for a valid name workspace.
        if (name_workspace.getText().toString().isEmpty()) {
            name_workspaceError.setError(getResources().getString(R.string.name_error));
            isNameWorkspaceValid = false;
        } else  {
            isNameWorkspaceValid = true;
            name_workspaceError.setErrorEnabled(false);
        }

    }
}
