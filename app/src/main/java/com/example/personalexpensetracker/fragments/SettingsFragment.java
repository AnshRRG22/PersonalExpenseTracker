package com.example.personalexpensetracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.personalexpensetracker.R;
import com.example.personalexpensetracker.activities.LoginActivity;
import com.example.personalexpensetracker.database.User;
import com.example.personalexpensetracker.utils.SharedPreferencesHelper;
import com.example.personalexpensetracker.viewmodel.UserViewModel;

public class SettingsFragment extends Fragment {
    private TextView tvUsername, tvEmail;
    private Switch switchDarkMode;
    private Button btnLogout;
    private SharedPreferencesHelper prefsHelper;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        switchDarkMode = view.findViewById(R.id.switchDarkMode);
        btnLogout = view.findViewById(R.id.btnLogout);

        prefsHelper = new SharedPreferencesHelper(requireContext());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Set current user info
        User user = userViewModel.getUserByEmail(prefsHelper.getUserEmail());
        if (user != null) {
            tvUsername.setText(user.getUsername());
            tvEmail.setText(user.getEmail());
        }

        // Set initial state of switch
        switchDarkMode.setChecked(prefsHelper.isDarkModeEnabled());


        // Listener for dark mode toggle
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefsHelper.setDarkModeEnabled(isChecked);

            if (user != null) {
                userViewModel.updateDarkMode(user.getId(), isChecked);
            }

            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );

            requireActivity().recreate(); // Apply the theme change
        });

        // Logout
        btnLogout.setOnClickListener(v -> {
            prefsHelper.clearUserCredentials();
            startActivity(new Intent(requireActivity(), LoginActivity.class));
            requireActivity().finish();
        });

        return view;
    }
}
