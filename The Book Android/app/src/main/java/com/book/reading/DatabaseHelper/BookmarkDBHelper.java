package com.book.reading.DatabaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.book.reading.activity.MainActivity;
import com.book.reading.model.Book;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class BookmarkDBHelper extends SQLiteOpenHelper {

    //database version
    private static int db_version = 1;

    //database name
    private static String db_name = "bookmark.db";
    //table name
    public static String TABLE_NAME = "bookmarked";
    //colimn name
    public static String V_ID = "v_id";
    private String db_path;
    private SQLiteDatabase db;
    private final Context context;


    ArrayList<Book> bookArrayList = new ArrayList<>();


    public BookmarkDBHelper(Context context) {
        super(context, db_name, null, db_version);
        this.context = context;

        File database = context.getDatabasePath(db_name);
        if (!database.exists()) {
            // Database does not exist so copy it from assets here
            db_path = context.getDatabasePath(db_name).toString().replace(db_name, "");
        } else {
            db_path = context.getDatabasePath(db_name).toString();

        }
    }
    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            Log.v("Database Upgrade", "Database version higher than old.");
            db_delete();
        }
    }

    //delete database
    public void db_delete() {
        File file = new File(db_path + db_name);
        if (file.exists()) {
            file.delete();
            System.out.println("delete database file.");
        }
    }
    //Create a empty database on the system

    public void createDatabase() throws IOException {
        boolean dbExist = checkDataBase();

        if (dbExist) {
            Log.v("DB Exists", "db exists");
        }
        boolean dbExist1 = checkDataBase();
        if (!dbExist1) {
            this.getWritableDatabase();
            try {
                this.close();
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }
    //Check database already exist or not
    private boolean checkDataBase() {
        boolean checkDB = false;
        try {
            String myPath = db_path + db_name;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
        } catch (SQLiteException e) {
        }
        return checkDB;
    }

    //Copies your database from your local assets-folder to the just created empty database in the system folder
    private void copyDataBase() throws IOException {
        String outFileName = db_path + db_name;
        OutputStream myOutput = new FileOutputStream(outFileName);
        InputStream myInput = context.getAssets().open(db_name);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myInput.close();
        myOutput.flush();
        myOutput.close();
    }


    //get bookmarked id
    public int getBookmarks(int id) {
        int vId = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " where v_id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                vId = c.getInt(c.getColumnIndex("v_id"));
            } while (c.moveToNext());
        }
        return vId;
    }
}
