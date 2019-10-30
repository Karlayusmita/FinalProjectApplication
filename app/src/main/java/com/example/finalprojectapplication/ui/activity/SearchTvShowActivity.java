package com.example.finalprojectapplication.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.finalprojectapplication.R;
import com.example.finalprojectapplication.data.adapter.ListTvShowAdapter;
import com.example.finalprojectapplication.data.api.Api;
import com.example.finalprojectapplication.data.model.TvShow;
import com.example.finalprojectapplication.data.network.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.finalprojectapplication.Contract.EXTRA_QUERY;
import static com.example.finalprojectapplication.Contract.RESULTS;
import static com.example.finalprojectapplication.Contract.RESULTSS;
import static com.example.finalprojectapplication.Contract.TV;

public class SearchTvShowActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ArrayList<TvShow> tvShows;
    private ListTvShowAdapter listTvShowAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        recyclerView = findViewById(R.id.rv_search_movie);
        progressBar = findViewById(R.id.pb_search_movie);
        tvShows= new ArrayList<>();
        listTvShowAdapter = new ListTvShowAdapter(this);
        String query = getIntent().getStringExtra(EXTRA_QUERY);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(String.format(getString(R.string.search_result), query));
        }

        showRecyclerView();
        ifSaveInstanceState(savedInstanceState, query);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TV, tvShows);
    }

    private void ifSaveInstanceState(Bundle savedInstanceState, String query) {
        if (savedInstanceState == null) {
            URL url = Api.getSearchTvShow(query);
            new SearchMovieActivityAsyncTask().execute(url);
        } else {
            tvShows = savedInstanceState.getParcelableArrayList(TV);
            if (tvShows != null) {
                listTvShowAdapter.setTvShows(tvShows);
            }
        }
    }

    private void showRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listTvShowAdapter);
    }


    private class SearchMovieActivityAsyncTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String test = null;
            try {
                test = Network.getFromNetwork(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return test;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            try {
                JSONObject object = new JSONObject(s);
                JSONArray array = object.getJSONArray(RESULTS);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    TvShow tvShow = new TvShow(jsonObject);
                    tvShows.add(tvShow);
                }
            } catch (JSONException error) {
                error.printStackTrace();
            }
            listTvShowAdapter.setTvShows(tvShows);
        }
    }
}
