package com.kunall17.ionautologin.Functions;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunall17 on 12/24/15.
 */


public class SQLiteDatabaseAdapter {

    private static SQLiteDatabaseAdapter instance;
    Context context;
    SQLiteDatabase database;

    public static SQLiteDatabaseAdapter getInstance(Context context) {
        if (instance == null) {
            instance = new SQLiteDatabaseAdapter(context);
        }
        return instance;
    }

    private SQLiteDatabaseAdapter(Context context) {
        this.context = context;
        database = new SQLiteDatabase(context);
    }

    public int insertData(String username, String password) throws java.sql.SQLIntegrityConstraintViolationException {
        android.database.sqlite.SQLiteDatabase db = database.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteDatabase.username, username);
        contentValues.put(SQLiteDatabase.password, password);
        return (int) db.insert(SQLiteDatabase.TABLE_NAME, null, contentValues);
    }


    public List<User> getAllData() throws NullPointerException {

        android.database.sqlite.SQLiteDatabase db = database.getReadableDatabase();
        String[] columns = {SQLiteDatabase.UID, SQLiteDatabase.username, SQLiteDatabase.password};
        Cursor cursor = db.query(SQLiteDatabase.TABLE_NAME, columns, null, null, null, null, null);
        List<User> listOfUsers = new ArrayList<>();
        User user = new User("", "");
        while (cursor.moveToNext()) {
            Log.d("userFound", cursor.getString(1) + " " + cursor.getString(2));
            user = new User(cursor.getString(1), cursor.getString(2));
            listOfUsers.add(user);
        }
        return listOfUsers;
    }

    public Boolean ifUserNameExists(String defaultUsername) {
        android.database.sqlite.SQLiteDatabase db = database.getReadableDatabase();
        String[] columns = {SQLiteDatabase.UID, SQLiteDatabase.username, SQLiteDatabase.password};
        Cursor cursor = db.query(SQLiteDatabase.TABLE_NAME, columns, SQLiteDatabase.username + "=\"" + defaultUsername + "\"", null, null, null, null);
        if (cursor.getCount() > 0) return true;
        return false;
    }

    public String getPassword(String username) throws NullPointerException {
        android.database.sqlite.SQLiteDatabase db = database.getReadableDatabase();
        String[] columns = {SQLiteDatabase.password};
        Cursor cursor = db.query(SQLiteDatabase.TABLE_NAME, columns, SQLiteDatabase.username + "=\"" + username + "\"", null, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(0);
        //TODO getPassword;
    }

    public boolean removeUser(String username) {
        android.database.sqlite.SQLiteDatabase db = database.getWritableDatabase();
        return db.delete(SQLiteDatabase.TABLE_NAME, SQLiteDatabase.username + "='" + username + "'", null) > 0;
    }

    static class SQLiteDatabase extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "userdatabase";
        private static final String TABLE_NAME = "USERTABLE";
        private static final String UID = "_id";
        private static final String username = "username";
        private static final String password = "password";
        private static final int DATABASE_VERSION = 1;

        public SQLiteDatabase(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(android.database.sqlite.SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT," + username + " VARCHAR(255)," + password + " VARCHAR(255));");
        }

        @Override
        public void onUpgrade(android.database.sqlite.SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}

