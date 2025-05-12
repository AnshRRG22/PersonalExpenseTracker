package com.example.personalexpensetracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.personalexpensetracker.database.CategoryTotal;
import com.example.personalexpensetracker.database.Expense;
import com.example.personalexpensetracker.database.MonthlyTotal;
import com.example.personalexpensetracker.repository.ExpenseRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseViewModel extends AndroidViewModel {
    private ExpenseRepository repository;
    private LiveData<List<Expense>> allExpenses;
    private LiveData<Double> totalExpenses;
    private LiveData<List<CategoryTotal>> categoryTotals;
    private LiveData<Map<String, Double>> expensesByCategoryMap;
    private LiveData<List<MonthlyTotal>> monthlyTotals;
    private String userId;

    public ExpenseViewModel(Application application, String userId) {
        super(application);
        this.userId = userId;
        repository = new ExpenseRepository(application, userId);
        initializeLiveData();
    }

    private void initializeLiveData() {
        allExpenses = repository.getAllExpenses();
        totalExpenses = repository.getTotalExpenses(userId);
        categoryTotals = repository.getCategoryTotals(userId);
        monthlyTotals = repository.getMonthlyTotals(userId);

        // Convert List<CategoryTotal> to Map<String, Double> for PieChart
        expensesByCategoryMap = Transformations.map(categoryTotals, categoryTotalsList -> {
            Map<String, Double> map = new HashMap<>();
            if (categoryTotalsList != null) {
                for (CategoryTotal categoryTotal : categoryTotalsList) {
                    map.put(categoryTotal.getCategory(), categoryTotal.getTotal());
                }
            }
            return map;
        });
    }

    // CRUD Operations
    public void insert(Expense expense) {
        repository.insert(expense);
    }

    public void update(Expense expense) {
        repository.update(expense);
    }

    public void delete(Expense expense) {
        repository.delete(expense);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

    // Data Access Methods
    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<Double> getTotalExpenses() {
        return totalExpenses;
    }

    public LiveData<Map<String, Double>> getExpensesByCategory() {
        return expensesByCategoryMap;
    }

    public LiveData<List<CategoryTotal>> getCategoryTotals() {
        return categoryTotals;
    }

    public LiveData<List<MonthlyTotal>> getMonthlyTotals() {
        return monthlyTotals;
    }

    // Filter Methods
    public LiveData<List<Expense>> getExpensesByDate(long startDate, long endDate) {
        return repository.getExpensesByDate(userId, startDate, endDate);
    }

    public LiveData<List<Expense>> getExpensesByCategory(String category) {
        return repository.getExpensesByCategory(userId, category);
    }

    public LiveData<List<Expense>> getExpensesByLocation(String location) {
        return repository.getExpensesByLocation(userId, location);
    }
}