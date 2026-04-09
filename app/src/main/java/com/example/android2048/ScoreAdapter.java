package com.example.android2048;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android2048.scoreBD.ScoreEntity;

import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {
    private List<ScoreEntity> scoreList;public ScoreAdapter(List<ScoreEntity> scoreList) {
        this.scoreList = scoreList;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.score, parent, false);
        return new ScoreViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        ScoreEntity current = scoreList.get(position);
        holder.pos.setText(String.format("%s - ", position + 1));
        holder.name.setText(current.name); // Assure-toi que ScoreEntity a un champ username
        holder.score.setText(String.valueOf(current.score));
    }

    @Override
    public int getItemCount() { return scoreList.size(); }

    static class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView pos, name, score;
        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            pos = itemView.findViewById(R.id.pos);
            name = itemView.findViewById(R.id.name);
            score = itemView.findViewById(R.id.score);
        }
    }
}