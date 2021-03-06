package com.example.btl_nhom4.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
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
    ArrayList<String> checkDuplicateMonth = new ArrayList<String>();

    public ResignationLettersAdapter(ArrayList<Letter> listResignationLetters, Context context) {
        this.listResignationLetters = listResignationLetters;
        this.context = context;
    }

    @NonNull
    @Override
    public ResignationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resignation_letters_item,parent,false);
        return new ResignationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResignationViewHolder holder, int position) {
        Letter letter = listResignationLetters.get(position);

        if (letter == null) {
            return;
        }

        // get value in each item of resignation letters
        String nameStaff = letter.getUsername();
        String reasonResignation = setTextTypeOfLetterForView(letter.getTypeOfLetter());

        // split date
        // Example: 11/03/2001 -> ['11', '03', '2001']
        String[] date = letter.getTimeOfLetter().split("/");
        String day = date[0];
        String month = date[1];
        String year = date[2];

        // check duplicate of the month
        Log.println(Log.ERROR, "result", checkDuplicateMonth.toString());
        if (checkDuplicateMonth.contains(month)) {
            holder.textViewMonthOfResignation.setVisibility(View.GONE);
        } else {
            checkDuplicateMonth.add(month);
            holder.textViewMonthOfResignation.setText("Th??ng " + month);
        }

        holder.textViewDayOfResignation.setText("Ng??y " + day);
        holder.textViewNameStaffResignation.setText(nameStaff);
        holder.textViewReasonResignation.setText(reasonResignation);
    }

    @Override
    public int getItemCount() {
        if(listResignationLetters != null){
            return listResignationLetters.size();
        }
        return 0;
    }

    private String setTextTypeOfLetterForView(TypeOfLetter typeOfLetter) {
        switch (typeOfLetter) {
            case ON_LEAVE:
                return "Ngh??? ph??p";
            case ON_LEAVE_HALF_DAY:
                return "Ngh??? ph??p n???a ng??y";
            case MATERNITY_LEAVE:
                return "Ngh??? thai s???n";
            case UNPAID_LEAVE:
                return "Ngh??? kh??ng l????ng";
            case WORK_AT_HOME:
                return "L??m vi???c t???i nh??";
            case LEARN_TRAIN:
                return "??i h???c, ????o t???o";
            case BUSINESS_TRAVEL:
                return "??i c??ng t??c";
            case MEET_CUSTOMERS:
                return "??i g???p kh??ch";
            case ON_LEAVE_AND_WORK_AT_HOME_HALF_FAY:
                return "ngh??? ph??p v?? l??m vi???c t???i nh?? n???a ng??y";
            case LATE_FOR_WORK:
                return "xin ??i l??m mu???n";
            case LEAVE_A_EARLY:
                return "xin v??? s???m";
            default:
                return "Kh??c";
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
