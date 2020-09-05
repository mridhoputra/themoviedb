package com.fairlight.submission_5.themoviedb.ui.home_movies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.fairlight.submission_5.themoviedb.R;
import com.fairlight.submission_5.themoviedb.adapter.CatalogueAdapter;
import com.fairlight.submission_5.themoviedb.model.Catalogue;
import com.fairlight.submission_5.themoviedb.ui.detaileditem.CatalogueDetailActivity;
import com.fairlight.submission_5.themoviedb.ui.settings.SettingsActivity;
import com.fairlight.submission_5.themoviedb.viewmodel.MovieViewModel;
import com.fairlight.submission_5.themoviedb.viewmodel.SearchMovieViewModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MovieFragment extends Fragment {
    private MovieViewModel movieViewModel;
    private SearchMovieViewModel searchMovieViewModel;
    private RecyclerView rv_catalogue;
    private CatalogueAdapter movieAdapter;
    private CatalogueAdapter searchMovieAdapter;
    private ProgressBar progressBar;
    private TextView noResults;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_home_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        noResults = view.findViewById(R.id.text_no_movie_results);

        rv_catalogue = view.findViewById(R.id.rv_catalogue);
        rv_catalogue.setHasFixedSize(true);
        rv_catalogue.setLayoutManager(new LinearLayoutManager(getContext()));
        movieAdapter = new CatalogueAdapter();
        movieAdapter.notifyDataSetChanged();
        rv_catalogue.setAdapter(movieAdapter);

        movieViewModel = new ViewModelProvider(MovieFragment.this, new ViewModelProvider.NewInstanceFactory()).get(MovieViewModel.class);

        movieViewModel.setListMovies();
        showLoading(true);
        showLiveList();
    }

    private void showLiveList() {
        movieViewModel.getListMovies().observe(getViewLifecycleOwner(), new Observer<ArrayList<Catalogue>>() {
            @Override
            public void onChanged(ArrayList<Catalogue> catalogues) {
                if (catalogues != null) {
                    movieAdapter.setData(catalogues);
                    showLoading(false);
                }
            }
        });

        movieAdapter.setOnItemClickCallback(new CatalogueAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Catalogue catalogue) {
                Intent moveWithObjectIntent = new Intent(getContext(), CatalogueDetailActivity.class);
                moveWithObjectIntent.putExtra(CatalogueDetailActivity.EXTRA_CATALOGUE, catalogue);
                String catalogue_type = "MOVIE";
                moveWithObjectIntent.putExtra(CatalogueDetailActivity.EXTRA_CATALOGUE_TYPE, catalogue_type);
                startActivity(moveWithObjectIntent);
            }
        });
    }

    private void showSearchedLiveList() {
        searchMovieViewModel.getListMovies().observe(getViewLifecycleOwner(), new Observer<ArrayList<Catalogue>>() {
            @Override
            public void onChanged(ArrayList<Catalogue> catalogues) {
                if (catalogues != null) {
                    noResults.setVisibility(View.GONE);
                    searchMovieAdapter.setData(catalogues);
                    showLoading(false);
                } else {
                    searchMovieAdapter.setData(new ArrayList<Catalogue>());
                    noResults.setVisibility(View.VISIBLE);
                    showLoading(false);
                }
            }
        });

        searchMovieAdapter.setOnItemClickCallback(new CatalogueAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Catalogue catalogue) {
                Intent moveWithObjectIntent = new Intent(getContext(), CatalogueDetailActivity.class);
                moveWithObjectIntent.putExtra(CatalogueDetailActivity.EXTRA_CATALOGUE, catalogue);
                String catalogue_type = "MOVIE";
                moveWithObjectIntent.putExtra(CatalogueDetailActivity.EXTRA_CATALOGUE_TYPE, catalogue_type);
                startActivity(moveWithObjectIntent);
            }
        });
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.movie_menu, menu);

        MenuItem search = menu.findItem(R.id.search_movie);

        final SearchView searchView = (SearchView) search.getActionView();
        searchView.setQuery("", false);
        searchView.clearFocus();
        searchView.setIconified(false);
        searchView.setQueryHint(getString(R.string.search_movie));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();

                searchMovieAdapter = new CatalogueAdapter();
                searchMovieAdapter.notifyDataSetChanged();
                rv_catalogue.setAdapter(searchMovieAdapter);

                searchMovieViewModel = new ViewModelProvider(MovieFragment.this, new ViewModelProvider.NewInstanceFactory()).get(SearchMovieViewModel.class);

                searchMovieViewModel.setListSearchedMovies(query);
                showLoading(true);
                showSearchedLiveList();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchView.setQuery("", false);
                searchView.clearFocus();

                noResults.setVisibility(View.GONE);
                rv_catalogue.setAdapter(movieAdapter);
                showLoading(true);
                showLiveList();
                return true;
            }
        });

        searchView.setOnSearchClickListener(new SearchView.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(view, 0);
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent settingsIntent = new Intent(getContext(), SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}