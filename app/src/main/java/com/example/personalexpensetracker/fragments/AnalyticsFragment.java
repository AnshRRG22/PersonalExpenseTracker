package com.example.personalexpensetracker.fragments;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.personalexpensetracker.R;
import com.example.personalexpensetracker.database.CategoryTotal;
import com.example.personalexpensetracker.database.MonthlyTotal;
import com.example.personalexpensetracker.utils.SharedPreferencesHelper;
import com.example.personalexpensetracker.viewmodel.ExpenseViewModel;
import com.example.personalexpensetracker.viewmodel.ExpenseViewModelFactory;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsFragment extends Fragment {
    private PieChart pieChart;
    private BarChart barChart;
    private ExpenseViewModel expenseViewModel;
    private SharedPreferencesHelper prefsHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);

        initializeViews(view);
        setupCharts();
        observeData();

        return view;
    }

    private void initializeViews(View view) {
        pieChart = view.findViewById(R.id.pieChart);
        barChart = view.findViewById(R.id.barChart);
        prefsHelper = new SharedPreferencesHelper(requireContext());
    }

    private void setupCharts() {
        // Configure PieChart appearance
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Expenses by Category");
        pieChart.setCenterTextSize(14f);
        pieChart.setHoleRadius(45f);
        pieChart.setTransparentCircleRadius(50f);

        // Configure BarChart appearance
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        barChart.getAxisRight().setEnabled(false);
    }

    private void observeData() {
        String userId = String.valueOf(prefsHelper.getUserId());
        expenseViewModel = new ViewModelProvider(this,
                new ExpenseViewModelFactory(requireActivity().getApplication(), userId))
                .get(ExpenseViewModel.class);

        // Observe category totals for PieChart
        expenseViewModel.getCategoryTotals().observe(getViewLifecycleOwner(), categoryTotals -> {
            if (categoryTotals != null && !categoryTotals.isEmpty()) {
                setupPieChart(categoryTotals);
            } else {
                pieChart.setNoDataText("No category data available");
                pieChart.invalidate();
            }
        });

        // Observe monthly totals for BarChart
        expenseViewModel.getMonthlyTotals().observe(getViewLifecycleOwner(), monthlyTotals -> {
            if (monthlyTotals != null && !monthlyTotals.isEmpty()) {
                setupBarChart(monthlyTotals);
            } else {
                barChart.setNoDataText("No monthly data available");
                barChart.invalidate();
            }
        });
    }

    private void setupPieChart(List<CategoryTotal> categoryTotals) {
        List<PieEntry> entries = new ArrayList<>();
        for (CategoryTotal ct : categoryTotals) {
            entries.add(new PieEntry((float) ct.getTotal(), ct.getCategory()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        int textColor = isDarkModeEnabled() ? Color.WHITE : Color.BLACK;
        dataSet.setValueTextColor(textColor);
        dataSet.setValueTextSize(12f);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("$%.2f", value);
            }
        });

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.setEntryLabelColor(textColor);
        pieChart.getLegend().setTextColor(textColor);
        pieChart.getDescription().setTextColor(textColor);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }

    private void setupBarChart(List<MonthlyTotal> monthlyTotals) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> monthLabels = new ArrayList<>();

        for (int i = 0; i < monthlyTotals.size(); i++) {
            MonthlyTotal mt = monthlyTotals.get(i);
            entries.add(new BarEntry(i, (float) mt.getTotal()));
            monthLabels.add(mt.getMonth());
        }

        BarDataSet dataSet = new BarDataSet(entries, "Monthly Expenses");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        int textColor = isDarkModeEnabled() ? Color.WHITE : Color.BLACK;
        dataSet.setValueTextColor(textColor);
        dataSet.setValueTextSize(12f);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("$%.2f", value);
            }
        });

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.8f);

        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(monthLabels));
        barChart.getXAxis().setTextColor(textColor);
        barChart.getAxisLeft().setTextColor(textColor);
        barChart.getAxisRight().setTextColor(textColor);
        barChart.getLegend().setTextColor(textColor);
        barChart.getDescription().setTextColor(textColor);
        barChart.setFitBars(true);
        barChart.animateY(1000);
        barChart.invalidate();
    }

    private boolean isDarkModeEnabled() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
}
