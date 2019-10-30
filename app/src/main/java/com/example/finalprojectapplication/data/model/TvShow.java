package com.example.finalprojectapplication.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class TvShow implements Parcelable {
    public static final Creator<TvShow> CREATOR = new Creator<TvShow>() {
        @Override
        public TvShow createFromParcel(Parcel in) {
            return new TvShow(in);
        }

        @Override
        public TvShow[] newArray(int size) {
            return new TvShow[size];
        }
    };
    private int id;
    private String poster;
    private String name;
    private String originalLanguage;
    private String firstAirDate;
    private String overview;
    private int voteCount;
    private int voteAverage;

    private TvShow(Parcel in) {
        id = in.readInt();
        poster = in.readString();
        name = in.readString();
        originalLanguage = in.readString();
        firstAirDate = in.readString();
        overview = in.readString();
        voteCount = in.readInt();
        voteAverage = in.readInt();
    }

    public TvShow() {
    }

    public TvShow(JSONObject object) {
        try {
            id = object.getInt("id");
            poster = object.getString("poster_path");
            name = object.getString("name");
            originalLanguage = object.getString("original_language");
            firstAirDate = object.getString("first_air_date");
            overview = object.getString("overview");
            voteCount = object.getInt("vote_count");
            voteAverage = object.getInt("vote_average");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
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
        parcel.writeString(name);
        parcel.writeString(originalLanguage);
        parcel.writeString(firstAirDate);
        parcel.writeString(overview);
        parcel.writeInt(voteCount);
        parcel.writeInt(voteAverage);
    }
}
