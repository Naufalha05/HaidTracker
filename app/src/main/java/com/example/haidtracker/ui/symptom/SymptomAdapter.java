package com.example.haidtracker.ui.symptom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.haidtracker.R;
import com.example.haidtracker.data.model.symptom.Symptom;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class SymptomAdapter extends RecyclerView.Adapter<SymptomAdapter.ViewHolder> {

    private List<Symptom> symptomList;
    private SimpleDateFormat dateFormat;
    private OnSymptomDeleteListener deleteListener;

    public interface OnSymptomDeleteListener {
        void onDeleteSymptom(Symptom symptom, int position);
    }

    public SymptomAdapter(List<Symptom> symptomList) {
        this.symptomList = symptomList;
        this.dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    }

    public void setOnSymptomDeleteListener(OnSymptomDeleteListener listener) {
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_symptom, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Symptom symptom = symptomList.get(position);
        
        // Format tanggal
        String formattedDate = "N/A";
        if (symptom.getDate() != null) {
            formattedDate = dateFormat.format(symptom.getDate());
        }
        
        holder.textDate.setText("📅 " + formattedDate);
        
        // Mood dengan emoji
        String mood = symptom.getMood() != null ? symptom.getMood() : "Tidak ada";
        holder.textMood.setText("😊 Mood: " + mood);
        
        // Gejala
        String symptoms = symptom.getSymptoms() != null && !symptom.getSymptoms().isEmpty() 
                ? symptom.getSymptoms() : "Tidak ada gejala";
        holder.textSymptoms.setText("🩺 Gejala: " + symptoms);
        
        // Set click listener untuk tombol hapus
        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteSymptom(symptom, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return symptomList != null ? symptomList.size() : 0;
    }

    public void updateData(List<Symptom> newSymptoms) {
        this.symptomList = newSymptoms;
        notifyDataSetChanged();
    }

    public void removeSymptom(int position) {
        if (position >= 0 && position < symptomList.size()) {
            symptomList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, symptomList.size());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textDate;
        TextView textMood;
        TextView textSymptoms;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.textDate);
            textMood = itemView.findViewById(R.id.textMood);
            textSymptoms = itemView.findViewById(R.id.textSymptoms);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}