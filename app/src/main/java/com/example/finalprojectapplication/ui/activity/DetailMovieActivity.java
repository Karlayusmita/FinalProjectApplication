package com.example.finalprojectapplication.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.finalprojectapplication.R;
import com.example.finalprojectapplication.data.model.Movie;
import com.example.finalprojectapplication.data.model.MovieFavorite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

import static com.example.finalprojectapplication.Contract.BASE_URL_POSTER;
import static com.example.finalprojectapplication.Contract.ID;
import static com.example.finalprojectapplication.Contract.MOVIE_DATA;
import static com.example.finalprojectapplication.Contract.SIZE;

public class DetailMovieActivity extends AppCompatActivity {
    private Realm realm;
    private boolean favorite = false;
    private Menu menu;
    private int id;
    private String poster;
    private String title;
    private String originalLanguage;
    private String releaseDate;
    private String overview;
    private int voteAverage;
    private int voteCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        ImageView detailPoster = findViewById(R.id.detail_poster);
        TextView detailTitle = findViewById(R.id.detail_title);
        TextView detailLanguage = findViewById(R.id.detail_original_language);
        TextView detailRelease = findViewById(R.id.detail_release_date);
        TextView detailOverview = findViewById(R.id.detail_overview);
        TextView detailVoteAverage = findViewById(R.id.vote_average);
        RatingBar detailVoteCount = findViewById(R.id.vote_count);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.movie_detail);
        }

        openRealm();

        Movie movie = getIntent().getParcelableExtra(MOVIE_DATA);
        if (movie != null) {
            this.id = movie.getId();
            this.poster = movie.getPoster();
            this.title = movie.getTitle();
            this.originalLanguage = movie.getOriginalLanguage();
            this.releaseDate = movie.getReleaseDate();
            this.overview = movie.getOverview();
            this.voteAverage = movie.getVoteAverage();
            this.voteCount = movie.getVoteCount();

            Glide.with(this)
                    .load(BASE_URL_POSTER + SIZE + movie.getPoster())
                    .apply(new RequestOptions().override(96, 144))
                    .into(detailPoster);
            detailTitle.setText(movie.getTitle());
            detailLanguage.setText(movie.getOriginalLanguage());
            detailRelease.setText(movie.getReleaseDate());
            detailOverview.setText(movie.getOverview());
            detailVoteAverage.setText(String.valueOf(movie.getVoteAverage()));
            detailVoteCount.setRating((float) movie.getVoteCount() / 100);
        }

        favoriteState();
    }

    private void favoriteState() {
        RealmResults<MovieFavorite> movieFavorites;
        movieFavorites = realm.where(MovieFavorite.class).equalTo(ID, id).findAll();
        favorite = !movieFavorites.isEmpty();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        setFavorite();
        return super.onCreateOptionsMenu(menu);
    }

    private void setFavorite() {
        if (favorite) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.love));
        } else {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.love_border));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_favorite) {
            if (favorite) {
                boolean remove = removeFromFavorite();
                if (remove) {
                    favorite = false;
                    setFavorite();
                    setToast(getString(R.string.remove));
                } else {
                    setToast(getString(R.string.failed_remove));
                }
            } else {
                favorite = addToFavorite();
                if (favorite) {
                    setFavorite();
                    setToast(getString(R.string.add));
                } else {
                    setToast(getString(R.string.failed_add));
                }
            }
        }
        return false;
    }

    private void setToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    private boolean addToFavorite() {
        openRealm();

        MovieFavorite movieFavorite = new MovieFavorite();
        movieFavorite.setId(this.id);
        movieFavorite.setPoster(this.poster);
        movieFavorite.setTitle(this.title);
        movieFavorite.setOriginalLanguage(this.originalLanguage);
        movieFavorite.setReleaseDate(this.releaseDate);
        movieFavorite.setOverview(this.overview);
        movieFavorite.setVoteAverage(this.voteAverage);
        movieFavorite.setVoteCount(this.voteCount);

        realm = Realm.getDefaultInstance();
        MovieFavorite test = realm.where(MovieFavorite.class).equalTo(ID, this.id).findFirst();
        if (test == null) {
            try {
                realm.beginTransaction();
                realm.copyToRealm(movieFavorite);
                realm.commitTransaction();
                realm.close();
                return true;
            } catch (Exception error) {
                error.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private boolean removeFromFavorite() {
        try {
            openRealm();
            realm.beginTransaction();

            MovieFavorite movieFavorite = realm.where(MovieFavorite.class).equalTo(ID, id).findFirst();
            if (movieFavorite != null) {
                movieFavorite.deleteFromRealm();
                realm.commitTransaction();
                while (realm.isInTransaction()) {
                    Log.e("realm", "in transaction");
                }
                realm.close();
                return true;
            }
        } catch (Exception error) {
            error.printStackTrace();
            return false;
        }
        return false;
    }

    private void openRealm() {
        try {
            Realm.init(this);
            realm = Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException e) {
            if (Realm.getDefaultConfiguration() != null) {
                Realm.deleteRealm(Realm.getDefaultConfiguration());
                realm = Realm.getDefaultInstance();
                realm.beginTransaction();
            }
        }
    }
}
