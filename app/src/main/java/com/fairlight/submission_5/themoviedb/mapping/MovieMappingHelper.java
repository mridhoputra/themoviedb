package com.fairlight.submission_5.themoviedb.mapping;

import android.database.Cursor;

import com.fairlight.submission_5.themoviedb.model.Catalogue;

import java.util.ArrayList;

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

public class MovieMappingHelper {
    public static ArrayList<Catalogue> mapCursorToArrayList(Cursor movieCursor) {
        ArrayList<Catalogue> listCatalogue = new ArrayList<>();

        while (movieCursor.moveToNext()) {
            String app_title = movieCursor.getString(movieCursor.getColumnIndexOrThrow(MOVIE_APP_TITLE));
            String id_from_api = movieCursor.getString(movieCursor.getColumnIndexOrThrow(MOVIE_ID_FROM_API));
            String backdrop_url = movieCursor.getString(movieCursor.getColumnIndexOrThrow(MOVIE_BACKDROP_URL));
            String title = movieCursor.getString(movieCursor.getColumnIndexOrThrow(MOVIE_TITLE));
            String released_date = movieCursor.getString(movieCursor.getColumnIndexOrThrow(MOVIE_RELEASED_DATE));
            String user_score = movieCursor.getString(movieCursor.getColumnIndexOrThrow(MOVIE_USER_SCORE));
            String poster_url = movieCursor.getString(movieCursor.getColumnIndexOrThrow(MOVIE_POSTER_URL));
            String description = movieCursor.getString(movieCursor.getColumnIndexOrThrow(MOVIE_DESCRIPTION));
            String original_language = movieCursor.getString(movieCursor.getColumnIndexOrThrow(MOVIE_ORIGINAL_LANGUAGE));
            String popularity = movieCursor.getString(movieCursor.getColumnIndexOrThrow(MOVIE_POPULARITY));
            String vote_count = movieCursor.getString(movieCursor.getColumnIndexOrThrow(MOVIE_VOTE_COUNT));
            int is_favorite = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(MOVIE_IS_FAVORITE));

            listCatalogue.add(new Catalogue(app_title, id_from_api, backdrop_url, title, released_date, user_score, poster_url, description, original_language, popularity, vote_count, is_favorite));
        }

        return listCatalogue;
    }
}
