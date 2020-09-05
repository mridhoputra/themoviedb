package com.fairlight.submission_5.themoviedb.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.fairlight.submission_5.themoviedb.tvshowdb.Catalogue_TvShowHelper;
import com.fairlight.submission_5.themoviedb.tvshowdb.TvShowDBContract;

import androidx.annotation.NonNull;

import static com.fairlight.submission_5.themoviedb.tvshowdb.TvShowDBContract.TABLE_NAME;
import static com.fairlight.submission_5.themoviedb.tvshowdb.TvShowDBContract.TV_SHOW_AUTHORITY;
import static com.fairlight.submission_5.themoviedb.tvshowdb.TvShowDBContract.TvShowColumns.TV_SHOW_CONTENT_URI;

public class TvShowProvider extends ContentProvider {
    private static final int TV_SHOW = 11;
    private static final int TV_SHOW_ID = 12;
    private static final int TV_SHOW_TITLE = 13;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // content://com.fairlight.submission_5.themoviedb.provider.TvShowProvider/table_tv_show
        sUriMatcher.addURI(TV_SHOW_AUTHORITY, TABLE_NAME, TV_SHOW);
        // content://com.fairlight.submission_5.themoviedb.provider.TvShowProvider/table_tv_show/id
        sUriMatcher.addURI(TV_SHOW_AUTHORITY,
                TABLE_NAME + "/#",
                TV_SHOW_ID);
        // content://com.fairlight.submission_5.themoviedb.provider.TvShowProvider/table_tv_show/title
        sUriMatcher.addURI(TV_SHOW_AUTHORITY,
                TABLE_NAME + "/" + TvShowDBContract.TvShowColumns.TV_SHOW_TITLE + "/*",
                TV_SHOW_TITLE);
    }

    private Catalogue_TvShowHelper tvShowHelper;

    @Override
    public boolean onCreate() {
        tvShowHelper = Catalogue_TvShowHelper.getInstance(getContext());
        tvShowHelper.open();
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case TV_SHOW:
                cursor = tvShowHelper.queryAll();
                break;
            case TV_SHOW_ID:
                cursor = tvShowHelper.queryById(uri.getLastPathSegment());
                break;
            case TV_SHOW_TITLE:
                Log.d("onQueryWithTitle", String.valueOf(uri));
                cursor = tvShowHelper.queryByTitle(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        long added;
        Log.d("onTvShowInsert: ", String.valueOf(uri));
        if (sUriMatcher.match(uri) == TV_SHOW) {
            added = tvShowHelper.insertValue(contentValues);
        } else {
            added = 0;
        }

        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(TV_SHOW_CONTENT_URI, null);
        }

        return Uri.parse(TV_SHOW_CONTENT_URI + "/" + added);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updated;
        if (sUriMatcher.match(uri) == TV_SHOW_ID) {
            updated = tvShowHelper.update(uri.getLastPathSegment(), values);
        } else {
            updated = 0;
        }

        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(TV_SHOW_CONTENT_URI, null);
        }

        return updated;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case TV_SHOW_ID:
                deleted = tvShowHelper.deleteById(uri.getLastPathSegment());
                break;
            case TV_SHOW_TITLE:
                Log.d("onTvShowDelete: ", String.valueOf(uri));
                deleted = tvShowHelper.delete(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(TV_SHOW_CONTENT_URI, null);
        }

        return deleted;
    }
}
