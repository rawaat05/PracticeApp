package com.nomaa.tcsapplication;

import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.nomaa.tcsapplication.data.DogContract;

public class MainActivity extends AppCompatActivity {

    private FrameLayout fragmentContainer;

    public static final String[] projection = {
            DogContract.DogEntry.COLUMN_NAME,
            DogContract.DogEntry.COLUMN_IMAGE_PATH,
            DogContract.DogEntry._ID
    };

    public static final int INDEX_DOG_NAME = 0;
    public static final int INDEX_DOG_IMG = 1;
    public static final int INDEX_DOG_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentContainer = findViewById(R.id.fragment_container);

        getSupportFragmentManager().beginTransaction().addToBackStack("dog").add(R.id.fragment_container, DogFragment.newInstance()).commit();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }
}
