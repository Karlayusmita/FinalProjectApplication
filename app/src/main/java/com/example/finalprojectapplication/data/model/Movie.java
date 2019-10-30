package com.example.finalprojectapplication.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie implements Parcelable {
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private int id;
    private String poster;
    private String title;
    private String originalLanguage;
    private String releaseDate;
    private String overview;
    private int voteCount;
    private int voteAverage;

    public Movie() {

    }

    public Movie(JSONObject object) {
        try {
            id = object.getInt("id");
            poster = object.getString("poster_path");
            title = object.getString("title");
            originalLanguage = object.getString("original_language");
            releaseDate = object.getString("release_date");
            overview = object.getString("overview");
            voteAverage = object.getInt("vote_average");
            voteCount = object.getInt("vote_count");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Movie(Parcel in) {
        id = in.readInt();
        poster = in.readString();
        title = in.readString();
        originalLanguage = in.readString();
        releaseDate = in.readString();
        overview = in.readString();
        voteCount = in.readInt();
        voteAverage = in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(int voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(poster);
        parcel.writeString(title);
        parcel.writeString(originalLanguage);
        parcel.writeString(releaseDate);
        parcel.writeString(overview);
        parcel.writeInt(voteCount);
        parcel.writeInt(voteAverage);
    }
}
