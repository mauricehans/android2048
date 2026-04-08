package com.example.android2048.scoreBD;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "score")
public class ScoreEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @NonNull @ColumnInfo(name="name")
    public String name;
    @ColumnInfo(name="score")
    public int score;
    public ScoreEntity(@NonNull String name, int score) {
        this.name = name;
        this.score = score;
    }
}
