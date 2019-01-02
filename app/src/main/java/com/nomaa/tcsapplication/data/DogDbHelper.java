package com.nomaa.tcsapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nomaa.tcsapplication.data.DogContract.*;


public class DogDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "dog.db";

    public static final int DATABASE_VERSION = 1;

    public DogDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQLITE_CREATE_DOG_TABLE = "CREATE TABLE " + DogEntry.TABLE_NAME + " (" +
                DogEntry.COLUMN_NAME + " TEXT, " +
                DogEntry.COLUMN_IMAGE_PATH + " TEXT, " +
                DogEntry._ID + " INTEGER PRIMARY KEY);";

        db.execSQL(SQLITE_CREATE_DOG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DogEntry.TABLE_NAME);
        onCreate(db);
    }
}