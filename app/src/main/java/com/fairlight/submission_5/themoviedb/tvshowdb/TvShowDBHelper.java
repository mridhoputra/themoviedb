package com.fairlight.submission_5.themoviedb.tvshowdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.fairlight.submission_5.themoviedb.tvshowdb.TvShowDBContract.TABLE_NAME;
import static com.fairlight.submission_5.themoviedb.tvshowdb.TvShowDBContract.TvShowColumns.TV_SHOW_APP_TITLE;
import static com.fairlight.submission_5.themoviedb.tvshowdb.TvShowDBContract.TvShowColumns.TV_SHOW_BACKDROP_URL;
import static com.fairlight.submission_5.themoviedb.tvshowdb.TvShowDBContract.TvShowColumns.TV_SHOW_DESCRIPTION;
import static com.fairlight.submission_5.themoviedb.tvshowdb.TvShowDBContract.TvShowColumns.TV_SHOW_ID_FROM_API;
import static com.fairlight.submission_5.themoviedb.tvshowdb.TvShowDBContract.TvShowColumns.TV_SHOW_IS_FAVORITE;
import static com.fairlight.submission_5.themoviedb.tvshowdb.TvShowDBContract.TvShowColumns.TV_SHOW_ORIGINAL_LANGUAGE;
import static com.fairlight.submission_5.themoviedb.tvshowdb.TvShowDBContract.TvShowColumns.TV_SHOW_POPULARITY;
import static com.fairlight.submission_5.themoviedb.tvshowdb.TvShowDBContract.TvShowColumns.TV_SHOW_POSTER_URL;
import static com.fairlight.submission_5.themoviedb.tvshowdb.TvShowDBContract.TvShowColumns.TV_SHOW_RELEASED_DATE;
import static com.fairlight.submission_5.themoviedb.tvshowdb.TvShowDBContract.TvShowColumns.TV_SHOW_TITLE;
import static com.fairlight.submission_5.themoviedb.tvshowdb.TvShowDBContract.TvShowColumns.TV_SHOW_USER_SCORE;
import static com.fairlight.submission_5.themoviedb.tvshowdb.TvShowDBContract.TvShowColumns.TV_SHOW_VOTE_COUNT;

public class TvShowDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dbtvshow";

    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_TV_SHOW = "CREATE TABLE " + TABLE_NAME +
            " (" + _ID + " integer primary key autoincrement, " +
            TV_SHOW_APP_TITLE + " TEXT NOT NULL, " +
            TV_SHOW_ID_FROM_API + " TEXT UNIQUE NOT NULL, " +
            TV_SHOW_BACKDROP_URL + " TEXT NOT NULL, " +
            TV_SHOW_TITLE + " TEXT NOT NULL, " +
            TV_SHOW_RELEASED_DATE + " TEXT NOT NULL, " +
            TV_SHOW_USER_SCORE + " TEXT NOT NULL, " +
            TV_SHOW_POSTER_URL + " TEXT NOT NULL, " +
            TV_SHOW_DESCRIPTION + " TEXT NOT NULL, " +
            TV_SHOW_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
            TV_SHOW_POPULARITY + " TEXT NOT NULL, " +
            TV_SHOW_VOTE_COUNT + " TEXT NOT NULL, " +
            TV_SHOW_IS_FAVORITE + " INT NOT NULL DEFAULT 0);";

    TvShowDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TV_SHOW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        Drop table tidak dianjurkan ketika proses migrasi terjadi dikarenakan data user akan hilang,
        But will use this for the sake of the simplicity, for now
        */
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
