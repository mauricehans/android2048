package com.example.android2048;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TileAdapter adapter;
    private GestureDetectorCompat gestureDetector;
    private TextView scoreView, bestView;
    private int bestScore = 0;
    private Map jeu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        scoreView = findViewById(R.id.score_value);
        bestView  = findViewById(R.id.best_value);

        jeu = new Map();
        adapter = new TileAdapter();

        RecyclerView board = findViewById(R.id.board);
        board.setLayoutManager(new GridLayoutManager(this, 4));
        board.setHasFixedSize(true);
        board.setAdapter(adapter);
        updateBoardUI();
        updateScoreUI();

        gestureDetector = new GestureDetectorCompat(this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(@Nullable MotionEvent e1,
                                           @NonNull  MotionEvent e2,
                                           float velocityX, float velocityY) {
                        if (e1 == null) return false;

                        float Y = e2.getY() - e1.getY();
                        float X = e2.getX() - e1.getX();
                        boolean moved = false;

                        if (Math.abs(Y) > Math.abs(X) && Math.abs(Y) > 100) {
                            moved = (Y > 0) ? jeu.bas() : jeu.haut();
                        } else if (Math.abs(X) > Math.abs(Y) && Math.abs(X) > 100) {
                            moved = (X > 0) ? jeu.droite() : jeu.gauche();
                        }

                        if (moved) {
                            updateBoardUI();
                            updateScoreUI();

                            if (jeu.objectifAtteint) {
                                int objectifAtteint = jeu.objectif;
                                jeu.objectif *= 2;
                                jeu.objectifAtteint = false;
                                afficherObjectif(objectifAtteint, jeu.objectif);
                            } else if (!jeu.peutJouer()) {
                                afficherGameOver();
                            }
                        }
                        return true;
                    }
                });

        board.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    private void updateBoardUI() {
        List<Integer> flat = new ArrayList<>();
        for (int[] row : jeu.matrice)
            for (int val : row) flat.add(val);
        adapter.submitList(flat);
    }

    private void updateScoreUI() {
        scoreView.setText(String.valueOf(jeu.score));
        if (jeu.score > bestScore) {
            bestScore = jeu.score;
            bestView.setText(String.valueOf(bestScore));
        }
    }

    private void afficherObjectif(int atteint, int prochain) {
        new AlertDialog.Builder(this)
                .setTitle("🎉 Objectif atteint !")
                .setMessage("Bravo ! Tu as atteint un score de " + atteint + " !\n\n"
                        + "Nouvel objectif : " + prochain)
                .setPositiveButton("Continuer", null)
                .setCancelable(false)
                .show();
    }

    private void afficherGameOver() {
        new AlertDialog.Builder(this)
                .setTitle("💀 Game Over")
                .setMessage("Plus aucun mouvement possible.\nScore final : " + jeu.score)
                .setPositiveButton("Rejouer", (dialog, which) -> {
                    jeu = new Map();
                    updateBoardUI();
                    updateScoreUI();
                })
                .setCancelable(false)
                .show();
    }
}