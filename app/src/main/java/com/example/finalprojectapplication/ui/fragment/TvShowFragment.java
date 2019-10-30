package com.example.finalprojectapplication.ui.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.finalprojectapplication.Contract.KEY;
import static com.example.finalprojectapplication.Contract.RESULTS;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ArrayList<TvShow> arrayList;
    private ListTvShowAdapter listTvShowAdapter;

    public TvShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rv_tv_show);
        progressBar = view.findViewById(R.id.pb_tv_show);
        arrayList = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showRecyclerList();
        setHasOptionsMenu(true);
        if (savedInstanceState == null) {
            loadData();
        } else {
            arrayList = savedInstanceState.getParcelableArrayList(KEY);
            if (arrayList != null) {
                listTvShowAdapter.setTvShows(arrayList);
            }
        }
    }

    private void loadData() {
        URL url = Api.getListTvShow();
        Log.e("URL", url.toString());
        new TvShowAsyncTask().execute(url);
    }

    private void showRecyclerList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listTvShowAdapter = new ListTvShowAdapter(getActivity());
        listTvShowAdapter.setTvShows(arrayList);
        recyclerView.setAdapter(listTvShowAdapter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY, arrayList);
    }

    private class TvShowAsyncTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String string = null;
            try {
                string = Network.getFromNetwork(url);
            } catch (IOException error) {
                error.printStackTrace();
            }
            return string;
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
                    arrayList.add(tvShow);
                }
            } catch (JSONException error) {
                error.printStackTrace();
            }
            listTvShowAdapter.setTvShows(arrayList);
        }
    }
}
