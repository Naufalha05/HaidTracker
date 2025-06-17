package com.example.haidtracker.ui.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.haidtracker.R;
import com.example.haidtracker.data.model.analytics.Analytics;

import java.util.List;

public class AdminAnalyticsAdapter extends RecyclerView.Adapter<AdminAnalyticsAdapter.ViewHolder> {

    private List<Analytics> analyticsList;

    public AdminAnalyticsAdapter(List<Analytics> analyticsList) {
        this.analyticsList = analyticsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_analytics, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Analytics analytics = analyticsList.get(position);
        
        // Display analytics data
        holder.tvAverageCycle.setText(analytics.getAverageCycleLength() != null ? 
                String.format("%.1f hari", analytics.getAverageCycleLength()) : "N/A");
        
        holder.tvLastCycle.setText(analytics.getLastCycleLength() != null ? 
                String.format("%d hari", analytics.getLastCycleLength()) : "N/A");
        
        holder.tvTotalCycles.setText(analytics.getTotalCycles() != null ? 
                String.valueOf(analytics.getTotalCycles()) : "0");
        
        holder.tvNextPredicted.setText(analytics.getNextPredictedDate() != null ? 
                analytics.getNextPredictedDate() : "Belum dapat diprediksi");
        
        holder.tvRegularityScore.setText(analytics.getRegularityScore() != null ? 
                String.format("%.1f%%", analytics.getRegularityScore() * 100) : "N/A");
    }

    @Override
    public int getItemCount() {
        return analyticsList != null ? analyticsList.size() : 0;
    }

    public void updateData(List<Analytics> newAnalytics) {
        this.analyticsList = newAnalytics;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAverageCycle;
        TextView tvLastCycle;
        TextView tvTotalCycles;
        TextView tvNextPredicted;
        TextView tvRegularityScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAverageCycle = itemView.findViewById(R.id.tvAverageCycle);
            tvLastCycle = itemView.findViewById(R.id.tvLastCycle);
            tvTotalCycles = itemView.findViewById(R.id.tvTotalCycles);
            tvNextPredicted = itemView.findViewById(R.id.tvNextPredicted);
            tvRegularityScore = itemView.findViewById(R.id.tvRegularityScore);
        }
    }
}