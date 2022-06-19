package com.example.btl_nhom4;
        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.content.Intent;
        import android.util.Log;
        import android.util.Patterns;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.AutoCompleteTextView;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.EditText;
        import android.widget.ProgressBar;
        import android.widget.Toast;

        import com.example.btl_nhom4.model.user.Notification;
        import com.example.btl_nhom4.model.user.User;
        import com.google.android.material.textfield.TextInputLayout;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;
        import java.util.List;

public class AddEmployees extends AppCompatActivity {
    private AutoCompleteTextView  email;
    private Button AddEmployees;
    private ImageView back_workspace;
    private boolean isNameValid, isEmailValid;
    private TextInputLayout nameError, emailError;
    private List<String> strEmail;
    private List<User> users;
    private int idWsp;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employees);

        email = (AutoCompleteTextView) findViewById(R.id.email);
        back_workspace = (ImageView) findViewById(R.id.back_workspace);
        AddEmployees = (Button) findViewById(R.id.AddEmployees);
        emailError = (TextInputLayout) findViewById(R.id.emailError);
        progressBar = findViewById(R.id.progressBar);
        strEmail = new ArrayList<>();
        users = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        idWsp = bundle.getInt("wspID");
        // get list user
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        reference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    strEmail.add(user.getEmail());
                    users.add(user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // show error when calling api failed
            }
        });

        ArrayAdapter<String> adapterEmail = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, strEmail);


        email.setThreshold(2);
        email.setAdapter(adapterEmail);




       AddEmployees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SetValidation()){
                    progressBar.setVisibility(View.VISIBLE);
                    String uidReceiver = "";
                    String nameReceiver ="";

                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    String emailSender = firebaseAuth.getCurrentUser().getEmail();
                    String idSender = firebaseAuth.getCurrentUser().getUid();
                    String emailReceiver = email.getText().toString().trim();
                    String message = " đã mời bạn tham gia workspace có ID:" +String.valueOf(idWsp);

                    // random number
                    double randomDouble = Math.random();
                    randomDouble = randomDouble * 100 + 1;
                    int randomInt = (int) randomDouble;
                    int idNotification = message.hashCode()+randomInt;
                    Log.e("firebase", String.valueOf(idNotification));
                    for(User user : users){
                        if(user.getEmail().equals(emailReceiver)){
                            uidReceiver = user.getUid();
                            nameReceiver = user.getUsername();
                            break;
                        }
                    }

                    if(nameReceiver != "" && uidReceiver != ""){
                        Notification notification = new Notification(idSender,uidReceiver,message,idNotification,idWsp,emailSender,0,0);
                        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                        DatabaseReference reference1 = database1.getReference();
                        reference1.child("Users").child(uidReceiver).child("Messages").child(String.valueOf(idNotification)).setValue(notification, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Đã mời thành công !!!",Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddEmployees.this);
                                alertDialog.setTitle("Thông báo");
                                alertDialog.setIcon(R.drawable.logo);
                                alertDialog.setMessage("Mời thành công. Bạn có muốn tiếp tục mời nhân viên ??");

                                alertDialog.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        email.setText("",false);
                                    }
                                });
                                alertDialog.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(AddEmployees.this, WorkspaceActivityAdmin.class);
                                        startActivity(intent);
                                    }
                                });
                                alertDialog.show();


                            }
                        });
                    }


                }
            }
        });

        back_workspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public boolean SetValidation() {


        // Check for a valid email address.
        if (email.getText().toString().isEmpty()) {
            emailError.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            emailError.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        }
        else  {
            for(String str : strEmail){
                if(str.equals(email.getText().toString())){
                    isEmailValid = true;
                    emailError.setErrorEnabled(false);
                    break;
                }
            }
            if(!isEmailValid){
                emailError.setError(getResources().getString(R.string.email_exist));
                isEmailValid = false;
            }
        }
         if(isEmailValid){
             return true;
         }
         return false;
    }
}
