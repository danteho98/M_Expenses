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

    private static final String COLUMN_TRIP_ID = "TripId";
    private static final String COLUMN_TRIP_NAME = "TripName";
    private static final String COLUMN_TRIP_DATE_OF_TRIP = "DateOfTrip";
    private static final String COLUMN_TRIP_DESTINATION = "Destination";
    private static final String COLUMN_TRIP_RISK_ASSESS = "RiskAssess";
    private static final String COLUMN_TRIP_DESCRIPTION = "Description";
    private static final String COLUMN_TRIP_ESTIMATED_SPENDING  = "EstimatedSpending";
    private static final String COLUMN_TRIP_TYPE = "TripType";

    private static final String CREATE_TABLE_EXPENSES = "CREATE TABLE IF NOT EXISTS `Expenses` (" +
            "`ExpenseId` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`TripId` INTEGER, `ExpenseName` TEXT, `Date` NUMERIC, `ExpenseType` TEXT, `Amount` REAL, `Comments` TEXT," +
            "FOREIGN KEY(TripId) REFERENCES Trips(TripId))";

    private static final String COLUMN_EXPENSE_ID = "ExpenseId";
    private static final String COLUMN_EXPENSETRIP_ID = "TripId";
    private static final String COLUMN_EXPENSE_NAME = "ExpenseName";
    private static final String COLUMN_EXPENSE_TYPE = "ExpenseType";
    private static final String COLUMN_EXPENSE_AMOUNT = "Amount";
    private static final String COLUMN_EXPENSE_DATE = "Date";
    private static final String COLUMN_EXPENSE_COMMENTS = "Comments";

    private static final String TABLE_TRIP = "Trips";
    private static final String TABLE_EXPENSES = "Expenses";

    private SQLiteDatabase sqLiteDatabase;

    public AppDb(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_TRIP);
        sqLiteDatabase.execSQL(CREATE_TABLE_EXPENSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public boolean addTrips(Trips trips) {
        boolean isAdded = false;
        Log.d("AppDb", "Adding TripId: " + trips.getTripId());
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

        String[] COLUMNS = {COLUMN_TRIP_ID, COLUMN_TRIP_NAME,
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

    public boolean addExpense(Expenses expenses) {
        boolean isAdded = false;
        Log.d("AppDb", "Adding expense with TripId: " + expenses.getTripId());
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EXPENSETRIP_ID, expenses.getTripId());
        cv.put(COLUMN_EXPENSE_NAME, expenses.getExpenseName());
        cv.put(COLUMN_EXPENSE_DATE, expenses.getExpenseDate());
        cv.put(COLUMN_EXPENSE_TYPE, expenses.getExpenseTypeOption());
        cv.put(COLUMN_EXPENSE_AMOUNT, expenses.getAmount());
        cv.put(COLUMN_EXPENSE_COMMENTS, expenses.getComment());


        SQLiteDatabase db = getWritableDatabase();
        isAdded = db.insert(TABLE_EXPENSES, null, cv) != -1;
        db.close();

        return isAdded;
    }

    public boolean deleteExpenses(Expenses expenses) {return deleteExpenses(expenses.getExpenseId());}

    public boolean deleteExpenses(int id) {
        boolean isDeleted = false;

        String whereClause = COLUMN_EXPENSE_ID + " = ?";
        String[] whereArgs = { String.valueOf(id) };

        SQLiteDatabase db = getWritableDatabase();
        isDeleted = db.delete(TABLE_EXPENSES, whereClause, whereArgs) > 0;
        db.close();

        return isDeleted;
    }

    public boolean updateExpenses(Expenses expenses) {
        boolean isUpdated = false;

        // ContentValues for the updated trip data
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EXPENSETRIP_ID, expenses.getTripId());
        cv.put(COLUMN_EXPENSE_NAME, expenses.getExpenseName());
        cv.put(COLUMN_EXPENSE_DATE, expenses.getExpenseDate());
        cv.put(COLUMN_EXPENSE_TYPE, expenses.getExpenseTypeOption());
        cv.put(COLUMN_EXPENSE_AMOUNT, expenses.getAmount());
        cv.put(COLUMN_EXPENSE_COMMENTS, expenses.getComment());

        // Corrected whereClause (use correct column name 'TripId' instead of 'Id')
        String whereClause = COLUMN_EXPENSE_ID + " = ?";
        String[] whereArgs = { String.valueOf(expenses.getExpenseId()) };

        // Get writable database to update the record
        SQLiteDatabase db = getWritableDatabase();
        int rowsUpdated = db.update(TABLE_EXPENSES, cv, whereClause, whereArgs);

        // Check if rows were updated successfully
        if (rowsUpdated > 0) {
            isUpdated = true;
        } else {
            Log.e("AppDb", "Failed to update expense: " + expenses.getExpenseName());
        }

        db.close();
        return isUpdated;
    }

    public Expenses[] getExpenses() {
        Expenses[] expenses = null;  // Default to an empty array instead of null

        String[] COLUMNS = {COLUMN_EXPENSE_ID, COLUMN_EXPENSETRIP_ID,
                COLUMN_EXPENSE_NAME, COLUMN_EXPENSE_DATE,
                COLUMN_EXPENSE_TYPE, COLUMN_EXPENSE_AMOUNT,
                COLUMN_EXPENSE_COMMENTS

        };
        String orderBy = COLUMN_EXPENSE_NAME;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_EXPENSES, COLUMNS,
                null, null,
                null, null,
                orderBy
        );

        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            expenses = new Expenses[cursor.getCount()];  // Use the previously declared trips array
            int index = 0;

            do {
                expenses[index] = new Expenses(
                        cursor.getInt(0), cursor.getInt(1),
                        cursor.getString(2), cursor.getLong(3),
                        cursor.getString(4), cursor.getDouble(5),
                        cursor.getString(6)
                );
                index++;
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return expenses;
    }

    public Expenses getExpenseById(int expenseId){
        Expenses expense = null;

        String[] COLUMNS = {COLUMN_EXPENSE_ID, COLUMN_EXPENSETRIP_ID,
                COLUMN_EXPENSE_NAME, COLUMN_EXPENSE_DATE,
                COLUMN_EXPENSE_TYPE, COLUMN_EXPENSE_AMOUNT,
                COLUMN_EXPENSE_COMMENTS
        };

        String selection = COLUMN_EXPENSE_ID + " = ?";
        String[] selectionArgs = { String.valueOf(expenseId) };
        String orderBy = COLUMN_EXPENSE_NAME;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_EXPENSES, COLUMNS,
                selection, selectionArgs,
                null, null,
                orderBy);

        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            expense = new Expenses(
                    cursor.getInt(0), cursor.getInt(1),
                    cursor.getString(2), cursor.getLong(3),
                    cursor.getString(4), cursor.getDouble(5),
                    cursor.getString(6)
            );
        }
        cursor.close();
        db.close();

        return expense;

    }

    public Expenses[] getExpensesByTripId(int tripId) {
        Expenses[] expenses = null;

        String[] COLUMNS = {COLUMN_EXPENSE_ID, COLUMN_EXPENSETRIP_ID,
                COLUMN_EXPENSE_NAME, COLUMN_EXPENSE_DATE,
                COLUMN_EXPENSE_TYPE, COLUMN_EXPENSE_AMOUNT,
                COLUMN_EXPENSE_COMMENTS};

        String selection = COLUMN_EXPENSETRIP_ID + " = ?";
        String[] selectionArgs = {String.valueOf(tripId)};
        String orderBy = COLUMN_EXPENSE_NAME;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_EXPENSES, COLUMNS,
                selection, selectionArgs,
                null, null,
                orderBy);

        if (cursor.getCount() > 0) {
            expenses = new Expenses[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                expenses[i++] = new Expenses(
                        cursor.getInt(0), // Expense ID
                        cursor.getInt(1), // Trip ID
                        cursor.getString(2), // Expense Name
                        cursor.getLong(3), // Expense Date
                        cursor.getString(4), // Expense Type
                        cursor.getDouble(5), // Expense Amount
                        cursor.getString(6) // Expense Comments
                );
            }
        }
        cursor.close();
        db.close();

        return expenses;
    }

}
