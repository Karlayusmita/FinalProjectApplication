package com.example.finalprojectapplication.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.finalprojectapplication.R;
import com.example.finalprojectapplication.ui.fragment.FavoriteFragment;
import com.example.finalprojectapplication.ui.fragment.MovieFragment;
import com.example.finalprojectapplication.ui.fragment.TvShowFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import static com.example.finalprojectapplication.Contract.EXTRA_QUERY;
import static com.example.finalprojectapplication.Contract.FRAGMENT;

public class MainActivity extends AppCompatActivity {
    private Fragment fragment;
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigation = menuItem -> {
        switch (menuItem.getItemId()) {
            case R.id.movie_navigation:
                fragment = new MovieFragment();
                buildFragment(fragment);
                return true;
            case R.id.tv_show_navigation:
                fragment = new TvShowFragment();
                buildFragment(fragment);
                return true;
            case R.id.favorite_navigation:
                fragment = new FavoriteFragment();
                buildFragment(fragment);
                return true;
        }
        return false;
    };

    private void buildFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_layout, fragment, FRAGMENT)
                    .commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigation);
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.movie_navigation);
            fragment = new MovieFragment();
        } else {
            fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT);
            buildFragment(fragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.search_menu)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Fragment fragment1 = getSupportFragmentManager().findFragmentByTag(FRAGMENT);
                    if (fragment1 instanceof MovieFragment) {
                        Intent intent = new Intent(MainActivity.this, SearchMovieActivity.class);
                        intent.putExtra(EXTRA_QUERY, query);
                        startActivity(intent);
                        return true;
                    } else if (fragment1 instanceof TvShowFragment) {
                        Intent intent = new Intent(MainActivity.this, SearchTvShowActivity.class);
                        intent.putExtra(EXTRA_QUERY, query);
                        startActivity(intent);
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_language_menu:
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
                break;
            case R.id.reminder_menu:
                Intent intent1 = new Intent(MainActivity.this, ReminderSettingsActivity.class);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
