package com.example.btl_nhom4.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_nhom4.R;
import com.example.btl_nhom4.model.user.MonthlySummary;

import java.util.ArrayList;

public class SummaryForAdminAdapter extends RecyclerView.Adapter<SummaryForAdminAdapter.SummaryEmploymentViewHolder>{
    private ArrayList<MonthlySummary> monthlySummaries;
    private Context context;
    private ArrayList<Integer> listCheckDuplicateMonth;

    public SummaryForAdminAdapter(Context context, ArrayList<MonthlySummary> monthlySummaries) {
        this.context = context;
        this.monthlySummaries = monthlySummaries;
        this.listCheckDuplicateMonth = new ArrayList<Integer>();
    }

    @NonNull
    @Override
    public SummaryEmploymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_for_admin_item,parent,false);
        return new SummaryForAdminAdapter.SummaryEmploymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryEmploymentViewHolder holder, int position) {
        MonthlySummary monthlySummary = monthlySummaries.get(position);
        String month = "Tháng " + monthlySummary.getMonth();

        if (listCheckDuplicateMonth.contains(monthlySummary.getMonth())) {
            holder.textViewMonthSummaryAdmin.setVisibility(View.GONE);
        } else {
            listCheckDuplicateMonth.add(monthlySummary.getMonth());
            holder.textViewMonthSummaryAdmin.setText(month);
        }

        holder.textViewUsernameSummaryAdmin.setText(monthlySummary.getUsername());
        holder.workOnTimeForAdmin.setText(String.valueOf(monthlySummary.getNumWorkOnTime()));
        holder.offWorkForAdmin.setText(String.valueOf(monthlySummary.getNumOffWork()));
        holder.lateForWorkForAdmin.setText(String.valueOf(monthlySummary.getNumLateForWork()));
    }

    @Override
    public int getItemCount() {
        if(monthlySummaries != null){
            return monthlySummaries.size();
        }
        return 0;
    }

    public class SummaryEmploymentViewHolder extends RecyclerView.ViewHolder {

        TextView
            textViewMonthSummaryAdmin,
            textViewUsernameSummaryAdmin,
            lateForWorkForAdmin,
            workOnTimeForAdmin,
            offWorkForAdmin;

        public SummaryEmploymentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMonthSummaryAdmin = itemView.findViewById(R.id.textViewMonthSummaryAdmin);
            textViewUsernameSummaryAdmin = itemView.findViewById(R.id.textViewUsernameSummaryAdmin);
            lateForWorkForAdmin = itemView.findViewById(R.id.lateForWorkForAdmin);
            workOnTimeForAdmin = itemView.findViewById(R.id.workOnTimeForAdmin);
            offWorkForAdmin = itemView.findViewById(R.id.offWorkForAdmin);
        }
    }
}
