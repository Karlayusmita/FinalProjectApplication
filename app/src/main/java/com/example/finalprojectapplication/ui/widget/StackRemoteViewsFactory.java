package com.example.finalprojectapplication.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.example.finalprojectapplication.R;
import com.example.finalprojectapplication.data.model.Movie;
import com.example.finalprojectapplication.data.model.MovieFavorite;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

import static com.example.finalprojectapplication.Contract.BASE_URL_POSTER;
import static com.example.finalprojectapplication.Contract.EXTRA_ITEM;
import static com.example.finalprojectapplication.Contract.SIZE;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context context;
    private ArrayList<Movie> movieArrayList;
    private Realm realm;

    public StackRemoteViewsFactory(Context context) {
        this.context = context;
        movieArrayList = new ArrayList<>();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        openRealm();
        RealmResults<MovieFavorite> movieFavorites = realm.where(MovieFavorite.class).findAll();
        if (movieFavorites != null) {
            for (int i = 0; i < movieFavorites.size(); i++) {
                Movie test = new Movie();
                test.setId(movieFavorites.get(i).getId());
                test.setPoster(movieFavorites.get(i).getPoster());
                test.setTitle(movieFavorites.get(i).getTitle());
                movieArrayList.add(test);
            }
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return movieArrayList.size();
    }



    @Override
    public RemoteViews getViewAt(int i) {
        openRealm();
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        Bitmap bitmap = null;

        if (movieArrayList.size() > 0) {
            try {
                bitmap = Glide.with(context)
                        .asBitmap()
                        .load(BASE_URL_POSTER + SIZE + movieArrayList.get(i).getPoster())
                        .into(800, 600).get();
                Log.d("Widget Load", "Success");
            } catch (InterruptedException | ExecutionException e) {
                Log.d("Widget Load", "error");
            }
            String title = movieArrayList.get(i).getTitle();
            remoteViews.setImageViewBitmap(R.id.image_widget, bitmap);
            remoteViews.setTextViewText(R.id.title_view, title);
        }

        Bundle extras = new Bundle();
        extras.putInt(EXTRA_ITEM, i);
        Intent fillIntent = new Intent();
        fillIntent.putExtras(extras);

        remoteViews.setOnClickFillInIntent(R.id.image_widget, fillIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private void openRealm() {
        try {
            Realm.init(context);
            realm = Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException e) {
            if (Realm.getDefaultConfiguration() != null) {
                Realm.deleteRealm(Realm.getDefaultConfiguration());
                realm = Realm.getDefaultInstance();
            }
        }
    }
}
