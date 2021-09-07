package com.book.reading.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.book.reading.activity.MainActivity;
import com.book.reading.model.Book;
import com.book.reading.model.Highlight;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by bhavana on 1/22/2018.
 */

public class HighlightDBHelper extends SQLiteOpenHelper {


    //database version
    public static int db_version = 1;
    //database name
    public static String db_name = "db_highlight.db";
    public String db_path;
    public SQLiteDatabase db;
    public final Context context;

    //database table name
    public static String NOTE_TABLE_NAME = "tbl_note";
    public static String MARK_TABLE_NAME = "tbl_mark";
    public static String UNDERLINE_TABLE_NAME = "tbl_underline";


    //column names
    public static String ID = "id";
    public static String CAT_ID = "cat_id";
    public static String CHAPTER_ID = "chapter_id";
    public static String NOTE = "note";
    public static String WORDS = "word";
    public static String INDEX = "position";
    public static String COLOR = "color";
    public ArrayList<Book> chapterList;


    public HighlightDBHelper(Context context) {
        super(context, db_name, null, db_version);
        this.context = context;

        db_path = context.getDatabasePath(db_name).toString().replace(db_name, "");
        // db_path = "/data/data/org.nishkulanand.kavya.bhujmandir/databases/";
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


    /*    String upgradeQuery = "ALTER TABLE " + NOTE_TABLE_NAME + " ADD COLUMN note_color TEXT ";
        if (oldVersion < db_version)
            Log.v("Database Upgrade", "Database version higher than old.");
        db.execSQL(upgradeQuery);*/
        //db_delete();

    }

    //delete database
    public void db_delete() {
        File file = new File(db_path + db_name);
        if (file.exists()) {
            file.delete();

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


    /**
     * insert data in note table
     *
     * @param cat_id
     * @param chapter_id
     * @param index
     * @param note
     * @param word
     */
    public void insertNoteIntoDB(int cat_id, int chapter_id, int index, String note, String word) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(CAT_ID, cat_id);
            values.put(CHAPTER_ID, chapter_id);
            values.put(INDEX, index);
            values.put(NOTE, note);
            values.put(WORDS, word);
            db.insert(NOTE_TABLE_NAME, null, values);

            db.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    /**
     * insert data in tbl_mark table
     *
     * @param cat_id
     * @param chapter_id
     * @param index
     * @param word
     */
    public void insertMarkWordIntoDB(int cat_id, int chapter_id, int index, String word) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(CAT_ID, cat_id);
            values.put(CHAPTER_ID, chapter_id);
            values.put(INDEX, index);
            values.put(WORDS, word);
            db.insert(MARK_TABLE_NAME, null, values);

            db.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    /**
     * insert data in tbl_underline table
     *
     * @param cat_id
     * @param chapter_id
     * @param index
     * @param word
     */
    public void insertUnderlineWordIntoDB(int cat_id, int chapter_id, int index, String word) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(CAT_ID, cat_id);
            values.put(CHAPTER_ID, chapter_id);
            values.put(INDEX, index);
            values.put(WORDS, word);
            db.insert(UNDERLINE_TABLE_NAME, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //get All Note from table
    public ArrayList<Highlight> getAllNote() {
        ArrayList<Highlight> noteList = new ArrayList<Highlight>();

        String selectQuery = "SELECT  * FROM  " + NOTE_TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
    try {
    if (c.getCount() > 0) {
        if (c.moveToFirst()) {
            do {

                Highlight note = new Highlight();
                note.setNote(c.getString(c.getColumnIndex(NOTE)));
                note.setV_id(c.getString(c.getColumnIndex(CHAPTER_ID)));
                note.setIndex(c.getInt(c.getColumnIndex(INDEX)));
                note.setChapterId(c.getInt(c.getColumnIndex(CHAPTER_ID)));
                note.setWords(c.getString(c.getColumnIndex(WORDS)));
                note.setCatId(c.getInt(c.getColumnIndex(CAT_ID)));
                chapterList = MainActivity.mainDbHelper.get_Bookmarked(c.getInt(c.getColumnIndex(CHAPTER_ID)));
                for(int i=0;i<chapterList.size();i++) {
                    note.setV_title(chapterList.get(i).getTitle());
                    note.setCatId(chapterList.get(i).getCatId());
                    note.setB_title(chapterList.get(i).getDescription());
                }
                noteList.add(note);
                //System.out.println("cls:" + chapterList.size() + "==" + chapterList.get(0).getTitle() + "==" + chapterList.get(0).getCatId() + "==");
            } while (c.moveToNext());
        }
    }
    }catch(Exception e)
    {
        e.printStackTrace();
    }
        return noteList;

    }

    //get markList by chapter id
    public ArrayList<Highlight> getMarkByChapterId(int chapter_id) {
        ArrayList<Highlight> noteList = new ArrayList<Highlight>();

        String selectQuery = "SELECT  * FROM " + MARK_TABLE_NAME + " where " + CHAPTER_ID + "=  " + chapter_id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.getCount() > 0) {
            if (c.moveToFirst()) {
                do {
                    Highlight mark = new Highlight();
                    mark.setIndex(c.getInt(c.getColumnIndex(INDEX)));
                    mark.setChapterId(c.getInt(c.getColumnIndex(CHAPTER_ID)));
                    mark.setWords(c.getString(c.getColumnIndex(WORDS)));
                    mark.setCustomColor(c.getString(c.getColumnIndex(COLOR)));
                    noteList.add(mark);

                } while (c.moveToNext());
            }
        }
        return noteList;
    }

    //get underLine by chapter id
    public ArrayList<Highlight> getUnderlineByChapterId(int chapter_id) {
        ArrayList<Highlight> noteList = new ArrayList<Highlight>();

        String selectQuery = "SELECT  * FROM " + UNDERLINE_TABLE_NAME + " where " + CHAPTER_ID + "=  " + chapter_id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.getCount() > 0) {
            if (c.moveToFirst()) {
                do {
                    Highlight underline = new Highlight();
                    underline.setIndex(c.getInt(c.getColumnIndex(INDEX)));
                    underline.setChapterId(c.getInt(c.getColumnIndex(CHAPTER_ID)));
                    underline.setCustomColor(c.getString(c.getColumnIndex(COLOR)));
                    underline.setWords(c.getString(c.getColumnIndex(WORDS)));
                    noteList.add(underline);

                } while (c.moveToNext());
            }
        }
        return noteList;
    }

    //get noteList by chapter id
    public ArrayList<Highlight> getNoteByChapterId(int chapter_id) {
        ArrayList<Highlight> noteList = new ArrayList<Highlight>();
        String selectQuery = "SELECT  * FROM " + NOTE_TABLE_NAME + " where " + CHAPTER_ID + "=  " + chapter_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.getCount() > 0) {
            if (c.moveToFirst()) {
                do {
                    Highlight note = new Highlight();
                    note.setNote(c.getString(c.getColumnIndex(NOTE)));
                    note.setIndex(c.getInt(c.getColumnIndex(INDEX)));
                    note.setChapterId(c.getInt(c.getColumnIndex(CHAPTER_ID)));
                    note.setWords(c.getString(c.getColumnIndex(WORDS)));
                    note.setCustomColor(c.getString(c.getColumnIndex(COLOR)));
                    noteList.add(note);

                } while (c.moveToNext());
            }
        }
        return noteList;
    }


