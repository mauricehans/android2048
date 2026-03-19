package com.example.android2048;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

        Map jeu = new Map();
        jeu.afficheterminal();
        adapter = new TileAdapter();
        RecyclerView board = findViewById(R.id.board);
        board.setLayoutManager(new GridLayoutManager(this, 4));
        board.setHasFixedSize(true);
        board.setAdapter(adapter);
        updateBoardUI(jeu.matrice);

        gestureDetector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
                float Y = e2.getY() - e1.getY();
                float X = e2.getX() - e1.getX();
                if (Math.abs(Y) > Math.abs(X) && Math.abs(Y) > 100) {
                    if (Y > 0) jeu.bas();
                    else jeu.haut();
                }
                else if (Math.abs(X) > Math.abs(Y) && Math.abs(X) > 100) {
                    if (X > 0) jeu.droite();
                    else jeu.gauche();
                }
                updateBoardUI(jeu.matrice);
                return true;
            }
        });
        board.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    private void updateBoardUI(int[][] matrice) {
        List<Integer> flatMat = new ArrayList<>();
        for (int[] col : matrice) {
            for (int row : col) flatMat.add(row);
        }
        Log.d(TAG, flatMat.toString());
        adapter.submitList(flatMat);
    }
}