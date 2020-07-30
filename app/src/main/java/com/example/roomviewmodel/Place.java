package com.example.roomviewmodel;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PLACES")
public class Place
{
    @PrimaryKey(autoGenerate = true)
    public long _id;
    public String name;
    public String description;
    public double coordinate_x;
    public double coordinate_y;


    public Place(String name, String description, double coordinate_x, double coordinate_y) {
        this.name = name;
        this.description = description;
        this.coordinate_x = coordinate_x;
        this.coordinate_y = coordinate_y;
        this._id = _id;
    }
}
