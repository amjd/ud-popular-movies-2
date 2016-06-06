package com.example.amjad.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "favMovies.db";


    public MovieDbHelper(Context context){
       super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
         "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                 MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                 MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                 MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                 MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                 MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                 MovieContract.MovieEntry.COLUMN_POPULARITY + " REAL, " +
                 MovieContract.MovieEntry.COLUMN_RATING + " REAL NOT NULL, " +
                 MovieContract.MovieEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
                 MovieContract.MovieEntry.COLUMN_BACKDROP + " TEXT, " +
                 MovieContract.MovieEntry.COLUMN_POSTER + " TEXT NOT NULL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
