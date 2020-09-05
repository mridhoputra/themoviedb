package com.fairlight.submission_5.themoviedb.tvshowdb;

import android.net.Uri;
import android.provider.BaseColumns;

public class TvShowDBContract {
    public static final String TV_SHOW_AUTHORITY = "com.fairlight.submission_5.themoviedb.provider.TvShowProvider";
    private static final String SCHEME = "content";
    public static String TABLE_NAME = "table_tv_show";

    //image database not created yet.
    public static final class TvShowColumns implements BaseColumns {
        public static final Uri TV_SHOW_CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(TV_SHOW_AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
        public static String TV_SHOW_APP_TITLE = "app_title";
        public static String TV_SHOW_ID_FROM_API = "id_from_api";
        public static String TV_SHOW_BACKDROP_URL = "backdrop_url";
        public static String TV_SHOW_TITLE = "title";
        public static String TV_SHOW_RELEASED_DATE = "released_date";
        public static String TV_SHOW_USER_SCORE = "user_score";
        public static String TV_SHOW_POSTER_URL = "poster_url";
        public static String TV_SHOW_DESCRIPTION = "description";
        public static String TV_SHOW_ORIGINAL_LANGUAGE = "original_language";
        public static String TV_SHOW_POPULARITY = "popularity";
        public static String TV_SHOW_VOTE_COUNT = "vote_count";
        public static String TV_SHOW_IS_FAVORITE = "is_favorite";
    }
}
