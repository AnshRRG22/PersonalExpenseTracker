package com.example.personalexpensetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.personalexpensetracker.R;
import com.example.personalexpensetracker.fragments.AddExpenseFragment;
import com.example.personalexpensetracker.fragments.AnalyticsFragment;
import com.example.personalexpensetracker.fragments.DashboardFragment;
import com.example.personalexpensetracker.fragments.ExpensesFragment;
import com.example.personalexpensetracker.fragments.SettingsFragment;
import com.example.personalexpensetracker.utils.SharedPreferencesHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private SharedPreferencesHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefsHelper = new SharedPreferencesHelper(this);

        // Apply dark mode based on saved preference
        if (prefsHelper.isDarkModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_main);

        // Redirect to login if user is not authenticated
        if (prefsHelper.getUserId() == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // Load default fragment (Dashboard)
        loadFragment(new DashboardFragment());
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.nav_dashboard) {
                loadFragment(new DashboardFragment());
            } else if (id == R.id.nav_expenses) {
                loadFragment(new ExpensesFragment());
            } else if (id == R.id.nav_add) {
                loadFragment(new AddExpenseFragment());
            } else if (id == R.id.nav_analytics) {
                loadFragment(new AnalyticsFragment());
            } else if (id == R.id.nav_settings) {
                loadFragment(new SettingsFragment());
            }

    
            return true;
        }
    }