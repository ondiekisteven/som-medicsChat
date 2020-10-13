package cf.somwaki.medicalchat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MessagesDatabase";

    public MyDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SampleDBContract.Message.CREATE_TABLE);
        db.execSQL(SampleDBContract.Notification.CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SampleDBContract.Message.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SampleDBContract.Notification.TABLE_NAME);
        onCreate(db);
    }


    /*
    * Method to save messages to database
    * Returns -1 if there was an error when saving, otherwise any other number
    * */
    long saveToDatabase(MyMessage message) {
        SQLiteDatabase database = this.getWritableDatabase();

        int belongs;
        if (message.isBelongsToCurrentUser()) {belongs = 1; }
        else{belongs = 0;}
        ContentValues values = new ContentValues();
        values.put(SampleDBContract.Message.COLUMN_NAME, message.getText());
        values.put(SampleDBContract.Message.COLUMN_BELONGS_TO_USER, belongs);

        return database.insert(SampleDBContract.Message.TABLE_NAME, null, values);
    }

    /*
    * Saving a notification item to the database.
    * Returns true if saving was successful otherwise
    * returns false.
    * */
    boolean saveToDatabase(NotificationItem item) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SampleDBContract.Notification.COLUMN_TITLE, item.getTitle());
        values.put(SampleDBContract.Notification.COLUMN_BODY, item.getBody());

        long res = database.insert(SampleDBContract.Notification.TABLE_NAME, null, values);

        return res != -1;
    }


    /*
    * Method to get all notifications from the database. Returns answer as a cursor
    * for the method in notification activity to check its status using
    * cursor functions.
    * */
    Cursor getNotifications() {
        SQLiteDatabase database = this.getReadableDatabase();
        String[] toReturn = {
                SampleDBContract.Notification._ID,
                SampleDBContract.Notification.COLUMN_TITLE,
                SampleDBContract.Notification.COLUMN_BODY
        };
        return database.query(SampleDBContract.Notification.TABLE_NAME, toReturn, null, null, null, null, null);
    }

    Cursor getMessages() {
        SQLiteDatabase database = this.getReadableDatabase();
        String [] toReturn = {
                SampleDBContract.Message._ID,
                SampleDBContract.Message.COLUMN_NAME,
                SampleDBContract.Message.COLUMN_BELONGS_TO_USER
        };

        return database.query(SampleDBContract.Message.TABLE_NAME, toReturn, null, null, null, null, null);

    }
}
