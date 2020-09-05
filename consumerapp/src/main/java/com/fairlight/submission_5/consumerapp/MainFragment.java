package com.fairlight.submission_5.consumerapp;


import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fairlight.submission_5.consumerapp.adapter.CatalogueAdapter;
import com.fairlight.submission_5.consumerapp.mapping.MovieMappingHelper;
import com.fairlight.submission_5.consumerapp.mapping.TvShowMappingHelper;
import com.fairlight.submission_5.consumerapp.model.Catalogue;
import com.fairlight.submission_5.consumerapp.moviedb.MovieDBContract;
import com.fairlight.submission_5.consumerapp.tvshowdb.TvShowDBContract;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


interface LoadCatalogueCallback {
    void preExecute();

    void postExecute(ArrayList<Catalogue> listCatalogue);
}

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements LoadCatalogueCallback {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private CatalogueAdapter catalogueAdapter;
    private ProgressBar progressBar;
    private TextView noData;


    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(int index) {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar);
        noData = view.findViewById(R.id.text_no_data);
        noData.setVisibility(View.GONE);

        RecyclerView rv_favorite_catalogue = view.findViewById(R.id.rv_favorite_catalogue);
        rv_favorite_catalogue.setHasFixedSize(true);
        rv_favorite_catalogue.setLayoutManager(new LinearLayoutManager(getContext()));

        int index = 0;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        switch (index) {
            case 0:
                catalogueAdapter = new CatalogueAdapter();
                catalogueAdapter.notifyDataSetChanged();
                rv_favorite_catalogue.setAdapter(catalogueAdapter);

                HandlerThread movieHandlerThread = new HandlerThread("MovieDataObserver");
                movieHandlerThread.start();
                Handler movieHandler = new Handler(movieHandlerThread.getLooper());
                MovieDataObserver movieObserver = new MovieDataObserver(movieHandler, getContext(), this);
                requireContext().getContentResolver().registerContentObserver(MovieDBContract.MovieColumns.MOVIE_CONTENT_URI, true, movieObserver);

                if (savedInstanceState == null) {
                    new LoadMovieAsync(requireContext(), this).execute();
                } else {
                    ArrayList<Catalogue> listCatalogue = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
                    if (listCatalogue != null) {
                        catalogueAdapter.setData(listCatalogue);
                    }
                }

                catalogueAdapter.setOnItemClickCallback(new CatalogueAdapter.OnItemClickCallback() {
                    @Override
                    public void onItemClicked(Catalogue catalogue) {
                        Intent moveWithObjectIntent = new Intent(getContext(), CatalogueDetailActivity.class);
                        moveWithObjectIntent.putExtra(CatalogueDetailActivity.EXTRA_CATALOGUE, catalogue);
                        String catalogue_type = "MOVIE";
                        moveWithObjectIntent.putExtra(CatalogueDetailActivity.EXTRA_CATALOGUE_TYPE, catalogue_type);
                        startActivity(moveWithObjectIntent);
                    }
                });
                break;
            case 1:
                catalogueAdapter = new CatalogueAdapter();
                catalogueAdapter.notifyDataSetChanged();
                rv_favorite_catalogue.setAdapter(catalogueAdapter);

                HandlerThread tvShowHandlerThread = new HandlerThread("TvShowDataObserver");
                tvShowHandlerThread.start();
                Handler tvShowHandler = new Handler(tvShowHandlerThread.getLooper());
                TvShowDataObserver tvShowObserver = new TvShowDataObserver(tvShowHandler, getContext(), this);
                requireContext().getContentResolver().registerContentObserver(TvShowDBContract.TvShowColumns.TV_SHOW_CONTENT_URI, true, tvShowObserver);

                if (savedInstanceState == null) {
                    new LoadTvShowAsync(requireContext(), this).execute();
                } else {
                    ArrayList<Catalogue> listCatalogue = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
                    if (listCatalogue != null) {
                        catalogueAdapter.setData(listCatalogue);
                    }
                }

                catalogueAdapter.setOnItemClickCallback(new CatalogueAdapter.OnItemClickCallback() {
                    @Override
                    public void onItemClicked(Catalogue catalogue) {
                        Intent moveWithObjectIntent = new Intent(getContext(), CatalogueDetailActivity.class);
                        moveWithObjectIntent.putExtra(CatalogueDetailActivity.EXTRA_CATALOGUE, catalogue);
                        String catalogue_type = "TV_SHOW";
                        moveWithObjectIntent.putExtra(CatalogueDetailActivity.EXTRA_CATALOGUE_TYPE, catalogue_type);
                        startActivity(moveWithObjectIntent);
                    }
                });
                break;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, catalogueAdapter.getData());
    }

    @Override
    public void preExecute() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void postExecute(ArrayList<Catalogue> listCatalogue) {
        progressBar.setVisibility(View.INVISIBLE);
        if (listCatalogue.size() > 0) {
            catalogueAdapter.setData(listCatalogue);
        } else {
            noData.setText(R.string.no_data);
            noData.setVisibility(View.VISIBLE);
            catalogueAdapter.setData(new ArrayList<Catalogue>());
        }
    }

    private static class LoadMovieAsync extends AsyncTask<Void, Void, ArrayList<Catalogue>> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadCatalogueCallback> weakCallback;

        private LoadMovieAsync(Context context, LoadCatalogueCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Catalogue> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(MovieDBContract.MovieColumns.MOVIE_CONTENT_URI, null, null, null, null);
            if (dataCursor != null) {
                return MovieMappingHelper.mapCursorToArrayList(dataCursor);
            } else {
                return new ArrayList<>();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Catalogue> listCatalogue) {
            super.onPostExecute(listCatalogue);
            weakCallback.get().postExecute(listCatalogue);
        }
    }

    private static class LoadTvShowAsync extends AsyncTask<Void, Void, ArrayList<Catalogue>> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadCatalogueCallback> weakCallback;

        private LoadTvShowAsync(Context context, LoadCatalogueCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Catalogue> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(TvShowDBContract.TvShowColumns.TV_SHOW_CONTENT_URI, null, null, null, null);
            if (dataCursor != null) {
                return TvShowMappingHelper.mapCursorToArrayList(dataCursor);
            } else {
                return new ArrayList<>();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Catalogue> listCatalogue) {
            super.onPostExecute(listCatalogue);
            weakCallback.get().postExecute(listCatalogue);
        }
    }

    public static class MovieDataObserver extends ContentObserver {
        final Context context;
        final LoadCatalogueCallback callback;

        MovieDataObserver(Handler handler, Context context, LoadCatalogueCallback callback) {
            super(handler);
            this.context = context;
            this.callback = callback;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadMovieAsync(context, callback).execute();
        }
    }

    public static class TvShowDataObserver extends ContentObserver {
        final Context context;
        final LoadCatalogueCallback callback;

        TvShowDataObserver(Handler handler, Context context, LoadCatalogueCallback callback) {
            super(handler);
            this.context = context;
            this.callback = callback;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadTvShowAsync(context, callback).execute();
        }
    }
}
