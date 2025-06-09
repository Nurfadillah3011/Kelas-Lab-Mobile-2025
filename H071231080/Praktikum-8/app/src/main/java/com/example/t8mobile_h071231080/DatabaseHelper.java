package com.example.t8mobile_h071231080;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "dbnotes";
    private static final int DATABASE_VERSION = 2;
    private static final String SQL_CREATE_TABLE_NOTES =
            String.format(
                    "CREATE TABLE %s"
                            + " (%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + " %s TEXT NOT NULL,"
                            + " %s TEXT NOT NULL,"
                            + " %s TEXT,"
                            + " %s TEXT)",
                    DatabaseContract.TABLE_NAME,
                    DatabaseContract.NotesColumns._ID,
                    DatabaseContract.NotesColumns.JUDUL,
                    DatabaseContract.NotesColumns.DESKRIPSI,
                    DatabaseContract.NotesColumns.CREATED_AT,
                    DatabaseContract.NotesColumns.UPDATED_AT
            );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_NAME);
        onCreate(db);
    }
}
