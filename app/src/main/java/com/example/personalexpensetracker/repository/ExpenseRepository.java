package com.example.personalexpensetracker.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.personalexpensetracker.database.AppDatabase;
import com.example.personalexpensetracker.database.Expense;
import com.example.personalexpensetracker.database.ExpenseDao;
import com.example.personalexpensetracker.database.CategoryTotal;
import com.example.personalexpensetracker.database.MonthlyTotal;

import java.util.List;

public class ExpenseRepository {
    private ExpenseDao expenseDao;
    private LiveData<List<Expense>> allExpenses;

    public ExpenseRepository(Application application, String userId) {
        AppDatabase database = AppDatabase.getInstance(application);
        expenseDao = database.expenseDao();
        allExpenses = expenseDao.getAllExpenses(userId);
    }

    public void insert(Expense expense) {
        new InsertExpenseAsyncTask(expenseDao).execute(expense);
    }

    public void update(Expense expense) {
        new UpdateExpenseAsyncTask(expenseDao).execute(expense);
    }

    public void delete(Expense expense) {
        new DeleteExpenseAsyncTask(expenseDao).execute(expense);
    }

    public void deleteById(int id) {
        new DeleteExpenseByIdAsyncTask(expenseDao).execute(id);
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<List<Expense>> getExpensesByDate(String userId, long startDate, long endDate) {
        return expenseDao.getExpensesByDate(userId, startDate, endDate);
    }

    public LiveData<List<Expense>> getExpensesByCategory(String userId, String category) {
        return expenseDao.getExpensesByCategory(userId, category);
    }

    public LiveData<Double> getTotalExpenses(String userId) {
        return expenseDao.getTotalExpenses(userId);
    }

    public LiveData<List<CategoryTotal>> getCategoryTotals(String userId) {
        return expenseDao.getCategoryTotals(userId);
    }

    public LiveData<List<MonthlyTotal>> getMonthlyTotals(String userId) {
        return expenseDao.getMonthlyTotals(userId);
    }

    public LiveData<List<Expense>> getExpensesByLocation(String userId, String location) {
        return expenseDao.getExpensesByLocation(userId, location);
    }

    private static class InsertExpenseAsyncTask extends AsyncTask<Expense, Void, Void> {
        private ExpenseDao expenseDao;

        private InsertExpenseAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Expense... expenses) {
            expenseDao.insert(expenses[0]);
            return null;
        }
    }

    private static class UpdateExpenseAsyncTask extends AsyncTask<Expense, Void, Void> {
        private ExpenseDao expenseDao;

        private UpdateExpenseAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Expense... expenses) {
            expenseDao.update(expenses[0]);
            return null;
        }
    }

    private static class DeleteExpenseAsyncTask extends AsyncTask<Expense, Void, Void> {
        private ExpenseDao expenseDao;

        private DeleteExpenseAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Expense... expenses) {
            expenseDao.delete(expenses[0]);
            return null;
        }
    }

    private static class DeleteExpenseByIdAsyncTask extends AsyncTask<Integer, Void, Void> {
        private ExpenseDao expenseDao;

        private DeleteExpenseByIdAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            expenseDao.deleteById(integers[0]);
            return null;
        }
    }
}