package com.example.m_expenses;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TripListActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private AppDb db;
    private ListView listTrip;
    private FloatingActionButton floatingAddActionButton;

    private ArrayAdapter<Trips> adapter;
    private List<Trips> tripList; // A list to hold all trips for searching

    @Override
    protected void onStart() {
        super.onStart();
        refreshTripList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        setTitle("Trip Database");

        db = new AppDb(this);
        listTrip = findViewById(R.id.listTrip);

        listTrip.setOnItemClickListener(this);
        //listTrip.setOnItemLongClickListener(this);
        findViewById(R.id.floatingAddActionButton).setOnClickListener(this);
    }

    public void refreshTripList() { // VIEW/SELECT
        Trips[] trips = db.getTrips();

        if (trips == null || trips.length == 0) {
            Toast.makeText(this, "No trips available.", Toast.LENGTH_SHORT).show();
            listTrip.setAdapter(null);
            return;
        }

        // Populate tripList and set up adapter
        tripList = new ArrayList<>(Arrays.asList(trips));
        ArrayAdapter<Trips> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, trips);
        listTrip.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) { // ADD
        Intent intent = new Intent(this, AddTripActivity.class);
        intent.putExtra("TRIP_ID", Trips.NEW_TRIP);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) { // EDIT
        Trips trips = (Trips) adapterView.getItemAtPosition(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(trips.getTripName())
                .setItems(new String[]{"View/Edit Trip", "View Expense", "Delete Trip"},
                        ((dialog, which) -> {
                    switch (which){
                        case 0:
                            Intent intent = new Intent(this, AddTripActivity.class);
                            intent.putExtra("TRIP_ID", trips.getTripId());
                            intent.putExtra("TRIP_NAME", trips.getTripName());
                            intent.putExtra("TRIP_DATE_OF_TRIP", trips.getDateOfTrip());
                            intent.putExtra("TRIP_DESTINATION", trips.getTripDestination());
                            intent.putExtra("RISK_ASSESS",trips.getRiskAssessOption());
                            intent.putExtra("TRIP_DESCRIPTION",trips.getTripDescription());
                            intent.putExtra("TRIP_ESTIMATED_SPENDING", trips.getEstimatedSpending());
                            intent.putExtra("TRIP_TYPE", trips.getTripTypeOption());
                            startActivity(intent);
                            break;

                        case 1:
                            Intent viewExpenseIntent = new Intent(this, ExpenseListActivity.class);
                            viewExpenseIntent.putExtra("Trip_ID", trips.getTripId());
                            viewExpenseIntent.putExtra("Trip_Name", trips.getTripName());
                            startActivity(viewExpenseIntent);
                            break;

                        case 2:
                            showDeleteConfirmation(trips);
                            break;
                    }
                }))
        .show();
    }

    private void showDeleteConfirmation(Trips trips){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Confirm Delete");
        dialog.setMessage("Delete Trip: " + trips.getTripName() + "?");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (db.deleteTrips(trips)) {
                    refreshTripList();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to delete trip.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.setNegativeButton("Cancel", null);
        dialog.show();
    }
}