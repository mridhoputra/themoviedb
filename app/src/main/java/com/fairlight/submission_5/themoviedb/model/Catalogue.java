package com.fairlight.submission_5.themoviedb.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Catalogue implements Parcelable {
    public static final Creator<Catalogue> CREATOR = new Creator<Catalogue>() {
        @Override
        public Catalogue createFromParcel(Parcel in) {
            return new Catalogue(in);
        }

        @Override
        public Catalogue[] newArray(int size) {
            return new Catalogue[size];
        }
    };
    private String app_title;
    private String id_from_api;
    private String backdrop_url;
    private String title;
    private String released_date;
    private String user_score;
    private String poster_url;
    private String description;
    private String original_language;
    private String popularity;
    private String vote_count;
    private int is_favorite;

    public Catalogue() {

    }

    public Catalogue(String app_title, String id_from_api, String backdrop_url, String title, String released_date,
                     String user_score, String poster_url, String description, String original_language,
                     String popularity, String vote_count, int is_favorite) {
        this.app_title = app_title;
        this.id_from_api = id_from_api;
        this.backdrop_url = backdrop_url;
        this.title = title;
        this.released_date = released_date;
        this.user_score = user_score;
        this.poster_url = poster_url;
        this.description = description;
        this.original_language = original_language;
        this.popularity = popularity;
        this.vote_count = vote_count;
        this.is_favorite = is_favorite;
    }

    protected Catalogue(Parcel in) {
        app_title = in.readString();
        id_from_api = in.readString();
        backdrop_url = in.readString();
        title = in.readString();
        released_date = in.readString();
        user_score = in.readString();
        poster_url = in.readString();
        description = in.readString();
        original_language = in.readString();
        popularity = in.readString();
        vote_count = in.readString();
        is_favorite = in.readInt();
    }

    public String getApp_title() {
        return app_title;
    }

    public void setApp_title(String app_title) {
        this.app_title = app_title;
    }

    public String getId_from_api() {
        return id_from_api;
    }

    public void setId_from_api(String id_from_api) {
        this.id_from_api = id_from_api;
    }

    public String getBackdrop_url() {
        return backdrop_url;
    }

    public void setBackdrop_url(String backdrop_url) {
        this.backdrop_url = backdrop_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleased_date() {
        return released_date;
    }

    public void setReleased_date(String released_date) {
        this.released_date = released_date;
    }

    public String getUser_score() {
        return user_score;
    }

    public void setUser_score(String user_score) {
        this.user_score = user_score;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    public int getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(int is_favorite) {
        this.is_favorite = is_favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(app_title);
        dest.writeString(id_from_api);
        dest.writeString(backdrop_url);
        dest.writeString(title);
        dest.writeString(released_date);
        dest.writeString(user_score);
        dest.writeString(poster_url);
        dest.writeString(description);
        dest.writeString(original_language);
        dest.writeString(popularity);
        dest.writeString(vote_count);
        dest.writeInt(is_favorite);
    }
}
