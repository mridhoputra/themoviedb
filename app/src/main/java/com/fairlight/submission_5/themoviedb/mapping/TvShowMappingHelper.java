package com.fairlight.submission_5.themoviedb.mapping;

import android.database.Cursor;

import com.fairlight.submission_5.themoviedb.model.Catalogue;

import java.util.ArrayList;

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

public class TvShowMappingHelper {
    public static ArrayList<Catalogue> mapCursorToArrayList(Cursor tvShowCursor) {
        ArrayList<Catalogue> listCatalogue = new ArrayList<>();

        while (tvShowCursor.moveToNext()) {
            String app_title = tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(TV_SHOW_APP_TITLE));
            String id_from_api = tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(TV_SHOW_ID_FROM_API));
            String backdrop_url = tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(TV_SHOW_BACKDROP_URL));
            String title = tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(TV_SHOW_TITLE));
            String released_date = tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(TV_SHOW_RELEASED_DATE));
            String user_score = tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(TV_SHOW_USER_SCORE));
            String poster_url = tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(TV_SHOW_POSTER_URL));
            String description = tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(TV_SHOW_DESCRIPTION));
            String original_language = tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(TV_SHOW_ORIGINAL_LANGUAGE));
            String popularity = tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(TV_SHOW_POPULARITY));
            String vote_count = tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(TV_SHOW_VOTE_COUNT));
            int is_favorite = tvShowCursor.getInt(tvShowCursor.getColumnIndexOrThrow(TV_SHOW_IS_FAVORITE));

            listCatalogue.add(new Catalogue(app_title, id_from_api, backdrop_url, title, released_date, user_score, poster_url, description, original_language, popularity, vote_count, is_favorite));
        }

        return listCatalogue;
    }
}
