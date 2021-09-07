package com.book.reading.DatabaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.audiofx.AudioEffect;
import android.os.Build;
import android.util.Log;

import com.book.reading.model.Book;
import com.book.reading.model.Category;

public class MainDBHelper extends SQLiteOpenHelper {

    private static int db_version = 1;
    private static String db_name = "lorem_lpsum.db";
    private String db_path="";

    //database table name
    public static String CATEGORY_TABLE_NAME = "tbl_category";
    public static String DETAIL_TABLE_NAME = "tbl_detail";

    //column name

    public static String ID="Id";
    public static String TITLE="Table";
    public static String CATEGORY_ID="Category_Id";
    public static String DESCRIPTION="Description";


    private SQLiteDatabase db;
    private final Context con;

    public MainDBHelper(Context context) {
        super(context, db_name, null, db_version);
        // TODO Auto-generated constructor stub


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            //  DataHelperNotes helper = new DataHelperNotes(con);
            db_path= context.getDatabasePath(db_name).getAbsolutePath();
        }else {
            db_path = context.getDatabasePath(db_name).toString().replace(db_name, "");
            // System.out.println("HELLOOOOOOOO"+db_path);
        }
        this.con = context;
       // db_path = con.getDatabasePath(db_name).toString().replace(db_name, "");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }

    public void createDB() throws IOException {

        /*if (checkDB()) {
        } else if (!checkDB()) {
            this.getReadableDatabase();
            close();
            copyDB();
        }*/
        boolean dbExist = checkDB();

        if (dbExist) {
            Log.v("DB Exists", "db exists");
            // By calling this method here onUpgrade will be called on a
            // writeable database, but only if the version number has been
            // bumped
            //onUpgrade(myDataBase, DATABASE_VERSION_old, DATABASE_VERSION);
        }

        boolean dbExist1 = checkDB();
        if (!dbExist1) {
            this.getWritableDatabase();
            try {
                this.close();
                copyDB();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }

    }

    private boolean checkDB() {

        /*SQLiteDatabase cDB = null;
        try {
            cDB = SQLiteDatabase.openDatabase(db_path + db_name, null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        if (cDB != null) {
            cDB.close();
        }
        return cDB != null ? true : false;*/
        boolean checkDB = false;
        try {
            String myPath = db_path + db_name;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
        } catch (SQLiteException e) {
        }
        return checkDB;
    }


    private void copyDB() throws IOException {
        /*InputStream inputFile = con.getAssets().open(db_name);
       // String outFileName = db_path + db_name;
        OutputStream outFile = new FileOutputStream(db_path);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputFile.read(buffer)) > 0) {
            outFile.write(buffer, 0, length);
        }
        outFile.flush();
        outFile.close();
        inputFile.close();*/
        InputStream myInput=con.getAssets().open(db_name);
        OutputStream myOutput;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            myOutput  = new FileOutputStream(db_path);
        }
        else {
            myOutput = new FileOutputStream(db_path+db_name);
        }
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myInput.close();
        myOutput.flush();
        myOutput.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        //  db.execSQL("DROP TABLE IF EXISTS " + Constants.VAR_HINDI);
        // db.execSQL("DROP TABLE IF EXISTS " + Constants.VAR_ENG);

    }

