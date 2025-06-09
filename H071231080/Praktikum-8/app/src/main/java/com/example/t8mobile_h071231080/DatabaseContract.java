package com.example.t8mobile_h071231080;

import android.provider.BaseColumns;

public final class DatabaseContract {
    public static final String TABLE_NAME = "notes";
    public static final class NotesColumns implements BaseColumns {
        public static final String JUDUL = "judul";
        public static final String DESKRIPSI = "deskripsi";
        public static final String CREATED_AT = "created_at";
        public static final String UPDATED_AT = "updated_at";
    }
}
