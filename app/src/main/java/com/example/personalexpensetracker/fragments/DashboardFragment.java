package com.example.personalexpensetracker.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.personalexpensetracker.R;
import com.example.personalexpensetracker.utils.SharedPreferencesHelper;
import com.example.personalexpensetracker.viewmodel.ExpenseViewModel;
import com.example.personalexpensetracker.viewmodel.ExpenseViewModelFactory;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {
    private TextView tvTotalExpenses;
    private PieChart pieChart;
    private ExpenseViewModel expenseViewModel;
    private SharedPreferencesHelper prefsHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        tvTotalExpenses = view.findViewById(R.id.tvTotalExpenses);
        pieChart = view.findViewById(R.id.pieChart);
        prefsHelper = new SharedPreferencesHelper(requireContext());

        String userId = String.valueOf(prefsHelper.getUserId());
        expenseViewModel = new ViewModelProvider(this, new ExpenseViewModelFactory(requireActivity().getApplication(), userId)).get(ExpenseViewModel.class);

        // Observe total expenses
        expenseViewModel.getTotalExpenses().observe(getViewLifecycleOwner(), total -> {
            if (total != null) {
                tvTotalExpenses.setText(String.format("$%.2f", total));
            }
        });

        // Observe category-wise expenses
        expenseViewModel.getExpensesByCategory().observe(getViewLifecycleOwner(), expensesByCategory -> {
            if (expensesByCategory != null && !expensesByCategory.isEmpty()) {
                setupPieChart(expensesByCategory);
            } else {
                pieChart.setNoDataText("No expenses data available!");
                pieChart.setNoDataTextColor(Color.GRAY);
                pieChart.invalidate();
            }
        });

        return view;
    }

    private void setupPieChart(Map<String, Double> expensesByCategory) {
        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Double> entry : expensesByCategory.entrySet()) {
            entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Expenses by Category");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Expenses");
        pieChart.setCenterTextSize(14f);
        pieChart.animateY(1000);
        pieChart.invalidate(); // Refresh chart
    }
}