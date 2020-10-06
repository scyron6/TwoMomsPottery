package com.example.twomomspottery;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Piece.class}, version = 1)
public abstract class PotteryDatabase extends RoomDatabase {
    public abstract PieceDao pieceDao();
}
