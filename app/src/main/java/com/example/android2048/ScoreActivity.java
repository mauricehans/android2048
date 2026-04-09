package com.example.android2048;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android2048.scoreBD.AppDB;
import com.example.android2048.scoreBD.ScoreEntity;

public class ScoreActivity extends AppCompatActivity {
    ScoreAdapter adapter;
    RecyclerView scoreTable;
    public void truncateTableDialog(AppDB db) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Réinitialiser la table ?");
        builder.setMessage("Voulez-vous vraiment supprimer la table des scores ?");
        builder.setPositiveButton("Oui", (dialog, which) -> {
            new Thread(() -> {
                db.scoreDAO().truncateTable();
                runOnUiThread(() -> {
                    adapter = new ScoreAdapter(new ArrayList<>());
                    scoreTable.setAdapter(adapter);
                    Toast.makeText(this, "Table réinitialisée", Toast.LENGTH_SHORT).show();
                });
            }).start();
        });
        builder.setNegativeButton("Non", (dialog, which) -> {});
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_table);

        AppDB db = AppDB.getInstance(this);

        scoreTable = findViewById(R.id.score_table);
        TextView resetBtn = findViewById(R.id.reset_score);
        scoreTable.setLayoutManager(new LinearLayoutManager(this));

        resetBtn.setOnClickListener(v -> {
            truncateTableDialog(db);
        });

        new Thread(() -> {
            List<ScoreEntity> list = db.scoreDAO().getScores(10);
            runOnUiThread(() -> {
                adapter = new ScoreAdapter(list);
                scoreTable.setAdapter(adapter);
            });
        }).start();
    }
}