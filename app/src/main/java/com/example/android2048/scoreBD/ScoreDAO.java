package com.example.android2048.scoreBD;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface ScoreDAO {
    @Insert
    void insert(ScoreEntity score);
    @Query("SELECT * FROM score ORDER BY score DESC LIMIT :limit")
    List<ScoreEntity> getScores(int limit);
    @Query("DELETE FROM score")
    void deleteAll();
    @Query("DELETE FROM sqlite_sequence WHERE name='score'")
    void resetId();
    @Transaction // Indique qu'on execute sur un thread secondaire
    default void truncateTable() {
        deleteAll();
        resetId();
    }
}