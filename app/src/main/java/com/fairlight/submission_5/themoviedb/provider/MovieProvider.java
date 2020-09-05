package com.fairlight.submission_5.themoviedb.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.fairlight.submission_5.themoviedb.moviedb.Catalogue_MovieHelper;
import com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract;

import androidx.annotation.NonNull;

import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MOVIE_AUTHORITY;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_CONTENT_URI;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.TABLE_NAME;

public class MovieProvider extends ContentProvider {
    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final int MOVIE_TITLE = 3;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // content://com.fairlight.submission_5.themoviedb.provider.MovieProvider/table_movie
        sUriMatcher.addURI(MOVIE_AUTHORITY, TABLE_NAME, MOVIE);
        // content://com.fairlight.submission_5.themoviedb.provider.MovieProvider/table_movie/id
        sUriMatcher.addURI(MOVIE_AUTHORITY,
                TABLE_NAME + "/#",
                MOVIE_ID);
        // content://com.fairlight.submission_5.themoviedb.provider.MovieProvider/table_movie/title
        sUriMatcher.addURI(MOVIE_AUTHORITY,
                TABLE_NAME + "/" + MovieDBContract.MovieColumns.MOVIE_TITLE + "/*",
                MOVIE_TITLE);
    }

    private Catalogue_MovieHelper movieHelper;

    @Override
    public boolean onCreate() {
        movieHelper = Catalogue_MovieHelper.getInstance(getContext());
        movieHelper.open();
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                cursor = movieHelper.queryAll();
                break;
            case MOVIE_ID:
                cursor = movieHelper.queryById(uri.getLastPathSegment());
                break;
            case MOVIE_TITLE:
                Log.d("onQueryWithTitle", String.valueOf(uri));
                cursor = movieHelper.queryByTitle(uri.getLastPathSegment());
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
        Log.d("onMovieInsert: ", String.valueOf(uri));
        if (sUriMatcher.match(uri) == MOVIE) {
            added = movieHelper.insertValue(contentValues);
        } else {
            added = 0;
        }

        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(MOVIE_CONTENT_URI, null);
        }

        return Uri.parse(MOVIE_CONTENT_URI + "/" + added);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updated;
        if (sUriMatcher.match(uri) == MOVIE_ID) {
            updated = movieHelper.update(uri.getLastPathSegment(), values);
        } else {
            updated = 0;
        }

        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(MOVIE_CONTENT_URI, null);
        }

        return updated;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                deleted = movieHelper.deleteById(uri.getLastPathSegment());
                break;
            case MOVIE_TITLE:
                Log.d("onMovieDelete: ", String.valueOf(uri));
                deleted = movieHelper.delete(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(MOVIE_CONTENT_URI, null);
        }

        return deleted;
    }
}
