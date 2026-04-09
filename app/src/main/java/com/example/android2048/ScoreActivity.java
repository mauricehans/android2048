package com.example.android2048;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android2048.scoreBD.AppDB;
import com.example.android2048.scoreBD.ScoreEntity;

import java.util.List;

public class ScoreActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_table);

        RecyclerView rv = findViewById(R.id.score_table);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Récupération des données en background
        new Thread(() -> {
            AppDB db = AppDB.getInstance(this);
            List<ScoreEntity> list = db.scoreDAO().getScores(10);

            // Retour sur le thread principal pour l'UI
            runOnUiThread(() -> {
                ScoreAdapter adapter = new ScoreAdapter(list);
                rv.setAdapter(adapter);
            });
        }).start();
    }
}