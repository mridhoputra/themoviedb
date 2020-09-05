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

public class SearchTvShowViewModel extends ViewModel {
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private MutableLiveData<ArrayList<Catalogue>> listSearchedTvShows = new MutableLiveData<>();

    public void setListSearchedTvShows(String query) {
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
        String url = "https://api.themoviedb.org/3/search/tv?api_key=" + API_KEY
                + "&language=" + api_language_code
                + "&query=" + query;
        Log.d("Check url: ", url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String tv_show_result = new String(responseBody);
                    JSONObject tv_show_responseObject = new JSONObject(tv_show_result);
                    JSONArray tv_show_responseArray = tv_show_responseObject.getJSONArray("results");
                    if (tv_show_responseArray.length() > 0) {
                        for (int i = 0; i < tv_show_responseArray.length(); i++) {
                            JSONObject tv_show = tv_show_responseArray.getJSONObject(i);
                            Catalogue tv_show_catalogue = new Catalogue();
                            //set action bar title : movie or tv show
                            if (api_language_code.equals("id-ID")) {
                                tv_show_catalogue.setApp_title("Serial TV");
                            } else {
                                tv_show_catalogue.setApp_title("TV Show");
                            }
                            //set tv_show catalogue id
                            tv_show_catalogue.setId_from_api(tv_show.getString("id"));
                            //set tv_show backdrop url
                            String tv_show_backdrop_filename = tv_show.getString("backdrop_path");
                            String tv_show_backdrop_url = "https://image.tmdb.org/t/p/w780" + tv_show_backdrop_filename;
                            tv_show_catalogue.setBackdrop_url(tv_show_backdrop_url);
                            //set tv_show title and released date
                            tv_show_catalogue.setTitle(tv_show.getString("name"));
                            if (tv_show.has("first_air_date")) {
                                tv_show_catalogue.setReleased_date(format_date(tv_show.getString("first_air_date")));
                            } else {
                                tv_show_catalogue.setReleased_date("");
                            }
                            //set tv_show user score
                            int tv_show_user_score = (int) ((tv_show.getDouble("vote_average")) * 10);
                            tv_show_catalogue.setUser_score(String.valueOf(tv_show_user_score).concat("%"));
                            //set tv_show poster url
                            String tv_show_poster_filename = tv_show.getString("poster_path");
                            String tv_show_poster_url = "https://image.tmdb.org/t/p/w154" + tv_show_poster_filename;
                            tv_show_catalogue.setPoster_url(tv_show_poster_url);
                            //set tv_show overview
                            tv_show_catalogue.setDescription(tv_show.getString("overview"));
                            //set tv_show language
                            String tv_show_language_code = tv_show.getString("original_language");
                            Locale tv_show_loc = new Locale(tv_show_language_code);
                            String tv_show_language = tv_show_loc.getDisplayLanguage(tv_show_loc);
                            tv_show_catalogue.setOriginal_language(tv_show_language);
                            //set tv popularity
                            tv_show_catalogue.setPopularity(tv_show.getString("popularity"));
                            //set tv vote count
                            tv_show_catalogue.setVote_count(tv_show.getString("vote_count"));
                            //post these data into MutableLiveData
                            listItems.add(tv_show_catalogue);
                        }
                        listSearchedTvShows.postValue(listItems);
                    } else {
                        listSearchedTvShows.postValue(null);
                    }
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

    public LiveData<ArrayList<Catalogue>> getListTvShows() {
        return listSearchedTvShows;
    }

    private String format_date(String raw_date) {
        String formatted_date = "";
        if (!raw_date.equals("")) {
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
            if (date != null) {
                formatted_date = sdf.format(date);
            }
        }
        return formatted_date;
    }
}
