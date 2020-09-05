package com.fairlight.submission_5.themoviedb.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.fairlight.submission_5.themoviedb.R;
import com.fairlight.submission_5.themoviedb.mapping.MovieMappingHelper;
import com.fairlight.submission_5.themoviedb.model.Catalogue;
import com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context mContext;
    private ArrayList<Catalogue> listMovie;

    StackRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        // Revert back to our process' identity so we can work with our
        // content provider
        final long identityToken = Binder.clearCallingIdentity();

        listMovie = new ArrayList<>();
        Cursor dataCursor = mContext.getContentResolver().query(MovieDBContract.MovieColumns.MOVIE_CONTENT_URI, null, null, null, null);
        if (dataCursor != null) {
            listMovie.addAll(MovieMappingHelper.mapCursorToArrayList(dataCursor));
        } else {
            listMovie = new ArrayList<>();
        }

        // Restore the identity - not sure if it's needed since we're going
        // to return right here, but it just *seems* cleaner
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return listMovie.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (listMovie.size() > 0) {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
            if (listMovie.get(position).getBackdrop_url().contains("null")) {
                rv.setImageViewResource(R.id.widget_tv_backdrop, R.drawable.backdrop_not_yet_available);
            } else {
                try {
                    Bitmap backdrop = Glide.with(mContext)
                            .asBitmap()
                            .load(listMovie.get(position).getBackdrop_url())
                            .submit()
                            .get();
                    rv.setImageViewBitmap(R.id.widget_tv_backdrop, backdrop);

                } catch (ExecutionException | InterruptedException e) {
                    Log.e("Error at getViewAt: ", "Fail to take bitmap");
                }
            }

            rv.setTextViewText(R.id.widget_tv_movie_title, listMovie.get(position).getTitle());
            Bundle extras = new Bundle();
            extras.putString(FavoriteMovieWidget.EXTRA_TITLE, listMovie.get(position).getTitle());
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            rv.setOnClickFillInIntent(R.id.widget_tv_backdrop, fillInIntent);
            return rv;
        } else {
            return null;
        }
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
