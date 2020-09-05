package com.fairlight.submission_5.themoviedb.ui.tv_shows;

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
import com.fairlight.submission_5.themoviedb.viewmodel.SearchTvShowViewModel;
import com.fairlight.submission_5.themoviedb.viewmodel.TvShowViewModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TvShowFragment extends Fragment {
    private TvShowViewModel tvShowViewModel;
    private SearchTvShowViewModel searchTvShowViewModel;
    private RecyclerView rv_catalogue;
    private CatalogueAdapter tvShowAdapter;
    private CatalogueAdapter searchTvShowAdapter;
    private ProgressBar progressBar;
    private TextView noResults;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_tv_shows, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        noResults = view.findViewById(R.id.text_no_tv_show_results);

        rv_catalogue = view.findViewById(R.id.rv_catalogue);
        rv_catalogue.setHasFixedSize(true);
        rv_catalogue.setLayoutManager(new LinearLayoutManager(getContext()));
        tvShowAdapter = new CatalogueAdapter();
        tvShowAdapter.notifyDataSetChanged();
        rv_catalogue.setAdapter(tvShowAdapter);

        tvShowViewModel = new ViewModelProvider(TvShowFragment.this, new ViewModelProvider.NewInstanceFactory()).get(TvShowViewModel.class);

        tvShowViewModel.setListTvShows();
        showLoading(true);
        showLiveList();
    }

    private void showLiveList() {
        tvShowViewModel.getListTvShows().observe(getViewLifecycleOwner(), new Observer<ArrayList<Catalogue>>() {
            @Override
            public void onChanged(ArrayList<Catalogue> catalogues) {
                if (catalogues != null) {
                    tvShowAdapter.setData(catalogues);
                    showLoading(false);
                }
            }
        });

        tvShowAdapter.setOnItemClickCallback(new CatalogueAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Catalogue catalogue) {
                Intent moveWithObjectIntent = new Intent(getContext(), CatalogueDetailActivity.class);
                moveWithObjectIntent.putExtra(CatalogueDetailActivity.EXTRA_CATALOGUE, catalogue);
                String catalogue_type = "TV_SHOW";
                moveWithObjectIntent.putExtra(CatalogueDetailActivity.EXTRA_CATALOGUE_TYPE, catalogue_type);
                startActivity(moveWithObjectIntent);
            }
        });
    }

    private void showSearchedLiveList() {
        searchTvShowViewModel.getListTvShows().observe(getViewLifecycleOwner(), new Observer<ArrayList<Catalogue>>() {
            @Override
            public void onChanged(ArrayList<Catalogue> catalogues) {
                if (catalogues != null) {
                    noResults.setVisibility(View.GONE);
                    searchTvShowAdapter.setData(catalogues);
                    showLoading(false);
                } else {
                    searchTvShowAdapter.setData(new ArrayList<Catalogue>());
                    noResults.setVisibility(View.VISIBLE);
                    showLoading(false);
                }
            }
        });

        searchTvShowAdapter.setOnItemClickCallback(new CatalogueAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Catalogue catalogue) {
                Intent moveWithObjectIntent = new Intent(getContext(), CatalogueDetailActivity.class);
                moveWithObjectIntent.putExtra(CatalogueDetailActivity.EXTRA_CATALOGUE, catalogue);
                String catalogue_type = "TV_SHOW";
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
        menuInflater.inflate(R.menu.tv_show_menu, menu);

        MenuItem search = menu.findItem(R.id.search_tv_show);

        final SearchView searchView = (SearchView) search.getActionView();
        searchView.setQuery("", false);
        searchView.clearFocus();
        searchView.setIconified(false);
        searchView.setQueryHint(getString(R.string.search_tv_show));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();

                searchTvShowAdapter = new CatalogueAdapter();
                searchTvShowAdapter.notifyDataSetChanged();
                rv_catalogue.setAdapter(searchTvShowAdapter);

                searchTvShowViewModel = new ViewModelProvider(TvShowFragment.this, new ViewModelProvider.NewInstanceFactory()).get(SearchTvShowViewModel.class);

                searchTvShowViewModel.setListSearchedTvShows(query);
                showLoading(true);
                showSearchedLiveList();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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
                rv_catalogue.setAdapter(tvShowAdapter);
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