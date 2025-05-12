package com.example.personalexpensetracker.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.personalexpensetracker.database.AppDatabase;
import com.example.personalexpensetracker.database.User;
import com.example.personalexpensetracker.database.UserDao;

public class UserRepository {
    private UserDao userDao;

    public UserRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userDao = database.userDao();
    }

    public void insert(User user) {
        new InsertUserAsyncTask(userDao).execute(user);
    }

    public User login(String email, String password) {
        try {
            return new LoginAsyncTask(userDao).execute(email, password).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getUserByEmail(String email) {
        try {
            return new GetUserByEmailAsyncTask(userDao).execute(email).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateDarkMode(int userId, boolean darkMode) {
        new UpdateDarkModeAsyncTask(userDao).execute(userId, darkMode ? 1 : 0);
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
    }

    private static class LoginAsyncTask extends AsyncTask<String, Void, User> {
        private UserDao userDao;

        private LoginAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected User doInBackground(String... strings) {
            return userDao.login(strings[0], strings[1]);
        }
    }

    private static class GetUserByEmailAsyncTask extends AsyncTask<String, Void, User> {
        private UserDao userDao;

        private GetUserByEmailAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected User doInBackground(String... strings) {
            return userDao.getUserByEmail(strings[0]);
        }
    }

    private static class UpdateDarkModeAsyncTask extends AsyncTask<Integer, Void, Void> {
        private UserDao userDao;

        private UpdateDarkModeAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            userDao.updateDarkMode(integers[0], integers[1] == 1);
            return null;
        }
    }
}