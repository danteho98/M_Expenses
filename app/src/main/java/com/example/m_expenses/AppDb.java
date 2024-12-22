package com.example.m_expenses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class AppDb extends SQLiteOpenHelper {

    private static final String DB_NAME = "app.db";
    private static final int DB_VERSION = 1;

    private static final String CREATE_TABLE_TRIP = "CREATE TABLE IF NOT EXISTS `Trips` (`TripId` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`TripName` TEXT, `DateOfTrip` NUMERIC, " +
            "`Destination` TEXT, `RiskAssess` TEXT," +
            "`Description` TEXT, `EstimatedSpending` INTEGER, `TripType` TEXT)";
    private static final String TABLE_TRIP = "Trips";
    private static final String COLUMN_TRIP_ID = "TripId";
    private static final String COLUMN_TRIP_NAME = "TripName";
    private static final String COLUMN_TRIP_DATE_OF_TRIP = "DateOfTrip";
    private static final String COLUMN_TRIP_DESTINATION = "Destination";
    private static final String COLUMN_TRIP_RISK_ASSESS = "RiskAssess";
    private static final String COLUMN_TRIP_DESCRIPTION = "Description";
    private static final String COLUMN_TRIP_ESTIMATED_SPENDING  = "EstimatedSpending";
    private static final String COLUMN_TRIP_TYPE = "TripType";
    private SQLiteDatabase sqLiteDatabase;

    public AppDb(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_TRIP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public boolean addTrips(Trips trips) {
        boolean isAdded = false;

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TRIP_NAME, trips.getTripName());
        cv.put(COLUMN_TRIP_DATE_OF_TRIP, trips.getDateOfTrip());
        cv.put(COLUMN_TRIP_DESTINATION, trips.getDestination());
        cv.put(COLUMN_TRIP_RISK_ASSESS, trips.getRiskAssessOption());
        cv.put(COLUMN_TRIP_DESCRIPTION, trips.getDescription());
        cv.put(COLUMN_TRIP_ESTIMATED_SPENDING, trips.getEstimatedSpending());
        cv.put(COLUMN_TRIP_TYPE, trips.getTripTypeOption());

        SQLiteDatabase db = getWritableDatabase();
        isAdded = db.insert(TABLE_TRIP, null, cv) != -1;
        db.close();

        return isAdded;
    }

    public boolean deleteTrips(Trips trips) {
        return deleteTrips(trips.getTripId());
    }

    public boolean deleteTrips(int id) {
        boolean isDeleted = false;

        String whereClause = COLUMN_TRIP_ID + " = ?";
        String[] whereArgs = { String.valueOf(id) };

        SQLiteDatabase db = getWritableDatabase();
        isDeleted = db.delete(TABLE_TRIP, whereClause, whereArgs) > 0;
        db.close();

        return isDeleted;
    }

    public Trips[] getTrips() {
        Trips[] trips = null;  // Default to an empty array instead of null

        String[] COLUMNS = {
                COLUMN_TRIP_ID, COLUMN_TRIP_NAME,
                COLUMN_TRIP_DATE_OF_TRIP, COLUMN_TRIP_DESTINATION,
                COLUMN_TRIP_RISK_ASSESS, COLUMN_TRIP_DESCRIPTION,
                COLUMN_TRIP_ESTIMATED_SPENDING, COLUMN_TRIP_TYPE
        };
        String orderBy = COLUMN_TRIP_NAME;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_TRIP, COLUMNS,
                null, null,
                null, null,
                orderBy
        );

        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            trips = new Trips[cursor.getCount()];  // Use the previously declared trips array
            int index = 0;

            do {
                trips[index] = new Trips(
                        cursor.getInt(0), cursor.getString(1),
                        cursor.getLong(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5),
                        cursor.getInt(6), cursor.getString(7)

                );
                index++;
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return trips;
    }

    // New method to retrieve a specific trip by its tripId
    public Trips getTripById(int tripId) {
        Trips trip = null;

        String[] COLUMNS = {
                COLUMN_TRIP_ID, COLUMN_TRIP_NAME,
                COLUMN_TRIP_DATE_OF_TRIP, COLUMN_TRIP_DESTINATION,
                COLUMN_TRIP_RISK_ASSESS, COLUMN_TRIP_DESCRIPTION,
                COLUMN_TRIP_ESTIMATED_SPENDING, COLUMN_TRIP_TYPE
        };

        String selection = COLUMN_TRIP_ID + " = ?";
        String[] selectionArgs = { String.valueOf(tripId) };

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_TRIP, COLUMNS, selection, selectionArgs, null, null, null);

        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            trip = new Trips(
                    cursor.getInt(0), // TripId
                    cursor.getString(1), // TripName
                    cursor.getLong(2), // DateOfTrip
                    cursor.getString(3), // Destination
                    cursor.getString(4), // RiskAssess
                    cursor.getString(5), // Description
                    cursor.getInt(6), // EstimatedSpending
                    cursor.getString(7) // TripType
            );
        }
        cursor.close();
        db.close();

        return trip;
    }

    public boolean updateTrips(Trips trips) {
        boolean isUpdated = false;

        // ContentValues for the updated trip data
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TRIP_NAME, trips.getTripName());
        cv.put(COLUMN_TRIP_DATE_OF_TRIP, trips.getDateOfTrip());
        cv.put(COLUMN_TRIP_DESTINATION, trips.getDestination());
        cv.put(COLUMN_TRIP_RISK_ASSESS, trips.getRiskAssessOption());
        cv.put(COLUMN_TRIP_DESCRIPTION, trips.getDescription());
        cv.put(COLUMN_TRIP_ESTIMATED_SPENDING, trips.getEstimatedSpending());
        cv.put(COLUMN_TRIP_TYPE, trips.getTripTypeOption());

        // Corrected whereClause (use correct column name 'TripId' instead of 'Id')
        String whereClause = COLUMN_TRIP_ID + " = ?";
        String[] whereArgs = { String.valueOf(trips.getTripId()) };

        // Get writable database to update the record
        SQLiteDatabase db = getWritableDatabase();
        int rowsUpdated = db.update(TABLE_TRIP, cv, whereClause, whereArgs);

        // Check if rows were updated successfully
        if (rowsUpdated > 0) {
            isUpdated = true;
        } else {
            Log.e("AppDb", "Failed to update trip: " + trips.getTripName());
        }

        db.close();
        return isUpdated;
    }

    public boolean addExpense(Expenses expenses) {
        // Example of adding an expense to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tripId", expenses.getTripId());
        values.put("description", expenses.getDescription());
        values.put("amount", expenses.getAmount());
        values.put("date", expenses.getDate());

        long result = db.insert("Expenses", null, values);
        return result != -1;  // Returns true if insert was successful
    }

}
