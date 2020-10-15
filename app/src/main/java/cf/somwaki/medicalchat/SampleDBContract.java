package cf.somwaki.medicalchat;

import android.provider.BaseColumns;

import com.google.android.material.tabs.TabLayout;

class SampleDBContract {
    SampleDBContract(){}

    static class Message implements BaseColumns {
        static final String TABLE_NAME = "message";
        static final String COLUMN_NAME = "text";
        static final String COLUMN_BELONGS_TO_USER = "belongsToUser";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_BELONGS_TO_USER + " INTEGER" + ")";
    }

    static class Notification implements BaseColumns{
        static final String TABLE_NAME = "notifications";
        static final String COLUMN_TITLE = "title";
        static final String COLUMN_BODY = "body";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_BODY + " TEXT" +
                ")";

    }

    static class Remedy implements BaseColumns {
        static final String TABLE_NAME = "remedies";
        static final String COLUMN_TITLE = "title";
        static final String COLUMN_BODY = "body";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_BODY + " TEXT" +
                ")";
    }


}
