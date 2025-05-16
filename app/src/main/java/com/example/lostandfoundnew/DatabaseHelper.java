package com.example.lostandfoundnew;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "LostFound.db";
    private static final String TABLE_ITEMS = "items";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_ITEMS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "postType TEXT," +
                "name TEXT," +
                "phone TEXT," +
                "description TEXT," +
                "date TEXT," +
                "location TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    // Unified insert method with new fields
    public boolean insertItem(String postType, String name, String phone,
                              String description, String date, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("postType", postType);
        values.put("name", name);
        values.put("phone", phone);
        values.put("description", description);
        values.put("date", date);
        values.put("location", location);
        return db.insert(TABLE_ITEMS, null, values) != -1;
    }

    public ArrayList<Item> getAllItems() {
        ArrayList<Item> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ITEMS, null);

        if (cursor.moveToFirst()) {
            do {
                items.add(new Item(
                        cursor.getInt(0),    // id
                        cursor.getString(1), // postType
                        cursor.getString(2), // name
                        cursor.getString(3), // phone
                        cursor.getString(4), // description
                        cursor.getString(5), // date
                        cursor.getString(6)  // location
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    public boolean deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ITEMS, "id=?", new String[]{String.valueOf(id)}) > 0;
    }
}