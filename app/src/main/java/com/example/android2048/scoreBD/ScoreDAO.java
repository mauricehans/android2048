package com.example.android2048.scoreBD;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface ScoreDAO {
    @Insert
    public void insert(ScoreEntity score);
    @Query("SELECT * FROM score ORDER BY score DESC LIMIT :limit")
    List<ScoreEntity> getScores(int limit);
    @Query("DELETE FROM score")
    void deleteAll();
    @Query("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='score'")
    void resetId();
    @Transaction // Indique qu'on execute sur un thread secondaire
    default void truncateTable() {
        deleteAll();
        resetId();
    }
}