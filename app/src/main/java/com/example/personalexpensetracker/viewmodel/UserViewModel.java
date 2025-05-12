package com.example.personalexpensetracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.personalexpensetracker.database.User;
import com.example.personalexpensetracker.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {
    private UserRepository repository;
    private MutableLiveData<User> currentUser = new MutableLiveData<>();

    public UserViewModel(Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public void insert(User user) {
        repository.insert(user);
    }

    public User login(String email, String password) {
        User user = repository.login(email, password);
        currentUser.postValue(user);
        return user;
    }

    public User getUserByEmail(String email) {
        return repository.getUserByEmail(email);
    }

    public void updateDarkMode(int userId, boolean darkMode) {
        repository.updateDarkMode(userId, darkMode);
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        currentUser.postValue(user);
    }
}