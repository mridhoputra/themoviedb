package com.fairlight.submission_5.themoviedb.ui.detaileditem;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fairlight.submission_5.themoviedb.R;
import com.fairlight.submission_5.themoviedb.model.Catalogue;
import com.fairlight.submission_5.themoviedb.widget.FavoriteMovieWidget;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_APP_TITLE;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_BACKDROP_URL;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_CONTENT_URI;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_DESCRIPTION;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_ID_FROM_API;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_IS_FAVORITE;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_ORIGINAL_LANGUAGE;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_POPULARITY;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_POSTER_URL;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_RELEASED_DATE;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_TITLE;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_USER_SCORE;
import static com.fairlight.submission_5.themoviedb.moviedb.MovieDBContract.MovieColumns.MOVIE_VOTE_COUNT;
import static com.fairlight.submission_5.themoviedb.tvshowdb.TvShowDBContract.TvShowColumns.TV_SHOW_APP_TITLE;
import static com.fairlight.submission_5.themoviedb.tvshowdb.TvShowDBContract.TvShowColumns.TV_SHOW_BACKDROP_URL;
import static com.fairlight.submission_5.themoviedb.tvshowdb.TvShowDBContract.TvShowColumns.TV_SHOW_CONTENT_URI;
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

public class CatalogueDetailActivity extends AppCompatActivity {
    public static final String EXTRA_CATALOGUE = "extra_catalogue";
    public static String EXTRA_CATALOGUE_TYPE = "extra_catalogue_type";
    private int isFavorite;
    private Uri uriWithTitle;

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

        switch (EXTRA_CATALOGUE_TYPE) {
            case "MOVIE":
                uriWithTitle = Uri.parse(MOVIE_CONTENT_URI + "/" + MOVIE_TITLE + "/" + catalogue.getTitle());
                if (uriWithTitle != null) {
                    Cursor cursor = getContentResolver().query(uriWithTitle, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        catalogue.setIs_favorite(1);
                        cursor.close();
                    } else {
                        catalogue.setIs_favorite(0);
                    }
                    isFavorite = catalogue.getIs_favorite();
                }
                break;
            case "TV_SHOW":
                uriWithTitle = Uri.parse(TV_SHOW_CONTENT_URI + "/" + TV_SHOW_TITLE + "/" + catalogue.getTitle());
                if (uriWithTitle != null) {
                    Cursor cursor = getContentResolver().query(uriWithTitle, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        catalogue.setIs_favorite(1);
                        cursor.close();
                    } else {
                        catalogue.setIs_favorite(0);
                    }
                    isFavorite = catalogue.getIs_favorite();
                }
                break;
            default:
                Toast.makeText(this, "Catalogue Type not found", Toast.LENGTH_SHORT).show();
                break;
        }

        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailed_layout_menu, menu);
        if (isFavorite == 1) {
            menu.findItem(R.id.action_set_favorite).setIcon(R.drawable.ic_favorite_yellow_24dp);
        } else {
            menu.findItem(R.id.action_set_favorite).setIcon(R.drawable.ic_favorite_white_24dp);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_set_favorite:
                //set selected movie or tv show to database
                Catalogue catalogue = getIntent().getParcelableExtra(EXTRA_CATALOGUE);
                switch (EXTRA_CATALOGUE_TYPE) {
                    case "MOVIE":
                        if (isFavorite == 1) {
                            if (catalogue != null) {
                                getContentResolver().delete(uriWithTitle, null, null);

                                //update widget if favorite movie list are changed
                                updateWidget();
                            }
                            item.setIcon(R.drawable.ic_favorite_white_24dp);
                            Toast.makeText(this, R.string.unset_favorite, Toast.LENGTH_SHORT).show();
                        } else {
                            if (catalogue != null) {
                                ContentValues values = new ContentValues();
                                values.put(MOVIE_APP_TITLE, catalogue.getApp_title());
                                values.put(MOVIE_ID_FROM_API, catalogue.getId_from_api());
                                values.put(MOVIE_BACKDROP_URL, catalogue.getBackdrop_url());
                                values.put(MOVIE_TITLE, catalogue.getTitle());
                                values.put(MOVIE_RELEASED_DATE, catalogue.getReleased_date());
                                values.put(MOVIE_USER_SCORE, catalogue.getUser_score());
                                values.put(MOVIE_POSTER_URL, catalogue.getPoster_url());
                                values.put(MOVIE_DESCRIPTION, catalogue.getDescription());
                                values.put(MOVIE_ORIGINAL_LANGUAGE, catalogue.getOriginal_language());
                                values.put(MOVIE_POPULARITY, catalogue.getPopularity());
                                values.put(MOVIE_VOTE_COUNT, catalogue.getVote_count());

                                //set to favorite
                                catalogue.setIs_favorite(1);

                                values.put(MOVIE_IS_FAVORITE, catalogue.getIs_favorite());

                                getContentResolver().insert(MOVIE_CONTENT_URI, values);

                                //update widget if favorite movie list are changed
                                updateWidget();
                            }
                            item.setIcon(R.drawable.ic_favorite_yellow_24dp);
                            Toast.makeText(this, R.string.set_favorite, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "TV_SHOW":
                        if (isFavorite == 1) {
                            if (catalogue != null) {
                                getContentResolver().delete(uriWithTitle, null, null);
                            }
                            item.setIcon(R.drawable.ic_favorite_white_24dp);
                            Toast.makeText(this, R.string.unset_favorite, Toast.LENGTH_SHORT).show();
                        } else {
                            if (catalogue != null) {
                                ContentValues values = new ContentValues();
                                values.put(TV_SHOW_APP_TITLE, catalogue.getApp_title());
                                values.put(TV_SHOW_ID_FROM_API, catalogue.getId_from_api());
                                values.put(TV_SHOW_BACKDROP_URL, catalogue.getBackdrop_url());
                                values.put(TV_SHOW_TITLE, catalogue.getTitle());
                                values.put(TV_SHOW_RELEASED_DATE, catalogue.getReleased_date());
                                values.put(TV_SHOW_USER_SCORE, catalogue.getUser_score());
                                values.put(TV_SHOW_POSTER_URL, catalogue.getPoster_url());
                                values.put(TV_SHOW_DESCRIPTION, catalogue.getDescription());
                                values.put(TV_SHOW_ORIGINAL_LANGUAGE, catalogue.getOriginal_language());
                                values.put(TV_SHOW_POPULARITY, catalogue.getPopularity());
                                values.put(TV_SHOW_VOTE_COUNT, catalogue.getVote_count());

                                //set to favorite
                                catalogue.setIs_favorite(1);

                                values.put(TV_SHOW_IS_FAVORITE, catalogue.getIs_favorite());

                                getContentResolver().insert(TV_SHOW_CONTENT_URI, values);
                            }
                            item.setIcon(R.drawable.ic_favorite_yellow_24dp);
                            Toast.makeText(this, R.string.set_favorite, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        Toast.makeText(this, "Catalogue Type not found", Toast.LENGTH_SHORT).show();
                        break;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateWidget() {
        Intent intent = new Intent(this, FavoriteMovieWidget.class);
        intent.setAction(FavoriteMovieWidget.UPDATE_WIDGET);
        sendBroadcast(intent);
    }
}