    /*
     * get note by note word index
     */
    public String GetNoteByIndex(int chapter, int index) {

        String note = "";
        String selectQuery = "SELECT  * FROM " + NOTE_TABLE_NAME + " WHERE  " + CHAPTER_ID + " = " + chapter + " and " + INDEX + "=" + index;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                note = c.getString(c.getColumnIndex(NOTE));
            } while (c.moveToNext());
        }
        return note;
    }
    /*
     * Update Note
     */
    public void UpdateNote(int id, int index, String val) {
        db = this.getReadableDatabase();
        ContentValues value = new ContentValues();
        value.put(NOTE, "" + val);
        db.update(NOTE_TABLE_NAME, value, CHAPTER_ID + "=" + id + "  and  " + INDEX + "=" + index, null);

    }

    //update note color
    public void UpdateNoteColor(int id, int index, String val) {
        db = this.getReadableDatabase();
        ContentValues value = new ContentValues();
        value.put(COLOR, "" + val);
        db.update(NOTE_TABLE_NAME, value, CHAPTER_ID + "=" + id + "  and  " + INDEX + "=" + index, null);

    }

    //update mark color
    public void UpdateMarkColor(int id, int index, String val) {
        db = this.getReadableDatabase();
        ContentValues value = new ContentValues();
        value.put(COLOR, "" + val);
        db.update(MARK_TABLE_NAME, value, CHAPTER_ID + "=" + id + "  and  " + INDEX + "=" + index, null);

    }

    //update underline color
    public void UpdateUnderlineColor(int id, int index, String val) {
        db = this.getReadableDatabase();
        ContentValues value = new ContentValues();
        value.put(COLOR, "" + val);
        db.update(UNDERLINE_TABLE_NAME, value, CHAPTER_ID + "=" + id + "  and  " + INDEX + "=" + index, null);

    }

    //delete note
    public void delete_note(int chapter_id, int index) {
        SQLiteDatabase db = this.getReadableDatabase();
        String delete_query = " DELETE FROM " + NOTE_TABLE_NAME + " WHERE " + CHAPTER_ID + "=" + chapter_id + "  and  " + INDEX + "=" + index;
        db.execSQL(delete_query);

    }

    //delete mark word
    public void delete_mark(int chapter_id, int index) {
        SQLiteDatabase db = this.getReadableDatabase();
        String delete_query = " DELETE FROM " + MARK_TABLE_NAME + " WHERE " + CHAPTER_ID + "=" + chapter_id + "  and  " + INDEX + "=" + index;
        db.execSQL(delete_query);

    }

    //delete underline word
    public void delete_underline(int chapter_id, int index) {
        SQLiteDatabase db = this.getReadableDatabase();
        String delete_query = " DELETE FROM " + UNDERLINE_TABLE_NAME + " WHERE " + CHAPTER_ID + "=" + chapter_id + "  and  " + INDEX + "=" + index;
        db.execSQL(delete_query);

    }
}
