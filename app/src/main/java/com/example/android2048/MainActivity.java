package com.example.android2048;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
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
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TileAdapter adapter;
    private GestureDetectorCompat gestureDetector;
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

        SharedPreferences gameSave = getSharedPreferences("game_save", Context.MODE_PRIVATE);
        int score = gameSave.getInt("score", 0);
        String strMat = gameSave.getString("jeu", null);

        jeu = new Map();
        scoreView = findViewById(R.id.score_value);
        bestView  = findViewById(R.id.best_value);

        jeu = new Map();
        adapter = new TileAdapter();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Resume ?")
                .setMessage("Voulez-vous reprendre la partie ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    jeu.stringToDeep(strMat);
                    updateBoardUI(jeu.matrice);
                })
                .setNegativeButton("Non", (dialog, which) -> finish());

        if (strMat != null) builder.show();


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

    @Override
    protected void onPause() {
        super.onDestroy();
        SharedPreferences gameSave = getSharedPreferences("game_save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = gameSave.edit();
        editor.putInt("score", 56);
        editor.putString("jeu", Arrays.deepToString(jeu.matrice));
        editor.apply();
    }

    private List<Integer> flatten(int[][] matrice) {
        List<Integer> flatMat = new ArrayList<>();
        for (int[] col : matrice) {
            for (int row : col) flatMat.add(row);

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
        return flatMat;
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