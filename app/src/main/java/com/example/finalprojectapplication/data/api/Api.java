package com.example.finalprojectapplication.data.api;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

import static com.example.finalprojectapplication.Contract.API;
import static com.example.finalprojectapplication.Contract.API_KEY;
import static com.example.finalprojectapplication.Contract.BASE_URL;
import static com.example.finalprojectapplication.Contract.BASE_URL_POSTER;
import static com.example.finalprojectapplication.Contract.BASE_URL_SEARCH;
import static com.example.finalprojectapplication.Contract.GTE;
import static com.example.finalprojectapplication.Contract.LANGUAGE;
import static com.example.finalprojectapplication.Contract.LANGUAGE_VALUE;
import static com.example.finalprojectapplication.Contract.LTE;
import static com.example.finalprojectapplication.Contract.MOVIE;
import static com.example.finalprojectapplication.Contract.QUERY;
import static com.example.finalprojectapplication.Contract.SIZE;
import static com.example.finalprojectapplication.Contract.TV;

public class Api {
    public static URL getListMovie() {
        //https://api.themoviedb.org/3/discover/movie?api_key=cc95b5cc8e36b6e407ca52aebbf908ec&language=en-US
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE)
                .appendQueryParameter(API, API_KEY)
                .appendQueryParameter(LANGUAGE, LANGUAGE_VALUE)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL getListTvShow() {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(TV)
                .appendQueryParameter(API, API_KEY)
                .appendQueryParameter(LANGUAGE, LANGUAGE_VALUE)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL getPoster(String poster_path) {
        // https://image.tmdb.org/t/p/w185/bI37vIHSH7o4IVkq37P8cfxQGMx.jpg
        poster_path = poster_path.startsWith("/") ? poster_path.substring(1) : poster_path;
        Uri uri = Uri.parse(BASE_URL_POSTER).buildUpon()
                .appendPath(SIZE)
                .appendPath(poster_path)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL getSearchMovie(String query) {
        //https://api.themoviedb.org/3/search/movie?api_key={API KEY}&language=en-US&query={MOVIE NAME}
        Uri uri = Uri.parse(BASE_URL_SEARCH).buildUpon()
                .appendPath(MOVIE)
                .appendQueryParameter(API, API_KEY)
                .appendQueryParameter(LANGUAGE, LANGUAGE_VALUE)
                .appendQueryParameter(QUERY, query)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL getSearchTvShow(String query) {
        //https://api.themoviedb.org/3/search/movie?api_key={API KEY}&language=en-US&query={MOVIE NAME}
        Uri uri = Uri.parse(BASE_URL_SEARCH).buildUpon()
                .appendPath(TV)
                .appendQueryParameter(API, API_KEY)
                .appendQueryParameter(LANGUAGE, LANGUAGE_VALUE)
                .appendQueryParameter(QUERY, query)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL getUpComingMovie(String todayDate) {
        // https://api.themoviedb.org/3/discover/movie?api_key={API KEY}&primary_release_date.gte={TODAY DATE}&primary_release_date.lte={TODAY DATE}
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE)
                .appendQueryParameter(API, API_KEY)
                .appendQueryParameter(GTE, todayDate)
                .appendQueryParameter(LTE, todayDate)
                .build();
        URL url = null;

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
