package com.example.roomviewmodel;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MyViewModel extends AndroidViewModel
{
    PlacesDatabase roomDatabase;
    MutableLiveData<Cursor> liveData;
    Executor executor;
    PlaceGenerator placeGenerator;

    public MyViewModel(@NonNull Application application) {
        super(application);
        roomDatabase = Room.databaseBuilder(application.getApplicationContext(), PlacesDatabase.class, "PLACES_DATABASE").fallbackToDestructiveMigration().build();
        liveData = new MutableLiveData<>();
        executor = Executors.newFixedThreadPool(1);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                liveData.postValue(roomDatabase.placesDao().queryPlaces());
            }
        });
        placeGenerator = new PlaceGenerator();
    }

    public RoomDatabase getRoomDatabase() {
        return roomDatabase;
    }

    public LiveData<Cursor> getLiveData() {
        return liveData;
    }

    public void insertPlace(View view)
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                roomDatabase.placesDao().insertPlaces(placeGenerator.getPlace());
                liveData.postValue(roomDatabase.placesDao().queryPlaces());
            }
        });
    }

    public void deletePlace(final SparseBooleanArray sparseBooleanArray)
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<Place> arrayList = new ArrayList<>();
                Cursor cursor = roomDatabase.placesDao().queryPlaces();
                Place place;
                for(int i = 0; i < sparseBooleanArray.size(); i++)
                {
                        cursor.moveToPosition(sparseBooleanArray.keyAt(i));
                        place = new Place(cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("description")), cursor.getDouble(cursor.getColumnIndex("coordinate_x")), cursor.getDouble(cursor.getColumnIndex("coordinate_y")));
                        place._id = cursor.getLong(cursor.getColumnIndex("_id"));
                        arrayList.add(place);
                }
                roomDatabase.placesDao().deletePlace(arrayList);
                liveData.postValue(roomDatabase.placesDao().queryPlaces());
            }
        });
    }

    public void updatePlace(final SparseBooleanArray sparseBooleanArray)
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = roomDatabase.placesDao().queryPlaces();
                Place place;
                cursor.moveToPosition(sparseBooleanArray.keyAt(0));
                place = placeGenerator.getPlace();
                place._id = cursor.getLong(cursor.getColumnIndex("_id"));
                roomDatabase.placesDao().updatePlace(place);
                liveData.postValue(roomDatabase.placesDao().queryPlaces());
            }
        });
    }
}


