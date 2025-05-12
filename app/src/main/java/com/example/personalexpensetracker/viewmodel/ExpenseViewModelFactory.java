package com.example.personalexpensetracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ExpenseViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private String userId;

    public ExpenseViewModelFactory(Application application, String userId) {
        this.application = application;
        this.userId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ExpenseViewModel.class)) {
            return (T) new ExpenseViewModel(application, userId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
