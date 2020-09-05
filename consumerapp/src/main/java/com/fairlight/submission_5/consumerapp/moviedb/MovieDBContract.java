package com.fairlight.submission_5.consumerapp.moviedb;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieDBContract {
    private static final String MOVIE_AUTHORITY = "com.fairlight.submission_5.themoviedb.provider.MovieProvider";
    private static final String SCHEME = "content";
    private static String TABLE_NAME = "table_movie";

    //image database not created yet.
    public static final class MovieColumns implements BaseColumns {
        public static final Uri MOVIE_CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(MOVIE_AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
        public static String MOVIE_APP_TITLE = "app_title";
        public static String MOVIE_ID_FROM_API = "id_from_api";
        public static String MOVIE_BACKDROP_URL = "backdrop_url";
        public static String MOVIE_TITLE = "title";
        public static String MOVIE_RELEASED_DATE = "released_date";
        public static String MOVIE_USER_SCORE = "user_score";
        public static String MOVIE_POSTER_URL = "poster_url";
        public static String MOVIE_DESCRIPTION = "description";
        public static String MOVIE_ORIGINAL_LANGUAGE = "original_language";
        public static String MOVIE_POPULARITY = "popularity";
        public static String MOVIE_VOTE_COUNT = "vote_count";
        public static String MOVIE_IS_FAVORITE = "is_favorite";
    }
}
