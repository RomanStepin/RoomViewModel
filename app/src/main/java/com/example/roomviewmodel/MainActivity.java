package com.example.roomviewmodel;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.roomviewmodel.databinding.ActivityMainBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    ListView listView;
    SimpleCursorAdapter simpleCursorAdapter;
    LiveData<Cursor> liveData;
    GoogleMap googleMap;

    SupportMapFragment supportMapFragment;
    MarkerOptions markerOptions;
    Cursor currentCursor;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);

        listView = findViewById(R.id.listView);
        simpleCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_activated_2, null, new String[]{"name", "description"}, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        listView.setAdapter(simpleCursorAdapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);


        final MyViewModel model = new ViewModelProvider(this).get(MyViewModel.class);
        binding.setButtonInsert(model);

        liveData = model.getLiveData();
        liveData.observe(this, new Observer<Cursor>() {
            @Override
            public void onChanged(Cursor cursor) {
                currentCursor = cursor;
                simpleCursorAdapter.swapCursor(cursor);
                drawMarkers(cursor);
            }
        });

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            Menu menu;
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if(listView.getCheckedItemCount() > 1)
                    menu.getItem(0).setVisible(false);
                else
                    menu.getItem(0).setVisible(true);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.context, menu);
                this.menu = menu;

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();

                switch (item.getItemId())
                {
                    case R.id.itemChange:
                        model.updatePlace(sparseBooleanArray);
                        break;
                    case R.id.itemDelete:
                        model.deletePlace(sparseBooleanArray);
                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentCursor.moveToPosition(position);
                LatLng clockLatLng = new LatLng(currentCursor.getDouble(currentCursor.getColumnIndex("coordinate_x")), currentCursor.getDouble(currentCursor.getColumnIndex("coordinate_y")));
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().zoom(5).target(clockLatLng).build()));
            }
        });

    }

    void drawMarkers(Cursor cursor)
    {
        googleMap.clear();
        if (cursor.moveToFirst()) {
            LatLng firstLatLng = new LatLng(cursor.getDouble(cursor.getColumnIndex("coordinate_x")), cursor.getDouble(cursor.getColumnIndex("coordinate_y")));
            markerOptions = new MarkerOptions().position(firstLatLng);
            googleMap.addMarker(markerOptions);
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().zoom(5).target(firstLatLng).build()));
            while (cursor.moveToNext()) {
                markerOptions = new MarkerOptions().position(new LatLng(cursor.getDouble(cursor.getColumnIndex("coordinate_x")), cursor.getDouble(cursor.getColumnIndex("coordinate_y"))));
                googleMap.addMarker(markerOptions);
            }
        }
        else
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().zoom(1).target(new LatLng(0,0)).build()));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
}








