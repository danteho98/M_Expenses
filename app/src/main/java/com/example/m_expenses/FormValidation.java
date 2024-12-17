package com.example.m_expenses;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class FormValidation {
    private Context context;

    public FormValidation(Context context) {
        this.context = context;
    }

    // Validation method
    public boolean ValidationForm(EditText tripName,
                                  long tripDate,
                                  EditText destination,
                                  RadioGroup riskAssessment,
                                  //EditText description,
                                  EditText estimatedSpending,
                                  RadioGroup tripType) {
        // Validate trip name
        if (TextUtils.isEmpty(tripName.getText().toString().trim())) {
            tripName.setError("Trip name is required");
            return false;
        }

        // Validate date selection
//        if (!isValidDate(tripDate)) {
//            Toast.makeText(context, "Please select a valid trip date", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        // Validate destination
        if (TextUtils.isEmpty(destination.getText().toString().trim())) {
            destination.setError("Destination is required");
            return false;
        }

        // Validate risk assessment
        if (riskAssessment.getCheckedRadioButtonId() == -1) {
            Toast.makeText(context, "Please select a risk assessment option", Toast.LENGTH_SHORT).show();
            return false;
        }

        // (Optional) Validate description
    /*
    if (TextUtils.isEmpty(description.getText().toString().trim())) {
        description.setError("Description is required");
        return false;
    }
    */

        // Validate estimated spending
        if (TextUtils.isEmpty(estimatedSpending.getText().toString().trim())) {
            estimatedSpending.setError("Estimated spending is required");
            return false;
        }

        // Validate trip type
        if (tripType.getCheckedRadioButtonId() == -1) {
            Toast.makeText(context, "Please select a trip type", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // All inputs are valid
    }

//    private static boolean isValidDate(long selectedDate) {
//        // Debugging log to check the value of the selected date
//        Log.d("FormValidation", "Selected date: " + selectedDate);
//
//        if (selectedDate == 0) {
//            return false; // Invalid date
//        }
//
//        Calendar today = Calendar.getInstance();
//        long currentTimestamp = today.getTimeInMillis();
//
//        Calendar oneYearFromToday = Calendar.getInstance();
//        oneYearFromToday.add(Calendar.YEAR, 1);
//        long oneYearFromNowTimestamp = oneYearFromToday.getTimeInMillis();
//
//        // Debug log for the range check
//        Log.d("FormValidation", "Current date: " + currentTimestamp);
//        Log.d("FormValidation", "One year from today: " + oneYearFromNowTimestamp);
//        Log.d("FormValidation", "Selected date range check: " + (selectedDate >= currentTimestamp && selectedDate <= oneYearFromNowTimestamp));
//
//        return selectedDate >= currentTimestamp && selectedDate <= oneYearFromNowTimestamp;
//    }

}
