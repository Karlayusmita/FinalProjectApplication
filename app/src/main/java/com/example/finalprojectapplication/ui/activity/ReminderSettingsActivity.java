package com.example.finalprojectapplication.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import com.example.finalprojectapplication.R;
import com.example.finalprojectapplication.data.model.Movie;
import com.example.finalprojectapplication.data.network.Network;
import com.example.finalprojectapplication.ui.alarm.DailyReminder;
import com.example.finalprojectapplication.ui.alarm.ReleaseReminder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;

import static com.example.finalprojectapplication.Contract.KEY_DAILY;
import static com.example.finalprojectapplication.Contract.KEY_RELEASE;
import static com.example.finalprojectapplication.Contract.NOTIF_ID;

//import androidx.appcompat.app.ActionBar;
//import android.preference.Preference;
//import android.preference.PreferenceManager;

public class ReminderSettingsActivity extends AppCompatPreferencesActivity {
    private static DailyReminder dailyReminder;
    private static ReleaseReminder releaseReminder;
    private static Context context;
    private static List<Movie> movies;
    private static Preference.OnPreferenceChangeListener bindPreferencesSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            boolean value = (boolean) newValue;

            String key = preference.getKey();
            String dailyKey = KEY_DAILY;
            String releaseKey = KEY_RELEASE;
            if (key.equals(dailyKey)) {
                if (value) {
                    dailyReminder.setRepeatingAlarm(getAppContext());
                } else {
                    dailyReminder.cancelAlarm(getAppContext());
                }
            }

            if (key.equals(releaseKey)) {
                if (value) {
                    releaseReminder.setRepeatingAlarm(getAppContext());
                } else {
                    releaseReminder.cancelAlarm(getAppContext());
                }
            }
            return true;
        }
    };

    private static boolean isXLargeTable(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    private static void bindPreferencesSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(bindPreferencesSummaryToValueListener);

        bindPreferencesSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                        .getBoolean(preference.getKey(), false));
    }

    public static Context getAppContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpActionBar();

        dailyReminder = new DailyReminder();
        releaseReminder = new ReleaseReminder();
        context = getApplicationContext();
        movies = new ArrayList<>();

        this.getFragmentManager().beginTransaction().replace(android.R.id.content, new ReminderPreferenceFragment()).commit();

    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return super.onNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onIsMultiPane() {
        return isXLargeTable(this);
    }

    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || ReminderPreferenceFragment.class.getName().equals(fragmentName);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ReminderPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_reminder);

            Preference preference = findPreference(getString(R.string.daily_reminder));
            bindPreferencesSummaryToValue(findPreference(getString(R.string.key_daily)));
            bindPreferencesSummaryToValue(findPreference(getString(R.string.key_release)));

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), ReminderSettingsActivity.class));
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }

    public static class MovieAsyncTask extends AsyncTask<URL, Void, String> {
        String currentDate;
        Intent intent;

        public MovieAsyncTask(String currentDate, Intent intent) {
            this.currentDate = currentDate;
            this.intent = intent;
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String result = null;
            try {
                result = Network.getFromNetwork(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("data up", s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Movie movie = new Movie(object);
                    if (movie.getReleaseDate().equals(currentDate)) {
                        movies.add(movie);
                    }
                }
                Log.e("test", movies.get(movies.size() - 1).getTitle());
                int i = 0;
                for (Movie movie : movies) {
                    int notifId = intent.getIntExtra(NOTIF_ID, 0);
                    String title = context.getString(R.string.release_reminder);
                    String movieName = movie.getTitle();
                    ReleaseReminder.showNotification(context, title, notifId + i, movieName);
                    i++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
