package com.example.t8mobile_h071231080;

import android.database.Cursor;

import java.util.ArrayList;
public class MappingHelper {

    public static ArrayList<Note> mapCursorToArrayList(Cursor cursor) {
        ArrayList<Note> notes = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.NotesColumns._ID));
            String judul = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NotesColumns.JUDUL));
            String deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NotesColumns.DESKRIPSI));

            String createdAt = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NotesColumns.CREATED_AT));
            String updatedAt = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NotesColumns.UPDATED_AT));

            if (createdAt == null) createdAt = "";
            if (updatedAt == null) updatedAt = "";

            notes.add(new Note(id, judul, deskripsi, createdAt, updatedAt));
        }

        return notes;
    }
}