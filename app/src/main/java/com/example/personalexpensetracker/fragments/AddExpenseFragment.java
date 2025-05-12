package com.example.personalexpensetracker.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.personalexpensetracker.R;
import com.example.personalexpensetracker.database.Expense;
import com.example.personalexpensetracker.utils.DateUtils;
import com.example.personalexpensetracker.utils.SharedPreferencesHelper;
import com.example.personalexpensetracker.viewmodel.ExpenseViewModel;
import com.example.personalexpensetracker.viewmodel.ExpenseViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddExpenseFragment extends Fragment implements
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private EditText etTitle, etAmount, etDate, etTime, etLocation;
    private Spinner spinnerCategory;
    private Button btnSave, btnGetLocation;
    private ExpenseViewModel expenseViewModel;
    private SharedPreferencesHelper prefsHelper;
    private FusedLocationProviderClient fusedLocationClient;
    private Calendar selectedDateTime = Calendar.getInstance();
    private double latitude = 0;
    private double longitude = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        etTitle = view.findViewById(R.id.etTitle);
        etAmount = view.findViewById(R.id.etAmount);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        etDate = view.findViewById(R.id.etDate);
        etTime = view.findViewById(R.id.etTime);
        etLocation = view.findViewById(R.id.etLocation);
        btnSave = view.findViewById(R.id.btnSave);
        btnGetLocation = view.findViewById(R.id.btnGetLocation);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.expense_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        etDate.setText(DateUtils.formatDate(new Date()));
        etTime.setText(DateUtils.formatDateTime(new Date()).split(" ")[1]);

        prefsHelper = new SharedPreferencesHelper(requireContext());
        ExpenseViewModelFactory factory = new ExpenseViewModelFactory(
                requireActivity().getApplication(), String.valueOf(prefsHelper.getUserId()));
        expenseViewModel = new ViewModelProvider(this, factory).get(ExpenseViewModel.class);

        etDate.setOnClickListener(v -> showDatePicker());
        etTime.setOnClickListener(v -> showTimePicker());
        btnGetLocation.setOnClickListener(v -> getCurrentLocation());
        btnSave.setOnClickListener(v -> saveExpense());

        return view;
    }

    private void showDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(
                requireContext(),
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show();
    }

    private void showTimePicker() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.show(requireActivity().getSupportFragmentManager(), "TimePickerDialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        selectedDateTime.set(Calendar.MINUTE, minute);
        etTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        selectedDateTime.set(year, month, dayOfMonth);
        etDate.setText(DateUtils.formatDate(selectedDateTime.getTime()));
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
            return;
        }

        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), location -> {
                        if (location != null) {
                            updateLocationUI(location);
                        } else {
                            requestFreshLocation();
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Error getting location: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void updateLocationUI(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (!addresses.isEmpty()) {
                String address = addresses.get(0).getAddressLine(0);
                etLocation.setText(address);
            } else {
                etLocation.setText(String.format(Locale.getDefault(),
                        "Lat: %.4f, Long: %.4f", latitude, longitude));
            }
        } catch (IOException e) {
            etLocation.setText(String.format(Locale.getDefault(),
                    "Lat: %.4f, Long: %.4f", latitude, longitude));
        }
    }

    private void requestFreshLocation() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest,
                new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (locationResult != null) {
                            updateLocationUI(locationResult.getLastLocation());
                            fusedLocationClient.removeLocationUpdates(this);
                        }
                    }
                },
                null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation(); // Try again after permission granted
            } else {
                Toast.makeText(requireContext(),
                        "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveExpense() {
        String title = etTitle.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String location = etLocation.getText().toString().trim();

        if (title.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(requireContext(),
                    "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(),
                    "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        Expense expense = new Expense(
                title,
                amount,
                category,
                selectedDateTime.getTime(),
                location,
                latitude,
                longitude,
                String.valueOf(prefsHelper.getUserId())
        );

        expenseViewModel.insert(expense);
        Toast.makeText(requireContext(), "Expense saved", Toast.LENGTH_SHORT).show();

        etTitle.setText("");
        etAmount.setText("");
        etLocation.setText("");
    }
}
