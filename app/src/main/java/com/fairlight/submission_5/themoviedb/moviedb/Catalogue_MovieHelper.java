package com.fairlight.submission_5.themoviedb.moviedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_TITLE;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.TABLE_NAME;

public class Catalogue_MovieHelper {
    private static Catalogue_MovieHelper INSTANCE;
    private final MovieDBHelper dataBaseHelper;
    private SQLiteDatabase database;

    private Catalogue_MovieHelper(Context context) {
        dataBaseHelper = new MovieDBHelper(context);
    }

    public static Catalogue_MovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Catalogue_MovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public Cursor queryAll() {
        return database.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC");
    }

    public Cursor queryById(String id) {
        return database.query(
                TABLE_NAME,
                null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
    }

    public Cursor queryByTitle(String title) {
        return database.query(TABLE_NAME,
                null,
                MOVIE_TITLE + " =?",
                new String[]{title},
                null,
                null,
                null,
                null);
    }

    public long insertValue(ContentValues contentValues) {
        return database.insert(TABLE_NAME, null, contentValues);
    }

    public int update(String id, ContentValues values) {
        return database.update(TABLE_NAME, values, _ID + " = ?", new String[]{id});
    }

    public int deleteById(String id) {
        return database.delete(TABLE_NAME, _ID + " = ?", new String[]{id});
    }

    public int delete(String title) {
        return database.delete(TABLE_NAME, MOVIE_TITLE + " = ?", new String[]{title});
    }
}
