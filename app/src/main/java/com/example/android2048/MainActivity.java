package com.example.android2048;

import static android.content.ContentValues.TAG;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TileAdapter adapter;
    private GestureDetectorCompat gestureDetector;
    private GameViewModel viewModel;
    private TextView tvScore;
    private TextView tvBest;

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

        viewModel = new ViewModelProvider(this).get(GameViewModel.class);
        tvScore = findViewById(R.id.tvScore);
        tvBest  = findViewById(R.id.best_value);

        adapter = new TileAdapter();
        RecyclerView board = findViewById(R.id.board);
        board.setLayoutManager(new GridLayoutManager(this, 4));
        board.setHasFixedSize(true);
        board.setAdapter(adapter);

        updateBoardUI(viewModel.jeu.matrice);

        // --- Bouton RESUME LAST ---
        findViewById(R.id.resume_game_btn).setOnClickListener(v -> reprendrePartie());

        // --- Bouton SCORE TABLE ---
        findViewById(R.id.score_table_btn).setOnClickListener(v -> afficherTableauScores());

        gestureDetector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
                if (viewModel.jeu.estBloque()) return true;

                float Y = e2.getY() - e1.getY();
                float X = e2.getX() - e1.getX();
                if (Math.abs(Y) > Math.abs(X) && Math.abs(Y) > 100) {
                    if (Y > 0) viewModel.jeu.bas();
                    else       viewModel.jeu.haut();
                } else if (Math.abs(X) > Math.abs(Y) && Math.abs(X) > 100) {
                    if (X > 0) viewModel.jeu.droite();
                    else       viewModel.jeu.gauche();
                }

                updateBoardUI(viewModel.jeu.matrice);

                if (viewModel.jeu.aGagne() && !viewModel.victoireAffichee) {
                    viewModel.victoireAffichee = true;
                    afficherDialogVictoire();
                } else if (viewModel.jeu.estBloque()) {
                    afficherDialogDefaite();
                }

                return true;
            }
        });

        board.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    // ─── Sauvegarde automatique quand l'app passe en arrière-plan ────────────
    @Override
    protected void onPause() {
        super.onPause();
        sauvegarderPartie();
    }

    // ─── Sauvegarde état du jeu dans SharedPreferences ───────────────────────
    private void sauvegarderPartie() {
        SharedPreferences.Editor editor = getSharedPreferences("game_save", MODE_PRIVATE).edit();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                sb.append(viewModel.jeu.matrice[i][j]);
                if (i * 4 + j < 15) sb.append(",");
            }
        editor.putString("matrice", sb.toString());
        editor.putInt("score", viewModel.jeu.score);
        editor.apply();
    }

    // ─── Reprendre la partie sauvegardée ─────────────────────────────────────
    private void reprendrePartie() {
        SharedPreferences prefs = getSharedPreferences("game_save", MODE_PRIVATE);
        String matriceStr = prefs.getString("matrice", null);
        if (matriceStr == null) {
            Toast.makeText(this, "Aucune partie sauvegardée", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] parts = matriceStr.split(",");
        int[] flat = new int[16];
        for (int i = 0; i < 16; i++)
            flat[i] = Integer.parseInt(parts[i]);
        int score = prefs.getInt("score", 0);
        viewModel.jeu = new Map(flat, score);
        viewModel.victoireAffichee = false;
        updateBoardUI(viewModel.jeu.matrice);
        Toast.makeText(this, "Partie reprise !", Toast.LENGTH_SHORT).show();
    }

    // ─── Enregistrer un score dans le top 5 ──────────────────────────────────
    private void enregistrerScore(int score) {
        SharedPreferences prefs = getSharedPreferences("scores", MODE_PRIVATE);
        List<Integer> scores = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int s = prefs.getInt("score_" + i, -1);
            if (s >= 0) scores.add(s);
        }
        scores.add(score);
        Collections.sort(scores, Collections.reverseOrder());
        SharedPreferences.Editor editor = prefs.edit();
        for (int i = 0; i < Math.min(5, scores.size()); i++)
            editor.putInt("score_" + i, scores.get(i));
        editor.apply();
        // Mettre à jour l'affichage BEST
        tvBest.setText(String.valueOf(scores.get(0)));
    }

    // ─── Afficher le tableau des meilleurs scores ─────────────────────────────
    private void afficherTableauScores() {
        SharedPreferences prefs = getSharedPreferences("scores", MODE_PRIVATE);
        StringBuilder sb = new StringBuilder();
        boolean hasScores = false;
        for (int i = 0; i < 5; i++) {
            int s = prefs.getInt("score_" + i, -1);
            if (s >= 0) {
                sb.append(i + 1).append(".  ").append(s).append("\n");
                hasScores = true;
            }
        }
        if (!hasScores) sb.append("Aucun score enregistré.");

        new AlertDialog.Builder(this)
                .setTitle("🏆 Meilleurs scores")
                .setMessage(sb.toString())
                .setPositiveButton("Fermer", null)
                .setNeutralButton("Réinitialiser", (dialog, which) -> {
                    prefs.edit().clear().apply();
                    tvBest.setText("0");
                    Toast.makeText(this, "Classement réinitialisé", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    // ─── Dialogs victoire / défaite ───────────────────────────────────────────
    private void afficherDialogVictoire() {
        enregistrerScore(viewModel.jeu.score);
        new AlertDialog.Builder(this)
                .setTitle("🎉 Vous avez gagné !")
                .setMessage("Vous avez atteint 2048 !\nScore : " + viewModel.jeu.score)
                .setPositiveButton("Continuer", (dialog, which) -> dialog.dismiss())
                .setNegativeButton("Nouvelle partie", (dialog, which) -> nouveauJeu())
                .setCancelable(false)
                .show();
    }

    private void afficherDialogDefaite() {
        enregistrerScore(viewModel.jeu.score);
        new AlertDialog.Builder(this)
                .setTitle("😞 Partie terminée")
                .setMessage("Plus aucun mouvement possible !\nScore final : " + viewModel.jeu.score)
                .setPositiveButton("Nouvelle partie", (dialog, which) -> nouveauJeu())
                .setCancelable(false)
                .show();
    }

    private void nouveauJeu() {
        viewModel.jeu = new Map();
        viewModel.victoireAffichee = false;
        updateBoardUI(viewModel.jeu.matrice);
    }

    // ─── Mise à jour de l'interface ───────────────────────────────────────────
    private void updateBoardUI(int[][] matrice) {
        List<Integer> flatMat = new ArrayList<>();
        for (int[] col : matrice)
            for (int row : col)
                flatMat.add(row);
        Log.d(TAG, flatMat.toString());
        adapter.submitList(flatMat);
        tvScore.setText(String.valueOf(viewModel.jeu.score));
        // Mettre à jour BEST depuis les prefs
        SharedPreferences prefs = getSharedPreferences("scores", MODE_PRIVATE);
        int best = prefs.getInt("score_0", 0);
        tvBest.setText(String.valueOf(best));
    }
}