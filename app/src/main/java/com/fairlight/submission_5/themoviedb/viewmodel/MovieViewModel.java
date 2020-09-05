package com.fairlight.submission_5.themoviedb.viewmodel;

import android.util.Log;

import com.fairlight.submission_5.themoviedb.BuildConfig;
import com.fairlight.submission_5.themoviedb.model.Catalogue;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import cz.msebera.android.httpclient.Header;

public class MovieViewModel extends ViewModel {
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private MutableLiveData<ArrayList<Catalogue>> listMovies = new MutableLiveData<>();

    public void setListMovies() {
        //get app iso code language, will be used for taking data from api later
        String iso_code = Locale.getDefault().getLanguage();
        final String api_language_code;
        if (iso_code.equals("in")) {
            api_language_code = "id-ID";
        } else {
            api_language_code = "en-US";
        }
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Catalogue> listItems = new ArrayList<>();
        //locale_language should be "en-US" and "id-ID" to match API url
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&language=" + api_language_code;
        Log.d("Check url: ", url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String movie_result = new String(responseBody);
                    JSONObject movie_responseObject = new JSONObject(movie_result);
                    JSONArray movie_responseArray = movie_responseObject.getJSONArray("results");
                    for (int i = 0; i < movie_responseArray.length(); i++) {
                        JSONObject movie = movie_responseArray.getJSONObject(i);
                        Catalogue movie_catalogue = new Catalogue();
                        //set action bar title : movie
                        if (api_language_code.equals("id-ID")) {
                            movie_catalogue.setApp_title("Film");
                        } else {
                            movie_catalogue.setApp_title("Movie");
                        }
                        //set movie catalogue id, id taken from api
                        movie_catalogue.setId_from_api(movie.getString("id"));
                        //set movie backdrop url
                        String movie_backdrop_filename = movie.getString("backdrop_path");
                        String movie_backdrop_url = "https://image.tmdb.org/t/p/w780" + movie_backdrop_filename;
                        movie_catalogue.setBackdrop_url(movie_backdrop_url);
                        //set movie title
                        movie_catalogue.setTitle(movie.getString("original_title"));
                        //set movie release date
                        movie_catalogue.setReleased_date(format_date(movie.getString("release_date")));
                        //set movie user score
                        int movie_user_score = (int) ((movie.getDouble("vote_average")) * 10);
                        movie_catalogue.setUser_score(String.valueOf(movie_user_score).concat("%"));
                        //set movie poster url
                        String movie_poster_filename = movie.getString("poster_path");
                        String movie_poster_url = "https://image.tmdb.org/t/p/w154" + movie_poster_filename;
                        movie_catalogue.setPoster_url(movie_poster_url);
                        //set movie overview
                        movie_catalogue.setDescription(movie.getString("overview"));
                        //set movie language
                        String movie_language_code = movie.getString("original_language");
                        Locale movie_loc = new Locale(movie_language_code);
                        String movie_language = movie_loc.getDisplayLanguage(movie_loc);
                        movie_catalogue.setOriginal_language(movie_language);
                        //set movie popularity
                        movie_catalogue.setPopularity(movie.getString("popularity"));
                        //set movie vote count
                        movie_catalogue.setVote_count(movie.getString("vote_count"));
                        //add these data to arraylist
                        listItems.add(movie_catalogue);
                    }
                    listMovies.postValue(listItems);
                } catch (Exception e) {
                    Log.e("Exception: ", "Fail to fetch data");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("onFailure: ", "url maybe wrong");
            }
        });
    }

    public LiveData<ArrayList<Catalogue>> getListMovies() {
        return listMovies;
    }

    private String format_date(String raw_date) {
        String locale_language = Locale.getDefault().getLanguage();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(raw_date);
        } catch (ParseException e) {
            Log.e("Parse Error", "try again");
        }
        if (locale_language.equals("en")) {
            sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        } else if (locale_language.equals("in")) {
            sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        }
        String formatted_date = "";
        if (date != null) {
            formatted_date = sdf.format(date);
        }
        return formatted_date;
    }
}