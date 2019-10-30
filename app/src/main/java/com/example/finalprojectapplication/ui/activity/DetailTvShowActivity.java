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
import com.example.finalprojectapplication.data.model.TvShow;
import com.example.finalprojectapplication.data.model.TvShowFavorite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

import static com.example.finalprojectapplication.Contract.BASE_URL_POSTER;
import static com.example.finalprojectapplication.Contract.ID;
import static com.example.finalprojectapplication.Contract.SIZE;
import static com.example.finalprojectapplication.Contract.TV_SHOW_DATA;

public class DetailTvShowActivity extends AppCompatActivity {
    private Realm realm;
    private boolean favorite = false;
    private Menu menu;
    private int id;
    private String poster;
    private String name;
    private String originalLanguage;
    private String firstAirDate;
    private String overview;
    private int voteAverage;
    private int voteCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv_show);

        ImageView detailPoster = findViewById(R.id.detail_poster);
        TextView detailName = findViewById(R.id.detail_name);
        TextView detailLanguage = findViewById(R.id.detail_original_language);
        TextView detailFirstAir = findViewById(R.id.first_air_date);
        TextView detailOverview = findViewById(R.id.detail_overview);
        TextView detailVoteAverage = findViewById(R.id.vote_average);
        RatingBar detailVoteCount = findViewById(R.id.vote_count);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.tv_show_detail));
        }

        openRealm();

        TvShow tvShow = getIntent().getParcelableExtra(TV_SHOW_DATA);
        if (tvShow != null) {
            this.id = tvShow.getId();
            this.poster = tvShow.getPoster();
            this.name = tvShow.getName();
            this.originalLanguage = tvShow.getOriginalLanguage();
            this.firstAirDate = tvShow.getFirstAirDate();
            this.overview = tvShow.getOverview();
            this.voteAverage = tvShow.getVoteAverage();
            this.voteCount = tvShow.getVoteCount();

            Glide.with(this)
                    .load(BASE_URL_POSTER + SIZE + tvShow.getPoster())
                    .apply(new RequestOptions().override(96, 144))
                    .into(detailPoster);
            detailName.setText(tvShow.getName());
            detailLanguage.setText(tvShow.getOriginalLanguage());
            detailFirstAir.setText(tvShow.getFirstAirDate());
            detailOverview.setText(tvShow.getOverview());
            detailVoteAverage.setText(String.valueOf(tvShow.getVoteAverage()));
            detailVoteCount.setRating((float) tvShow.getVoteCount() / 2);
        }

        favoriteState();
    }

    private void favoriteState() {
        RealmResults<TvShowFavorite> tvShowFavorites;
        tvShowFavorites = realm.where(TvShowFavorite.class).equalTo(ID, id).findAll();
        favorite = !tvShowFavorites.isEmpty();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detail_menu, menu);
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

    private void openRealm() {
        try {
            Realm.init(this);
            realm = Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException error) {
            if (Realm.getDefaultConfiguration() != null) {
                Realm.deleteRealm(Realm.getDefaultConfiguration());
                realm = Realm.getDefaultInstance();
                realm.beginTransaction();
            }
        }
    }

    private boolean addToFavorite() {
        openRealm();

        TvShowFavorite tvShowFavorite = new TvShowFavorite();
        tvShowFavorite.setId(this.id);
        tvShowFavorite.setPoster(this.poster);
        tvShowFavorite.setName(this.name);
        tvShowFavorite.setOriginalLanguage(this.originalLanguage);
        tvShowFavorite.setFirstAirDate(this.firstAirDate);
        tvShowFavorite.setOverview(this.overview);
        tvShowFavorite.setVoteAverage(this.voteAverage);
        tvShowFavorite.setVoteCount(this.voteCount);

        realm = Realm.getDefaultInstance();
        TvShowFavorite test = realm.where(TvShowFavorite.class).equalTo(ID, this.id).findFirst();
        if (test == null) {
            try {
                realm.beginTransaction();
                realm.copyToRealm(tvShowFavorite);
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

            TvShowFavorite tvShowFavorite = realm.where(TvShowFavorite.class).equalTo(ID, id).findFirst();
            if (tvShowFavorite != null) {
                tvShowFavorite.deleteFromRealm();
                realm.commitTransaction();
                while (realm.isInTransaction()) {
                    Log.e("Realm", "in transaction");
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
}
