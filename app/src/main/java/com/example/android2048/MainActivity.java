package com.example.android2048;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android2048.scoreBD.AppDB;
import com.example.android2048.scoreBD.ScoreDAO;
import com.example.android2048.scoreBD.ScoreEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Map jeu;
    private TileAdapter adapter;
    private TextView scoreView, bestView, scoreTableView;
    private int bestScore;
    private GestureDetector gestureDetector;

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

    private void resumeGameDialog(Map jeu, int score, String strMat) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Resume ?")
                .setMessage("Voulez-vous reprendre la partie ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    assert strMat != null;
                    jeu.stringToDeep(strMat);
                    jeu.score = score;
                    updateBoardUI();
                    updateScoreUI();
                })
                .setNegativeButton("Non", (dialog, which) -> {});

        if (strMat != null) builder.show();
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

    /*private void afficherGameOver() {
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
    }*/

    private void afficherGameOver(int finalScore) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("GAME OVER");
        builder.setMessage("Votre score : " + finalScore + "\nEntrez vos initiales (3 lettres) :");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        input.setFilters(new InputFilter[] { new InputFilter.LengthFilter(3) });
        input.setGravity(Gravity.CENTER);
        builder.setView(input);

        builder.setPositiveButton("Enregistrer", (dialog, which) -> {
            String name = input.getText().toString().trim().toUpperCase();
            if (name.isEmpty()) name = "AAA";
            saveScore(name, finalScore);
            jeu = new Map();
            updateBoardUI();
            updateScoreUI();
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void saveScore(String name, int score) {
        new Thread(() -> {
            AppDB db = AppDB.getInstance(getApplicationContext());
            db.scoreDAO().insert(new ScoreEntity(name, score));
        }).start();
    }

    @SuppressLint("ClickableViewAccessibility")
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
        /* **** PERSISTANCE ***** */
        SharedPreferences gameSave = getSharedPreferences("game_save", Context.MODE_PRIVATE);
        int score = gameSave.getInt("score", 0);
        bestScore = gameSave.getInt("best_score", 0);
        String strMat = gameSave.getString("jeu", null);
        AppDB db = AppDB.getInstance(getApplicationContext());
        ScoreDAO DAO = db.scoreDAO();
        /* **** PERSISTANCE ***** */

        /* **** INIT **** */
        jeu = new Map();
        scoreView = findViewById(R.id.score_value);
        bestView  = findViewById(R.id.best_value);
        bestView.setText(String.valueOf(bestScore));
        scoreTableView = findViewById(R.id.score_table_btn);
        /* **** INIT **** */

        resumeGameDialog(jeu, score, strMat);

        /* **** BOARD INIT **** */
        adapter = new TileAdapter();
        RecyclerView board = findViewById(R.id.board);
        board.setLayoutManager(new GridLayoutManager(this, 4));
        board.setHasFixedSize(true);
        board.setAdapter(adapter);
        updateBoardUI();
        updateScoreUI();
        /* **** BOARD INIT **** */

        /* **** SWIPE CONTROL **** */
        // LISTENER
        board.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) v.performClick(); // Pour des limites d'accessibilite (selon le linter)
            return gestureDetector.onTouchEvent(event);
        });

        // CALLBACK
        gestureDetector = new GestureDetector(this,
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(@Nullable MotionEvent e1,
                                       @NonNull  MotionEvent e2,
                                       float velocityX,
                                       float velocityY) {
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
                            afficherGameOver(score);
                        }
                    }
                    return true;
                }
            });
        /* **** SWIPE CONTROL **** */

        /* **** MENU CONTROLS **** */
        scoreTableView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences gameSave = getSharedPreferences("game_save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = gameSave.edit();
        editor.putInt("score", jeu.score);
        editor.putInt("best_score", bestScore);
        editor.putString("jeu", Arrays.deepToString(jeu.matrice));
        editor.apply();
    }
}