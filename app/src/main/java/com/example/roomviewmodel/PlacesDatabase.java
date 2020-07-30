package com.example.roomviewmodel;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(version = 2, entities = {Place.class})
public abstract class PlacesDatabase extends RoomDatabase
{
    public abstract PlacesDao placesDao();
}
