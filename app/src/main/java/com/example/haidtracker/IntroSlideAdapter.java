package com.example.haidtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IntroSlideAdapter extends RecyclerView.Adapter<IntroSlideAdapter.IntroSlideViewHolder> {

    private Context context;
    private List<IntroSlide> introSlides;

    public IntroSlideAdapter(Context context, List<IntroSlide> introSlides) {
        this.context = context;
        this.introSlides = introSlides;
    }

    @NonNull
    @Override
    public IntroSlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IntroSlideViewHolder(
                LayoutInflater.from(context).inflate(R.layout.layout_intro_slide, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull IntroSlideViewHolder holder, int position) {
        holder.bind(introSlides.get(position));
    }

    @Override
    public int getItemCount() {
        return introSlides.size();
    }

    class IntroSlideViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView titleTextView;
        private TextView descriptionTextView;

        public IntroSlideViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.intro_image);
            titleTextView = itemView.findViewById(R.id.intro_title);
            descriptionTextView = itemView.findViewById(R.id.intro_description);
        }

        void bind(IntroSlide introSlide) {
            imageView.setImageResource(introSlide.getImageResId());
            titleTextView.setText(introSlide.getTitle());
            descriptionTextView.setText(introSlide.getDescription());
        }
    }
}