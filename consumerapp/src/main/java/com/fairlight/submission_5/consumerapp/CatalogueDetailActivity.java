package com.fairlight.submission_5.consumerapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fairlight.submission_5.consumerapp.model.Catalogue;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class CatalogueDetailActivity extends AppCompatActivity {
    public static final String EXTRA_CATALOGUE = "extra_catalogue";
    public static String EXTRA_CATALOGUE_TYPE = "extra_catalogue_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue_detail);

        Catalogue catalogue = getIntent().getParcelableExtra(EXTRA_CATALOGUE);
        EXTRA_CATALOGUE_TYPE = getIntent().getStringExtra(EXTRA_CATALOGUE_TYPE);

        if (getSupportActionBar() != null) {
            if (catalogue != null) {
                getSupportActionBar().setTitle(catalogue.getApp_title());
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ProgressBar progressBar = findViewById(R.id.detailed_activity_progressBar);
        ImageView imgBackdrop = findViewById(R.id.img_detailed_catalogue_backdrop);
        TextView tvTitle = findViewById(R.id.tv_detailed_catalogue_title);
        TextView tvReleasedDate = findViewById(R.id.tv_detailed_catalogue_released_date);
        TextView tvUserScore = findViewById(R.id.tv_detailed_catalogue_user_score);
        ImageView imgPoster = findViewById(R.id.img_detailed_catalogue_poster);
        TextView tvDescription = findViewById(R.id.tv_detailed_catalogue_description);
        TextView tvOriginalLanguage = findViewById(R.id.tv_detailed_catalogue_original_language);
        TextView tvPopularity = findViewById(R.id.tv_detailed_catalogue_popularity);
        TextView tvVoteCount = findViewById(R.id.tv_detailed_catalogue_vote_count);


        progressBar.setVisibility(View.VISIBLE);

        if (Objects.requireNonNull(catalogue).getBackdrop_url().contains("null")) {
            Glide.with(this)
                    .load(R.drawable.backdrop_not_yet_available)
                    .into(imgBackdrop);
        } else {
            Glide.with(this)
                    .load(Objects.requireNonNull(catalogue).getBackdrop_url())
                    .into(imgBackdrop);
        }
        tvTitle.setText(catalogue.getTitle());
        tvReleasedDate.setText(catalogue.getReleased_date());
        tvUserScore.setText(catalogue.getUser_score());
        if (catalogue.getPoster_url().contains("null")) {
            Glide.with(this)
                    .load(R.drawable.poster_not_yet_available)
                    .into(imgPoster);
        } else {
            Glide.with(this)
                    .load(Objects.requireNonNull(catalogue).getPoster_url())
                    .into(imgPoster);
        }
        if (catalogue.getDescription().equals("")) {
            tvDescription.setText(R.string.no_overview_available);
        } else {
            tvDescription.setText(catalogue.getDescription());
        }
        tvOriginalLanguage.setText(catalogue.getOriginal_language());
        tvPopularity.setText(catalogue.getPopularity());
        tvVoteCount.setText(catalogue.getVote_count());

        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