    //when user change language from reading area .whe should get right content from table with language
    //get description
    public String getDescription(int id) {
        String des = "";
        String selectQuery = "SELECT  * FROM " + DETAIL_TABLE_NAME + " where Id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                des = c.getString(c.getColumnIndex("Description"));

            } while (c.moveToNext());
        }
        return des;
    }

    //when user change language from reading area .whe should get right content from table with language
    //get chapter title
    public String getTitle(int id) {
        String title = "";
        String selectQuery = "SELECT  * FROM " + DETAIL_TABLE_NAME + " where Id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                title = c.getString(c.getColumnIndex("Title"));
            } while (c.moveToNext());
        }
        return title;
    }

    //set actionbar title with selected language
    public String getActionBarTitle(int id) {
        String title = "";
        String selectQuery = "SELECT  * FROM " + CATEGORY_TABLE_NAME + " where Id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                title = c.getString(c.getColumnIndex("Title"));
            } while (c.moveToNext());
        }
        return title;
    }

    /*
     *get All chapter of book from table with out filter
     */
    public ArrayList<Book> getBookChapterWithCategory(int categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> bookArrayList = new ArrayList<>();
        String query = "SELECT * FROM  " + DETAIL_TABLE_NAME + "  WHERE Category_Id = " + categoryId;
        Cursor cur = db.rawQuery(query, null);
        if (cur.moveToFirst()) {
            do {
                Book book = new Book();
                byte[] byt = cur.getBlob(cur.getColumnIndex("Description"));
                String str = new String(byt);
                book.setDescription(str);
                book.setTitle(cur.getString(cur.getColumnIndex("Title")));
                book.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("Id")).trim()));
                book.setCatId(Integer.parseInt(cur.getString(cur.getColumnIndex("Category_Id")).trim()));
                bookArrayList.add(book);
            } while (cur.moveToNext());
        }
        //}
        return bookArrayList;
    }

    /*
     *get All chapter of table with out filter
     */
    public ArrayList<Category> getMainCategory() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Category> categoryArrayList = new ArrayList<>();
        Cursor cur = db.rawQuery("SELECT * FROM  " + CATEGORY_TABLE_NAME, null);
        if (cur.moveToFirst()) {
            do {
                Category category = new Category();
                category.setCategoryId(cur.getInt(cur.getColumnIndex("Id")));
                category.setTitle(cur.getString(cur.getColumnIndex("Title")));
                categoryArrayList.add(category);

            } while (cur.moveToNext());
        }
        //}
        return categoryArrayList;
    }

    public ArrayList<Book> getAllHymns() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> bookArrayList = new ArrayList<>();
        String query = "SELECT * FROM  " + DETAIL_TABLE_NAME + "  WHERE 1 ";
        Cursor cur = db.rawQuery(query, null);
        if (cur.moveToFirst()) {
            do {
                Book book = new Book();
                byte[] byt = cur.getBlob(cur.getColumnIndex("Description"));
                String str = new String(byt);
                book.setDescription(str);
                book.setTitle(cur.getString(cur.getColumnIndex("Title")));
                book.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("Id")).trim()));
                book.setCatId(Integer.parseInt(cur.getString(cur.getColumnIndex("Category_Id")).trim()));
                bookArrayList.add(book);
            } while (cur.moveToNext());
        }
        return bookArrayList;
    }
    //get only chapter from table which are marked
    public ArrayList<Book> get_Bookmarked(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> bookArrayList = new ArrayList<>();
        String[] args = new String[]{};
        Cursor cur = db.rawQuery("SELECT * FROM  " + DETAIL_TABLE_NAME + "  WHERE Id=" + id, null);

        if (cur.moveToFirst()) {
            do {

                Book book = new Book();
                byte[] byt = cur.getBlob(cur.getColumnIndex("Description"));
                String str = new String(byt);
                book.setDescription(str);
                book.setTitle(cur.getString(cur.getColumnIndex("Title")));
                book.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("Id")).trim()));
                book.setCatId(Integer.parseInt(cur.getString(cur.getColumnIndex("Category_Id")).trim()));
                bookArrayList.add(book);

            } while (cur.moveToNext());
        }
        return bookArrayList;
    }

    public ArrayList<Book> getBasedOnIndexOne() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> bookArrayList = new ArrayList<>();
        String query = "SELECT * FROM  " + DETAIL_TABLE_NAME + "  WHERE Id >=1 AND Id <=42";
        Cursor cur = db.rawQuery(query, null);
        if (cur.moveToFirst()) {
            do {
                Book book = new Book();
                byte[] byt = cur.getBlob(cur.getColumnIndex("Description"));
                String str = new String(byt);
                book.setDescription(str);
                book.setTitle(cur.getString(cur.getColumnIndex("Title")));
                book.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("Id")).trim()));
                book.setCatId(Integer.parseInt(cur.getString(cur.getColumnIndex("Category_Id")).trim()));
                bookArrayList.add(book);
            } while (cur.moveToNext());
        }
        return bookArrayList;
    }
    public ArrayList<Book> getBasedOnIndexTwo() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> bookArrayList = new ArrayList<>();
        String query = "SELECT * FROM  " + DETAIL_TABLE_NAME + "  WHERE Id >=43 AND Id <=67";
        Cursor cur = db.rawQuery(query, null);
        if (cur.moveToFirst()) {
            do {
                Book book = new Book();
                byte[] byt = cur.getBlob(cur.getColumnIndex("Description"));
                String str = new String(byt);
                book.setDescription(str);
                book.setTitle(cur.getString(cur.getColumnIndex("Title")));
                book.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("Id")).trim()));
                book.setCatId(Integer.parseInt(cur.getString(cur.getColumnIndex("Category_Id")).trim()));
                bookArrayList.add(book);
            } while (cur.moveToNext());
        }
        return bookArrayList;
    }
    public ArrayList<Book> getBasedOnIndexThree() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> bookArrayList = new ArrayList<>();
        String query = "SELECT * FROM  " + DETAIL_TABLE_NAME + "  WHERE Id >=68 AND Id <=125";
        Cursor cur = db.rawQuery(query, null);
        if (cur.moveToFirst()) {
            do {
                Book book = new Book();
                byte[] byt = cur.getBlob(cur.getColumnIndex("Description"));
                String str = new String(byt);
                book.setDescription(str);
                book.setTitle(cur.getString(cur.getColumnIndex("Title")));
                book.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("Id")).trim()));
                book.setCatId(Integer.parseInt(cur.getString(cur.getColumnIndex("Category_Id")).trim()));
                bookArrayList.add(book);
            } while (cur.moveToNext());
        }
        return bookArrayList;
    }
    public ArrayList<Book> getBasedOnIndexFour() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> bookArrayList = new ArrayList<>();
        String query = "SELECT * FROM  " + DETAIL_TABLE_NAME + "  WHERE Id >=126 AND Id <=155";
        Cursor cur = db.rawQuery(query, null);
        if (cur.moveToFirst()) {
            do {
                Book book = new Book();
                byte[] byt = cur.getBlob(cur.getColumnIndex("Description"));
                String str = new String(byt);
                book.setDescription(str);
                book.setTitle(cur.getString(cur.getColumnIndex("Title")));
                book.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("Id")).trim()));
                book.setCatId(Integer.parseInt(cur.getString(cur.getColumnIndex("Category_Id")).trim()));
                bookArrayList.add(book);
            } while (cur.moveToNext());
        }
        return bookArrayList;
    }
    public ArrayList<Book> getBasedOnIndexFive() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> bookArrayList = new ArrayList<>();
        String query = "SELECT * FROM  " + DETAIL_TABLE_NAME + "  WHERE Id >=156 AND Id <=164";
        Cursor cur = db.rawQuery(query, null);
        if (cur.moveToFirst()) {
            do {
                Book book = new Book();
                byte[] byt = cur.getBlob(cur.getColumnIndex("Description"));
                String str = new String(byt);
                book.setDescription(str);
                book.setTitle(cur.getString(cur.getColumnIndex("Title")));
                book.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("Id")).trim()));
                book.setCatId(Integer.parseInt(cur.getString(cur.getColumnIndex("Category_Id")).trim()));
                bookArrayList.add(book);
            } while (cur.moveToNext());
        }
        return bookArrayList;
    }
    public ArrayList<Book> getBasedOnIndexSix() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> bookArrayList = new ArrayList<>();
        String query = "SELECT * FROM  " + DETAIL_TABLE_NAME + "  WHERE Id >=165 AND Id <=190";
        Cursor cur = db.rawQuery(query, null);
        if (cur.moveToFirst()) {
            do {
                Book book = new Book();
                byte[] byt = cur.getBlob(cur.getColumnIndex("Description"));
                String str = new String(byt);
                book.setDescription(str);
                book.setTitle(cur.getString(cur.getColumnIndex("Title")));
                book.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("Id")).trim()));
                book.setCatId(Integer.parseInt(cur.getString(cur.getColumnIndex("Category_Id")).trim()));
                bookArrayList.add(book);
            } while (cur.moveToNext());
        }
        return bookArrayList;
    }
    public ArrayList<Book> getBasedOnIndexSeven() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> bookArrayList = new ArrayList<>();
        String query = "SELECT * FROM  " + DETAIL_TABLE_NAME + "  WHERE Id >=191 AND Id <=202";
        Cursor cur = db.rawQuery(query, null);
        if (cur.moveToFirst()) {
            do {
                Book book = new Book();
                byte[] byt = cur.getBlob(cur.getColumnIndex("Description"));
                String str = new String(byt);
                book.setDescription(str);
                book.setTitle(cur.getString(cur.getColumnIndex("Title")));
                book.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("Id")).trim()));
                book.setCatId(Integer.parseInt(cur.getString(cur.getColumnIndex("Category_Id")).trim()));
                bookArrayList.add(book);
            } while (cur.moveToNext());
        }
        return bookArrayList;
    }
    public ArrayList<Book> getBasedOnIndexEight() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> bookArrayList = new ArrayList<>();
        String query = "SELECT * FROM  " + DETAIL_TABLE_NAME + "  WHERE Id >=203 AND Id <=217";
        Cursor cur = db.rawQuery(query, null);
        if (cur.moveToFirst()) {
            do {
                Book book = new Book();
                byte[] byt = cur.getBlob(cur.getColumnIndex("Description"));
                String str = new String(byt);
                book.setDescription(str);
                book.setTitle(cur.getString(cur.getColumnIndex("Title")));
                book.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("Id")).trim()));
                book.setCatId(Integer.parseInt(cur.getString(cur.getColumnIndex("Category_Id")).trim()));
                bookArrayList.add(book);
            } while (cur.moveToNext());
        }
        return bookArrayList;
    }
    public ArrayList<Book> getBasedOnIndexNine() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> bookArrayList = new ArrayList<>();
        String query = "SELECT * FROM  " + DETAIL_TABLE_NAME + "  WHERE Id >=218 AND Id <=270";
        Cursor cur = db.rawQuery(query, null);
        if (cur.moveToFirst()) {
            do {
                Book book = new Book();
                byte[] byt = cur.getBlob(cur.getColumnIndex("Description"));
                String str = new String(byt);
                book.setDescription(str);
                book.setTitle(cur.getString(cur.getColumnIndex("Title")));
                book.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("Id")).trim()));
                book.setCatId(Integer.parseInt(cur.getString(cur.getColumnIndex("Category_Id")).trim()));
                bookArrayList.add(book);
            } while (cur.moveToNext());
        }
        return bookArrayList;
    }
    public ArrayList<Book> getBasedOnIndexTen() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> bookArrayList = new ArrayList<>();
        String query = "SELECT * FROM  " + DETAIL_TABLE_NAME + "  WHERE Id >=271 AND Id <=289";
        Cursor cur = db.rawQuery(query, null);
        if (cur.moveToFirst()) {
            do {
                Book book = new Book();
                byte[] byt = cur.getBlob(cur.getColumnIndex("Description"));
                String str = new String(byt);
                book.setDescription(str);
                book.setTitle(cur.getString(cur.getColumnIndex("Title")));
                book.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("Id")).trim()));
                book.setCatId(Integer.parseInt(cur.getString(cur.getColumnIndex("Category_Id")).trim()));
                bookArrayList.add(book);
            } while (cur.moveToNext());
        }
        return bookArrayList;
    }
    public ArrayList<Book> getBasedOnIndexEleven() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> bookArrayList = new ArrayList<>();
        String query = "SELECT * FROM  " + DETAIL_TABLE_NAME + "  WHERE Id >=290 AND Id <=309";
        Cursor cur = db.rawQuery(query, null);
        if (cur.moveToFirst()) {
            do {
                Book book = new Book();
                byte[] byt = cur.getBlob(cur.getColumnIndex("Description"));
                String str = new String(byt);
                book.setDescription(str);
                book.setTitle(cur.getString(cur.getColumnIndex("Title")));
                book.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("Id")).trim()));
                book.setCatId(Integer.parseInt(cur.getString(cur.getColumnIndex("Category_Id")).trim()));
                bookArrayList.add(book);
            } while (cur.moveToNext());
        }
        return bookArrayList;
    }
    public ArrayList<Book> getBasedOnIndexTwelve() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> bookArrayList = new ArrayList<>();
        String query = "SELECT * FROM  " + DETAIL_TABLE_NAME + "  WHERE Id >=310 AND Id <=316";
        Cursor cur = db.rawQuery(query, null);
        if (cur.moveToFirst()) {
            do {
                Book book = new Book();
                byte[] byt = cur.getBlob(cur.getColumnIndex("Description"));
                String str = new String(byt);
                book.setDescription(str);
                book.setTitle(cur.getString(cur.getColumnIndex("Title")));
                book.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("Id")).trim()));
                book.setCatId(Integer.parseInt(cur.getString(cur.getColumnIndex("Category_Id")).trim()));
                bookArrayList.add(book);
            } while (cur.moveToNext());
        }
        return bookArrayList;
    }
    public ArrayList<Book> getBasedOnIndexThirteen() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> bookArrayList = new ArrayList<>();
        String query = "SELECT * FROM  " + DETAIL_TABLE_NAME + "  WHERE Id >=317 AND Id <=339";
        Cursor cur = db.rawQuery(query, null);
        if (cur.moveToFirst()) {
            do {
                Book book = new Book();
                byte[] byt = cur.getBlob(cur.getColumnIndex("Description"));
                String str = new String(byt);
                book.setDescription(str);
                book.setTitle(cur.getString(cur.getColumnIndex("Title")));
                book.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("Id")).trim()));
                book.setCatId(Integer.parseInt(cur.getString(cur.getColumnIndex("Category_Id")).trim()));
                bookArrayList.add(book);
            } while (cur.moveToNext());
        }
        return bookArrayList;
    }
    public ArrayList<Book> getBasedOnIndexFourteen() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> bookArrayList = new ArrayList<>();
        String query = "SELECT * FROM  " + DETAIL_TABLE_NAME + "  WHERE Id >=340 AND Id <=357";
        Cursor cur = db.rawQuery(query, null);
        if (cur.moveToFirst()) {
            do {
                Book book = new Book();
                byte[] byt = cur.getBlob(cur.getColumnIndex("Description"));
                String str = new String(byt);
                book.setDescription(str);
                book.setTitle(cur.getString(cur.getColumnIndex("Title")));
                book.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("Id")).trim()));
                book.setCatId(Integer.parseInt(cur.getString(cur.getColumnIndex("Category_Id")).trim()));
                bookArrayList.add(book);
            } while (cur.moveToNext());
        }
        return bookArrayList;
    }
    public ArrayList<Book> getBasedOnIndexFifteen() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> bookArrayList = new ArrayList<>();
        String query = "SELECT * FROM  " + DETAIL_TABLE_NAME + "  WHERE Id >=358 AND Id <=383";
        Cursor cur = db.rawQuery(query, null);
        if (cur.moveToFirst()) {
            do {
                Book book = new Book();
                byte[] byt = cur.getBlob(cur.getColumnIndex("Description"));
                String str = new String(byt);
                book.setDescription(str);
                book.setTitle(cur.getString(cur.getColumnIndex("Title")));
                book.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("Id")).trim()));
                book.setCatId(Integer.parseInt(cur.getString(cur.getColumnIndex("Category_Id")).trim()));
                bookArrayList.add(book);
            } while (cur.moveToNext());
        }
        return bookArrayList;
    }
}
