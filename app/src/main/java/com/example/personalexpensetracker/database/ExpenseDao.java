package com.example.personalexpensetracker.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Insert
    void insert(Expense expense);

    @Update
    void update(Expense expense);

    @Delete
    void delete(Expense expense);

    @Query("DELETE FROM expenses WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT * FROM expenses WHERE userId = :userId ORDER BY date DESC")
    LiveData<List<Expense>> getAllExpenses(String userId);

    @Query("SELECT * FROM expenses WHERE userId = :userId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    LiveData<List<Expense>> getExpensesByDate(String userId, long startDate, long endDate);

    @Query("SELECT * FROM expenses WHERE userId = :userId AND category = :category ORDER BY date DESC")
    LiveData<List<Expense>> getExpensesByCategory(String userId, String category);

    @Query("SELECT SUM(amount) FROM expenses WHERE userId = :userId")
    LiveData<Double> getTotalExpenses(String userId);

    @Query("SELECT category, SUM(amount) as total FROM expenses WHERE userId = :userId GROUP BY category")
    LiveData<List<CategoryTotal>> getCategoryTotals(String userId);

    @Query("SELECT * FROM expenses WHERE userId = :userId AND location LIKE '%' || :location || '%'")
    LiveData<List<Expense>> getExpensesByLocation(String userId, String location);

    @Query("SELECT strftime('%Y-%m', date/1000, 'unixepoch') as month, " +
            "SUM(amount) as total FROM expenses " +
            "WHERE userId = :userId GROUP BY month ORDER BY month")
    LiveData<List<MonthlyTotal>> getMonthlyTotals(String userId);
}

