package com.example.finalprojectapplication.ui.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalprojectapplication.R;
import com.example.finalprojectapplication.data.adapter.ListMovieAdapter;
import com.example.finalprojectapplication.data.model.Movie;
import com.example.finalprojectapplication.data.model.MovieFavorite;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFavoriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Movie> movies;
    private ListMovieAdapter adapter;
    private RealmResults<MovieFavorite> movieFavorites;
    private Realm realm;


    public MovieFavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rv_favorite_movie);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        openRealm();
        movies = new ArrayList<>();
        showRecyclerList();

    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();

    }

    private void loadData() {
        movieFavorites = realm.where(MovieFavorite.class).findAll();
        movies.clear();
        if (!movieFavorites.isEmpty()){
            for (int i = 0; i<movieFavorites.size(); i++){
                Movie test = new Movie();
                test.setId(movieFavorites.get(i).getId());
                test.setPoster(movieFavorites.get(i).getPoster());
                test.setTitle(movieFavorites.get(i).getTitle());
                test.setOverview(movieFavorites.get(i).getOverview());
                test.setOriginalLanguage(movieFavorites.get(i).getOriginalLanguage());
                test.setReleaseDate(movieFavorites.get(i).getReleaseDate());
                test.setVoteCount(movieFavorites.get(i).getVoteCount());
                test.setVoteAverage(movieFavorites.get(i).getVoteAverage());
                movies.add(test);
            }
        }
        adapter.setMovies(movies);
    }

    private void showRecyclerList() {
        adapter = new ListMovieAdapter(getActivity());
        adapter.setMovies(movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        movieFavorites = realm.where(MovieFavorite.class).findAll();
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
