package com.parkaid.app.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhisheksisodia on 15-08-27.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // table names
    private static final String TABLE_CONTACTS = "contacts";
    private static final String TABLE_GAIT_DATA = "gaitdata";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";

    // Gait Data Table Columns names
    private static final String KEY_EVENT_ID = "event_id";
    private static final String KEY_EVENT_TYPE = "event_type";
    private static final String KEY_EVENT_ADDRESS = "event_address";
    private static final String KEY_EVENT_DATE = "event_date";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        String CREATE_GAITDATA_TABLE = "CREATE TABLE " + TABLE_GAIT_DATA + "("
                + KEY_EVENT_ID + " INTEGER PRIMARY KEY," + KEY_EVENT_TYPE + " TEXT," + KEY_EVENT_ADDRESS + " TEXT,"
                + KEY_EVENT_DATE + " TEXT" + ")";
        db.execSQL(CREATE_GAITDATA_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAIT_DATA);

        // Create tables again
        onCreate(db);
    }


    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addContact(User contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName()); // Contact Name
        values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    // Adding new event
    public void addEvent(GaitData event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_TYPE, event.getEventType());
        values.put(KEY_EVENT_ADDRESS, event.getEventLocation());
        values.put(KEY_EVENT_DATE, event.getEventDate());

        // Inserting Row
        db.insert(TABLE_GAIT_DATA, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    User getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        User contact = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }

    // Getting All Contacts
    public ArrayList<User> getAllContacts() {
        ArrayList<User> contactList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User contact = new User();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public ArrayList<GaitData> getAllEvents() {
        ArrayList<GaitData> eventList = new ArrayList<GaitData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GAIT_DATA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GaitData event = new GaitData();
                event.setId(Integer.parseInt(cursor.getString(0)));
                event.setEventType(cursor.getString(1));
                event.setEventLocation(cursor.getString(2));
                event.setEventDate(cursor.getString(3));

                eventList.add(event);
            } while (cursor.moveToNext());
        }


        return eventList;
    }

    // Updating single contact
    public int updateContact(User contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

    // Deleting single contact
    public void deleteContact(User contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getID())});
        db.close();
    }

    public void deleteEvent(GaitData event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GAIT_DATA, KEY_EVENT_ID + " = ?",
                new String[] { String.valueOf(event.getId()) });
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}