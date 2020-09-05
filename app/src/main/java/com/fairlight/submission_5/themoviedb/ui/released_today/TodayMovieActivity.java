package com.fairlight.submission_5.themoviedb.ui.released_today;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.fairlight.submission_5.themoviedb.MainActivity;
import com.fairlight.submission_5.themoviedb.R;
import com.fairlight.submission_5.themoviedb.adapter.CatalogueAdapter;
import com.fairlight.submission_5.themoviedb.model.Catalogue;
import com.fairlight.submission_5.themoviedb.receiver.ReleaseTodayReminderReceiver;
import com.fairlight.submission_5.themoviedb.ui.detaileditem.CatalogueDetailActivity;
import com.fairlight.submission_5.themoviedb.viewmodel.ReleaseTodayMovieViewModel;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TodayMovieActivity extends AppCompatActivity {
    private ReleaseTodayMovieViewModel releaseTodayMovieViewModel;
    private CatalogueAdapter releaseTodayMovieAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today__movie_);

        progressBar = findViewById(R.id.activity_today_movie_progressBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_title_released_today);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView rv_catalogue = findViewById(R.id.activity_today_movie_rv_catalogue);
        rv_catalogue.setHasFixedSize(true);
        rv_catalogue.setLayoutManager(new LinearLayoutManager(this));
        releaseTodayMovieAdapter = new CatalogueAdapter();
        releaseTodayMovieAdapter.notifyDataSetChanged();
        rv_catalogue.setAdapter(releaseTodayMovieAdapter);

        releaseTodayMovieViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ReleaseTodayMovieViewModel.class);

        releaseTodayMovieViewModel.setListTodayMovies();
        showLoading(true);
        showLiveList();

        //dismiss all notifications
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(ReleaseTodayReminderReceiver.SINGLE_MOVIE_ID);
            notificationManager.cancel(ReleaseTodayReminderReceiver.MULTIPLE_MOVIE_ID);
        }
    }

    private void showLiveList() {
        releaseTodayMovieViewModel.getListTodayMovies().observe(this, new Observer<ArrayList<Catalogue>>() {
            @Override
            public void onChanged(ArrayList<Catalogue> catalogues) {
                if (catalogues != null) {
                    releaseTodayMovieAdapter.setData(catalogues);
                    showLoading(false);
                } else {
                    releaseTodayMovieAdapter.setData(new ArrayList<Catalogue>());
                    showLoading(false);
                }
            }
        });

        releaseTodayMovieAdapter.setOnItemClickCallback(new CatalogueAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Catalogue catalogue) {
                Intent moveWithObjectIntent = new Intent(TodayMovieActivity.this, CatalogueDetailActivity.class);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// app icon in action bar clicked; go home
            Intent intent = new Intent(TodayMovieActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(TodayMovieActivity.this, MainActivity.class);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
        finish();
    }
}
