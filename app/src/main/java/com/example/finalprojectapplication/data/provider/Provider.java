package com.example.finalprojectapplication.data.provider;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;

import com.example.finalprojectapplication.data.model.MovieFavorite;
import com.example.finalprojectapplication.data.service.CleanupJobService;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmResults;
import io.realm.RealmSchema;
import io.realm.exceptions.RealmMigrationNeededException;

import static com.example.finalprojectapplication.Contract.AUTHORITY;
import static com.example.finalprojectapplication.Contract.CLEANUP_ID;
import static com.example.finalprojectapplication.Contract.ID;
import static com.example.finalprojectapplication.Contract.ORIGINAl_LANGUAGE;
import static com.example.finalprojectapplication.Contract.OVERVIEW;
import static com.example.finalprojectapplication.Contract.POSTER;
import static com.example.finalprojectapplication.Contract.RELEASE_DATE;
import static com.example.finalprojectapplication.Contract.TASKS;
import static com.example.finalprojectapplication.Contract.TEST;
import static com.example.finalprojectapplication.Contract.TEST_ID;
import static com.example.finalprojectapplication.Contract.TITLE;
import static com.example.finalprojectapplication.Contract.VOTE_AVERAGE;
import static com.example.finalprojectapplication.Contract.VOTE_COUNT;

public class Provider extends ContentProvider {
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, TASKS, TEST);
        uriMatcher.addURI(AUTHORITY, TASKS + "/#", TEST_ID);
    }

    private Realm realm;

    @Override
    public boolean onCreate() {
        Realm.init(Objects.requireNonNull(getContext()));
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .migration(new migration())
                .build();
        Realm.setDefaultConfiguration(configuration);
        manageCleanupJob();
        return true;
    }

    private void manageCleanupJob() {
        Log.d("manageCleanupJob", "Scheduling cleanup job");
        JobScheduler jobScheduler = (JobScheduler) Objects.requireNonNull(getContext())
                .getSystemService(Context.JOB_SCHEDULER_SERVICE);
        long jobInterval = DateUtils.HOUR_IN_MILLIS;

        ComponentName jobService = new ComponentName(getContext(), CleanupJobService.class);
        JobInfo task = new JobInfo.Builder(CLEANUP_ID, jobService)
                .setPeriodic(jobInterval)
                .setPersisted(true)
                .build();

        if (jobScheduler != null) {
            if (jobScheduler.schedule(task) != JobScheduler.RESULT_SUCCESS) {
                Log.w("unable", "Unable to schedule cleanup job");
            }
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        int matcher = uriMatcher.match(uri);
        try {
            realm = Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException e) {
            if (Realm.getDefaultConfiguration() != null) {
                Realm.deleteRealm(Realm.getDefaultConfiguration());
                realm = Realm.getDefaultInstance();
            }
        }

        MatrixCursor cursor = new MatrixCursor(new String[]{ID, POSTER, TITLE, ORIGINAl_LANGUAGE, RELEASE_DATE, OVERVIEW, VOTE_AVERAGE, VOTE_COUNT});
        try {
            switch (matcher) {
                case TEST:
                    RealmResults<MovieFavorite> realmResults = realm.where(MovieFavorite.class).findAll();
                    for (MovieFavorite movieFavorite : realmResults) {
                        Object[] rowData = new Object[]{movieFavorite.getId(), movieFavorite.getPoster(), movieFavorite.getTitle(),
                                movieFavorite.getReleaseDate(), movieFavorite.getOriginalLanguage(), movieFavorite.getOverview(), movieFavorite.getVoteAverage(), movieFavorite.getVoteCount()};
                        cursor.addRow(rowData);
                        Log.v("RealmDB", movieFavorite.toString());
                    }
                    break;
                case TEST_ID:
                    Integer id = Integer.parseInt(uri.getPathSegments().get(1));
                    MovieFavorite movieFavorite = realm.where(MovieFavorite.class).equalTo("task_id", id).findFirst();
                    if (movieFavorite != null) {
                        cursor.addRow(new Object[]{movieFavorite.getId(), movieFavorite.getPoster(), movieFavorite.getTitle(),
                                movieFavorite.getReleaseDate(), movieFavorite.getOriginalLanguage(), movieFavorite.getOverview(), movieFavorite.getVoteAverage(), movieFavorite.getVoteCount()});
                    }
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

            cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        } finally {
            realm.close();
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    class migration implements RealmMigration {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            RealmSchema schema = realm.getSchema();
            if (oldVersion != 0) {
                schema.create(TASKS)
                        .addField(ID, Integer.class)
                        .addField(POSTER, String.class)
                        .addField(TITLE, String.class)
                        .addField(ORIGINAl_LANGUAGE, String.class)
                        .addField(RELEASE_DATE, String.class)
                        .addField(OVERVIEW, String.class)
                        .addField(VOTE_AVERAGE, String.class)
                        .addField(VOTE_COUNT, String.class);
                oldVersion++;
            }
        }
    }
}
