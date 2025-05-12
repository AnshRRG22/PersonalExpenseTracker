package com.example.personalexpensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalexpensetracker.R;
import com.example.personalexpensetracker.adapters.ExpenseAdapter;
import com.example.personalexpensetracker.utils.SharedPreferencesHelper;
import com.example.personalexpensetracker.viewmodel.ExpenseViewModel;
import com.example.personalexpensetracker.viewmodel.ExpenseViewModelFactory;

public class ExpensesFragment extends Fragment {
    private RecyclerView recyclerView;
    private ExpenseAdapter adapter;
    private ExpenseViewModel expenseViewModel;
    private SharedPreferencesHelper prefsHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ExpenseAdapter();
        recyclerView.setAdapter(adapter);

        prefsHelper = new SharedPreferencesHelper(requireContext());
        String userId = String.valueOf(prefsHelper.getUserId());

        expenseViewModel = new ViewModelProvider(this, new ExpenseViewModelFactory(requireActivity().getApplication(), userId)).get(ExpenseViewModel.class);


        expenseViewModel.getAllExpenses().observe(getViewLifecycleOwner(), expenses -> {
            if (expenses != null) {
                adapter.setExpenses(expenses);
            }
        });

        return view;
    }
}
