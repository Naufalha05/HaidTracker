package com.example.haidtracker.ui.siklus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.haidtracker.R;
import com.example.haidtracker.data.model.cycle.Cycle;


import java.util.List;

public class siklusAdapter extends RecyclerView.Adapter<siklusAdapter.ViewHolder> {

    private List<Cycle> cycleList;

    public siklusAdapter(List<Cycle> cycleList) {
        this.cycleList = cycleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cycle, parent, false); // item_cycle.xml harus ada
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cycle cycle = cycleList.get(position);
        holder.textTanggal.setText("Tanggal: " + cycle.getStartDate());
        holder.textNote.setText("Catatan: " + cycle.getNote());
    }

    @Override
    public int getItemCount() {
        return cycleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTanggal, textNote;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTanggal = itemView.findViewById(R.id.text_tanggal);
            textNote = itemView.findViewById(R.id.text_note);
        }
    }
}
