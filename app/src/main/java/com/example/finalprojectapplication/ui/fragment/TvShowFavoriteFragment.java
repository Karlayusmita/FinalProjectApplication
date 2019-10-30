package com.example.finalprojectapplication.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.finalprojectapplication.R;
import com.example.finalprojectapplication.data.adapter.ListTvShowAdapter;
import com.example.finalprojectapplication.data.model.TvShow;
import com.example.finalprojectapplication.data.model.TvShowFavorite;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFavoriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ArrayList<TvShow> tvShowArrayList;
    private ListTvShowAdapter adapter;
    private RealmResults<TvShowFavorite> tvShowFavorite;
    private Realm realm;

    public TvShowFavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rv_favorite_tv_show);
        progressBar = view.findViewById(R.id.pb_favorite_tv_show);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        openRealm();
        tvShowArrayList = new ArrayList<>();
        showRecyclerList();

    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        tvShowFavorite = realm.where(TvShowFavorite.class).findAll();
        tvShowArrayList.clear();
        if (!tvShowFavorite.isEmpty()) {
            for (int i = 0; i < tvShowFavorite.size(); i++) {
                TvShow test = new TvShow();
                test.setId(tvShowFavorite.get(i).getId());
                test.setPoster(tvShowFavorite.get(i).getPoster());
                test.setName(tvShowFavorite.get(i).getName());
                test.setOverview(tvShowFavorite.get(i).getOverview());
                test.setOriginalLanguage(tvShowFavorite.get(i).getOriginalLanguage());
                test.setFirstAirDate(tvShowFavorite.get(i).getFirstAirDate());
                test.setVoteCount(tvShowFavorite.get(i).getVoteCount());
                test.setVoteAverage(tvShowFavorite.get(i).getVoteAverage());
                tvShowArrayList.add(test);
            }
        }
        adapter.setTvShows(tvShowArrayList);
    }

    private void showRecyclerList() {
        adapter = new ListTvShowAdapter(getActivity());
        adapter.setTvShows(tvShowArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        tvShowFavorite = realm.where(TvShowFavorite.class).findAll();
    }

    private void openRealm() {
        try {
            Realm.init(Objects.requireNonNull(getActivity()));
            realm = Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException e) {
            if (Realm.getDefaultConfiguration() != null) {
                Realm.deleteRealm(Realm.getDefaultConfiguration());
                realm = Realm.getDefaultInstance();
            }
        }
    }
}
