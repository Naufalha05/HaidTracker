package com.example.haidtracker.ui.siklus;

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

public class siklusAdapter extends RecyclerView.Adapter<siklusAdapter.ViewHolder> {

    private List<Cycle> cycleList;
    private SimpleDateFormat dateFormat;
    private OnCycleDeleteListener deleteListener;

    public interface OnCycleDeleteListener {
        void onDeleteCycle(Cycle cycle, int position);
    }

    public siklusAdapter(List<Cycle> cycleList) {
        this.cycleList = cycleList;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    public void setOnCycleDeleteListener(OnCycleDeleteListener listener) {
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cycle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cycle cycle = cycleList.get(position);
        
        String startDate = "N/A";
        String endDate = "N/A";
        
        try {
            if (cycle.getStartDate() != null) {
                startDate = dateFormat.format(cycle.getStartDate());
            }
            
            if (cycle.getEndDate() != null) {
                endDate = dateFormat.format(cycle.getEndDate());
            }
        } catch (Exception e) {
            // Jika format gagal, coba tampilkan string tanggal mentah
            startDate = cycle.getStartDate() != null ? cycle.getStartDate().toString() : "N/A";
            endDate = cycle.getEndDate() != null ? cycle.getEndDate().toString() : "N/A";
        }
                
        holder.textTanggal.setText("Tanggal: " + startDate + " - " + endDate);
        holder.textNote.setText("Catatan: " + (cycle.getNote() != null ? cycle.getNote() : ""));
        
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
        TextView textTanggal;
        TextView textNote;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTanggal = itemView.findViewById(R.id.textTanggal);
            textNote = itemView.findViewById(R.id.textNote);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
