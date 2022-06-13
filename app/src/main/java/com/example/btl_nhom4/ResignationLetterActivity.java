package com.example.btl_nhom4;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class ResignationLetterActivity extends AppCompatActivity {
    private TextInputEditText textInputEditTextReasonLetter;
    private TextView textViewNameLeader;
    private AutoCompleteTextView autoCompleteTextViewTypeLetter, autoCompleteTextViewTimeLetter;
    private Calendar calendar;
    private ImageView back_pressed;
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
    private String nameLeader = "Lê Thanh Sơn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resignation_letter);

        textViewNameLeader = findViewById(R.id.textViewNameLeader);
        textViewNameLeader.setText(nameLeader);
        back_pressed = findViewById(R.id.btnBackPressed);

        autoCompleteTextViewTypeLetter = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewTypeLetter);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, R.id.textViewListNameTypeLetters, listTypeNameLetters);
        autoCompleteTextViewTypeLetter.setAdapter(adapter);

        autoCompleteTextViewTimeLetter = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewTimeLetter);
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
        back_pressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}