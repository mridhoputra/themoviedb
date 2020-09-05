package com.fairlight.submission_5.themoviedb.moviedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_APP_TITLE;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_BACKDROP_URL;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_DESCRIPTION;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_ID_FROM_API;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_IS_FAVORITE;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_ORIGINAL_LANGUAGE;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_POPULARITY;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_POSTER_URL;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_RELEASED_DATE;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_TITLE;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_USER_SCORE;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_VOTE_COUNT;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.TABLE_NAME;

public class MovieDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dbmovie";

    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_MOVIE = "CREATE TABLE " + TABLE_NAME +
            " (" + _ID + " integer primary key autoincrement, " +
            MOVIE_APP_TITLE + " TEXT NOT NULL, " +
            MOVIE_ID_FROM_API + " TEXT UNIQUE NOT NULL, " +
            MOVIE_BACKDROP_URL + " TEXT NOT NULL, " +
            MOVIE_TITLE + " TEXT NOT NULL, " +
            MOVIE_RELEASED_DATE + " TEXT NOT NULL, " +
            MOVIE_USER_SCORE + " TEXT NOT NULL, " +
            MOVIE_POSTER_URL + " TEXT NOT NULL, " +
            MOVIE_DESCRIPTION + " TEXT NOT NULL, " +
            MOVIE_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
            MOVIE_POPULARITY + " TEXT NOT NULL, " +
            MOVIE_VOTE_COUNT + " TEXT NOT NULL, " +
            MOVIE_IS_FAVORITE + " INT NOT NULL DEFAULT 0);";

    MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        Drop table tidak dianjurkan ketika proses migrasi terjadi dikarenakan data user akan hilang,
        tetapi demi kemudahan, untuk sementara menggunakan cara ini dahulu
        */
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
