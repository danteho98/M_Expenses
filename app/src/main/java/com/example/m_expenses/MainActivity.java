package com.example.m_expenses;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button add_trip_button, backToTripListButton, pickDateButton, addExpenseButton;
    private EditText trip_name_text, tripDateEditText, destination_text, description_text, estimated_spending_text;
    private RadioButton yes_radioButton, no_radioButton, casual_radioButton, business_radioButton, others_radioButton;
    private RadioGroup rs_radioGroup, tt_radioGroup;
    private FormValidation formValidation;
    private AppDb appDb = null;
    private long selectedDate; // Store the selected date as long
    private int tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("M-Expense");

        // Get trip ID from intent
        tripId = getIntent().getIntExtra("TRIP_ID", Trips.NEW_TRIP);

        // Initialize UI components
        trip_name_text = findViewById(R.id.trip_name_text);
        tripDateEditText = findViewById(R.id.tripDateEditText);
        destination_text = findViewById(R.id.destination_text);
        description_text = findViewById(R.id.description_text);
        rs_radioGroup = findViewById(R.id.rs_radioGroup);
        yes_radioButton = findViewById(R.id.rs_yes_button);
        no_radioButton = findViewById(R.id.rs_no_button);
        estimated_spending_text = findViewById(R.id.estimated_spending_text);
        addExpenseButton = findViewById(R.id.addExpenseButton);
        tt_radioGroup = findViewById(R.id.tt_radioGroup);
        business_radioButton = findViewById(R.id.business_radioButton);
        casual_radioButton = findViewById(R.id.casual_radioButton);
        others_radioButton = findViewById(R.id.others_radioButton);
        add_trip_button = findViewById(R.id.add_trip_button);
        backToTripListButton = findViewById(R.id.backToTripListButton);
        pickDateButton = findViewById(R.id.pickDateButton);

        // Set listeners
        findViewById(R.id.backToTripListButton).setOnClickListener(this);
        findViewById(R.id.add_trip_button).setOnClickListener(this);
        addExpenseButton.setOnClickListener(this);
        pickDateButton.setOnClickListener(this); // Set listener for date picker button

        appDb = new AppDb(this);

        // Initialize FormValidation
        formValidation = new FormValidation(MainActivity.this);

        // Load existing trip details if tripId is not NEW_TRIP
        if (tripId != Trips.NEW_TRIP) {
            Trips existingTrip = appDb.getTripById(tripId);
            if (existingTrip != null) {
                trip_name_text.setText(existingTrip.getTripName());

                // Retrieve and set trip date
                long tripDate = existingTrip.getDateOfTrip();
                if (tripDate != 0) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(tripDate);
                    String formattedDate = String.format("%04d-%02d-%02d",
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH) + 1,
                            calendar.get(Calendar.DAY_OF_MONTH));
                    tripDateEditText.setText(formattedDate);
                }

                destination_text.setText(existingTrip.getDestination());
                description_text.setText(existingTrip.getDescription());
                estimated_spending_text.setText(String.valueOf(existingTrip.getEstimatedSpending()));

                // Pre-select the corresponding radio buttons
                for (int i = 0; i < rs_radioGroup.getChildCount(); i++) {
                    RadioButton button = (RadioButton) rs_radioGroup.getChildAt(i);
                    if (button.getText().toString().equals(existingTrip.getRiskAssessOption())) {
                        button.setChecked(true);
                        break;
                    }
                }

                for (int i = 0; i < tt_radioGroup.getChildCount(); i++) {
                    RadioButton button = (RadioButton) tt_radioGroup.getChildAt(i);
                    if (button.getText().toString().equals(existingTrip.getTripTypeOption())) {
                        button.setChecked(true);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_trip_button) {
            // Collect trip input details
            String tripName = trip_name_text.getText().toString().trim();
            if (selectedDate == 0) {
                Toast.makeText(this, "Please select a valid trip date", Toast.LENGTH_SHORT).show();
                return; // Skip if no date is selected
            }
            String destination = destination_text.getText().toString().trim();
            int selectedRiskId = rs_radioGroup.getCheckedRadioButtonId();
            int selectedTypeId = tt_radioGroup.getCheckedRadioButtonId();
            int estimatedSpending;

            // Use FormValidation for inputs
            if (!formValidation.ValidationForm(
                    trip_name_text,
                    selectedDate,
                    destination_text,
                    rs_radioGroup,
                    // description_text,
                    estimated_spending_text,
                    tt_radioGroup
            )) {
                return; // Validation failed
            }

            try {
                estimatedSpending = Integer.parseInt(estimated_spending_text.getText().toString().trim());
            } catch (NumberFormatException e) {
                estimated_spending_text.setError("Estimated spending must be a valid number");
                return;
            }

            // Show AlertDialog for confirmation
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Confirm Trip")
                    .setMessage("Are you sure you want to save this trip?")
                    .setPositiveButton("OK", (dialog, which) -> {
                        // Proceed to add or update the trip in the database
                        if (tripId != Trips.NEW_TRIP) {
                            updateTripInDatabase(); // Update existing trip
                        } else {
                            addTripToDatabase(); // Add new trip
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        // Just dismiss the dialog and keep input data intact
                        dialog.dismiss();
                    })
                    .show();
        }
        else if (view.getId() == R.id.backToTripListButton) {
            // Navigate back to TripListActivity
            Intent intent = new Intent(MainActivity.this, TripListActivity.class);
            startActivity(intent); // Start TripListActivity
            finish(); // Close the current activity
        }
        else if (view.getId() == R.id.pickDateButton) {
            // Show DatePickerDialog
            Calendar calendar = Calendar.getInstance();
            if (selectedDate != 0) {
                calendar.setTimeInMillis(selectedDate);
            }
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(MainActivity.this, (datePickerView, selectedYear, selectedMonth, selectedDay) -> {
                calendar.set(selectedYear, selectedMonth, selectedDay);
                selectedDate = calendar.getTimeInMillis();

                // Format and update date in the EditText
                String formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                tripDateEditText.setText(formattedDate);

                Log.d("DatePicker", "Selected Date: " + selectedDate);
                Toast.makeText(MainActivity.this, "Date selected: " + formattedDate, Toast.LENGTH_SHORT).show();
            }, currentYear, currentMonth, currentDay).show();
        }
        else if (view.getId() == R.id.addExpenseButton) {
            // Navigate to ExpenseListActivity and pass the tripId
            Intent intent = new Intent(MainActivity.this, ExpenseListActivity.class);
            intent.putExtra("TRIP_ID", tripId); // Pass the current trip ID
            startActivity(intent);
        }
    }

    private void addTripToDatabase() {
        // Collect all the input details after the confirmation
        String tripName = trip_name_text.getText().toString().trim();
        String destination = destination_text.getText().toString().trim();
        int selectedRiskId = rs_radioGroup.getCheckedRadioButtonId();
        int selectedTypeId = tt_radioGroup.getCheckedRadioButtonId();
        int estimatedSpending;

        try {
            estimatedSpending = Integer.parseInt(estimated_spending_text.getText().toString().trim());
        } catch (NumberFormatException e) {
            estimated_spending_text.setError("Estimated spending must be a valid number");
            return;
        }

        // Get the selected radio buttons
        RadioButton selectedRiskButton = findViewById(selectedRiskId);
        RadioButton selectedTypeButton = findViewById(selectedTypeId);

        String riskAssessment = selectedRiskButton.getText().toString();
        String tripType = selectedTypeButton.getText().toString();

        // Persist the trip details into the database
        Trips trip = new Trips(Trips.NEW_TRIP, tripName, selectedDate, destination,
                riskAssessment, description_text.getText().toString().trim(),
                estimatedSpending, tripType);

        // Save to the database and show appropriate message
        if (appDb.addTrips(trip)) {
            Toast.makeText(this, "Trip saved successfully.", Toast.LENGTH_SHORT).show();
            finish(); // Close the current activity and return to TripListActivity
        } else {
            Toast.makeText(this, "Failed to save trip.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTripInDatabase() {
        // Collect updated trip details
        String tripName = trip_name_text.getText().toString().trim();
        String destination = destination_text.getText().toString().trim();
        int selectedRiskId = rs_radioGroup.getCheckedRadioButtonId();
        int selectedTypeId = tt_radioGroup.getCheckedRadioButtonId();
        int estimatedSpending;

        try {
            estimatedSpending = Integer.parseInt(estimated_spending_text.getText().toString().trim());
        } catch (NumberFormatException e) {
            estimated_spending_text.setError("Estimated spending must be a valid number");
            return;
        }

        // Get selected radio buttons
        RadioButton selectedRiskButton = findViewById(selectedRiskId);
        RadioButton selectedTypeButton = findViewById(selectedTypeId);

        String riskAssessment = selectedRiskButton.getText().toString();
        String tripType = selectedTypeButton.getText().toString();

        // Update the trip in the database
        Trips updatedTrip = new Trips(tripId, tripName, selectedDate, destination,
                riskAssessment, description_text.getText().toString().trim(),
                estimatedSpending, tripType);

        // Persist the updated trip details
        if (appDb.updateTrips(updatedTrip)) {
            Toast.makeText(this, "Trip updated successfully.", Toast.LENGTH_SHORT).show();
            finish(); // Close the current activity and return to TripListActivity
        } else {
            Toast.makeText(this, "Failed to update trip.", Toast.LENGTH_SHORT).show();
        }
    }
}
