package com.example.twomomspottery;

import androidx.room.Dao;
import androidx.room.Query;

import java.sql.Blob;
import java.util.List;

@Dao
public interface PieceDao {
    @Query("INSERT INTO pottery (type, cost, date, owner, path) VALUES (:name, :price, :date, :owner, :path)")
    void create(String name, String price, String date, String owner, String path);

    @Query("SELECT * FROM pottery")
    List<Piece> getAllPieces();

    @Query("DELETE FROM pottery WHERE id = :value")
    void delete(int value);
}
