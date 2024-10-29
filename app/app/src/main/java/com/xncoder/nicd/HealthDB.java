package com.xncoder.nicd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class HealthDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "NICD.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "HealthData";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_DRINKER = "drinker";
    private static final String COLUMN_SMOKER = "smoker";
    private static final String COLUMN_DIABETES = "diabetes";
    private static final String COLUMN_HYPERTENSION = "hypertension";

    public HealthDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_AGE + " INTEGER, "
                + COLUMN_GENDER + " TEXT, "
                + COLUMN_WEIGHT + " REAL, "
                + COLUMN_DRINKER + " TEXT, "
                + COLUMN_SMOKER + " TEXT, "
                + COLUMN_DIABETES + " TEXT, "
                + COLUMN_HYPERTENSION + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert a new entry into the database (or update the existing one)
    public void insertData(String name, int age, String gender, double weight, String drinker, String smoker, String diabetes, String hypertension) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_AGE, age);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_DRINKER, drinker);
        values.put(COLUMN_SMOKER, smoker);
        values.put(COLUMN_DIABETES, diabetes);
        values.put(COLUMN_HYPERTENSION, hypertension);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Clear all data in the table
    public void clearData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public ArrayList<String> getData() {
        ArrayList<String> data = new ArrayList<>();
        SQLiteDatabase db = getSqLiteDatabase();

        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            data.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            data.add(cursor.getString(cursor.getColumnIndex(COLUMN_AGE)));
            data.add(cursor.getString(cursor.getColumnIndex(COLUMN_GENDER)));
            data.add(cursor.getString(cursor.getColumnIndex(COLUMN_WEIGHT)));
            data.add(cursor.getString(cursor.getColumnIndex(COLUMN_DRINKER)));
            data.add(cursor.getString(cursor.getColumnIndex(COLUMN_SMOKER)));
            data.add(cursor.getString(cursor.getColumnIndex(COLUMN_DIABETES)));
            data.add(cursor.getString(cursor.getColumnIndex(COLUMN_HYPERTENSION)));
        }
        cursor.close();
        db.close();

        return data;
    }

    private @NonNull SQLiteDatabase getSqLiteDatabase() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_AGE + " INTEGER, "
                + COLUMN_GENDER + " TEXT, "
                + COLUMN_WEIGHT + " REAL, "
                + COLUMN_DRINKER + " INTEGER, "
                + COLUMN_SMOKER + " INTEGER, "
                + COLUMN_DIABETES + " INTEGER, "
                + COLUMN_HYPERTENSION + " INTEGER" + ")");
        return db;
    }
}
