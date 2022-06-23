package com.example.btl_nhom4.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_nhom4.R;
import com.example.btl_nhom4.model.letter.Letter;
import com.example.btl_nhom4.model.user.User;
import com.example.btl_nhom4.types.TypeOfLetter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResignationLettersAdapter extends RecyclerView.Adapter<ResignationLettersAdapter.ResignationViewHolder> {
    private ArrayList<Letter> listResignationLetters;
    private Context context;

    public ResignationLettersAdapter(ArrayList<Letter> listResignationLetters, Context context) {
        this.listResignationLetters = listResignationLetters;
        this.context = context;
    }

    @NonNull
    @Override
    public ResignationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ResignationViewHolder holder, int position) {
        Letter letter = listResignationLetters.get(position);

        if (letter == null) {
            return;
        }

        // get value in each item of resignation letters
        String nameStaff = getDataFromFirebaseDatabaseByIdUser(letter.getUserId());
        String reasonResignation = setTextTypeOfLetterForView(letter.getTypeOfLetter());

        // split date
        // Example: 11/03/2001 -> ['11', '03', '2001']
        String[] date = letter.getTimeOfLetter().split("/");
        String day = date[0];
        String month = date[1];
        String year = date[2];

        // check duplicate of the month
        ArrayList<String> checkDuplicateMonth = new ArrayList<String>();

        if (checkDuplicateMonth.contains(month)) {
            holder.textViewMonthOfResignation.setVisibility(View.GONE);
        } else {
            checkDuplicateMonth.add(month);
            holder.textViewMonthOfResignation.setText(month);
        }

        holder.textViewDayOfResignation.setText(day);
        holder.textViewNameStaffResignation.setText(nameStaff);
        holder.textViewReasonResignation.setText(reasonResignation);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public String getDataFromFirebaseDatabaseByIdUser(String userId) {
        final String[] username = {""};
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                 username[0] = user.getUsername();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("firebase", error.getMessage());
            }
        });

        return username[0];
    }

    private String setTextTypeOfLetterForView(TypeOfLetter typeOfLetter) {
        switch (typeOfLetter) {
            case ON_LEAVE:
                return "Nghỉ phép";
            case ON_LEAVE_HALF_DAY:
                return "Nghỉ phép nửa ngày";
            case MATERNITY_LEAVE:
                return "Nghỉ thai sản";
            case UNPAID_LEAVE:
                return "Nghỉ không lương";
            case WORK_AT_HOME:
                return "Làm việc tại nhà";
            case LEARN_TRAIN:
                return "Đi học, đào tạo";
            case BUSINESS_TRAVEL:
                return "Đi công tác";
            case MEET_CUSTOMERS:
                return "Đi gặp khách";
            case ON_LEAVE_AND_WORK_AT_HOME_HALF_FAY:
                return "nghỉ phép và làm việc tại nhà nửa ngày";
            case LATE_FOR_WORK:
                return "xin đi làm muộn";
            case LEAVE_A_EARLY:
                return "xin về sớm";
            default:
                return "Khác";
        }
    }

    public class ResignationViewHolder extends RecyclerView.ViewHolder{
        TextView
            textViewMonthOfResignation,
            textViewDayOfResignation,
            textViewNameStaffResignation,
            textViewReasonResignation;

        public ResignationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMonthOfResignation = itemView.findViewById(R.id.monthOfResignation);
            textViewDayOfResignation = itemView.findViewById(R.id.dayOfResignation);
            textViewNameStaffResignation = itemView.findViewById(R.id.nameStaffResignation);
            textViewReasonResignation = itemView.findViewById(R.id.reasonResignation);
        }
    }

}
