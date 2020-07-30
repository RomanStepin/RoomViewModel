package com.example.roomviewmodel;

import android.database.Cursor;
import android.widget.ListView;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlacesDao
{

    @Query("SELECT * FROM PLACES")
    Cursor queryPlaces();

    @Insert
    void insertPlaces(Place place);

    @Delete
    int deletePlace(List<Place> places);

    @Update
    void updatePlace(Place place);
}
