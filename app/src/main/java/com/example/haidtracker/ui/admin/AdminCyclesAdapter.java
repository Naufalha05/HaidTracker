package com.example.haidtracker.ui.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.haidtracker.R;
import com.example.haidtracker.data.model.cycle.Cycle;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AdminCyclesAdapter extends RecyclerView.Adapter<AdminCyclesAdapter.ViewHolder> {

    private List<Cycle> cycleList;
    private SimpleDateFormat dateFormat;
    private OnCycleDeleteListener deleteListener;

    public interface OnCycleDeleteListener {
        void onDeleteCycle(Cycle cycle, int position);
    }

    public AdminCyclesAdapter(List<Cycle> cycleList) {
        this.cycleList = cycleList;
        this.dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    }

    public void setOnCycleDeleteListener(OnCycleDeleteListener listener) {
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_cycle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cycle cycle = cycleList.get(position);
        
        // Format dates
        String startDate = "N/A";
        String endDate = "N/A";
        
        if (cycle.getStartDate() != null) {
            startDate = dateFormat.format(cycle.getStartDate());
        }
        
        if (cycle.getEndDate() != null) {
            endDate = dateFormat.format(cycle.getEndDate());
        }
        
        holder.textUserId.setText("User ID: " + cycle.getUserId());
        holder.textDates.setText(startDate + " - " + endDate);
        holder.textNote.setText("Note: " + (cycle.getNote() != null ? cycle.getNote() : "No note"));
        
        // Set click listener for delete button
        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteCycle(cycle, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cycleList != null ? cycleList.size() : 0;
    }

    public void updateData(List<Cycle> newCycles) {
        this.cycleList = newCycles;
        notifyDataSetChanged();
    }

    public void removeCycle(int position) {
        if (position >= 0 && position < cycleList.size()) {
            cycleList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cycleList.size());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textUserId;
        TextView textDates;
        TextView textNote;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textUserId = itemView.findViewById(R.id.textUserId);
            textDates = itemView.findViewById(R.id.textDates);
            textNote = itemView.findViewById(R.id.textNote);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}