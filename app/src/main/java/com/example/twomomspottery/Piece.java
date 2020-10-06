package com.example.twomomspottery;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Blob;

@Entity (tableName = "pottery")
public class Piece {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "owner")
    public String owner;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "cost")
    public String cost;

    @ColumnInfo(name = "path")
    public String path;


}
