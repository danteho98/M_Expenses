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

    // Validation method for expenses
    public boolean validateExpenseForm(EditText expenseName,
                                       RadioGroup expenseType,
                                       EditText costAmount,
                                       long selectedDate) {
        // Validate expense name
        if (TextUtils.isEmpty(expenseName.getText().toString().trim())) {
            expenseName.setError("Expense name is required");
            return false;
        }

        // Validate expense type
        if (expenseType.getCheckedRadioButtonId() == -1) {
            Toast.makeText(context, "Please select an expense type", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate cost amount
        if (TextUtils.isEmpty(costAmount.getText().toString().trim())) {
            costAmount.setError("Cost amount is required");
            return false;
        }

        return true; // All inputs are valid
    }
}
