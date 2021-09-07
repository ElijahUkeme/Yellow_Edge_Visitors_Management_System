package com.book.reading.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.book.reading.adapter.ListAdapter;
import com.book.reading.adapter.ReadingAdapter;
import com.book.reading.fragment.ReadingFragment;
import com.book.reading.model.Book;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.book.reading.R;
import com.book.reading.adapter.CategoryListAdapter;
import com.book.reading.DatabaseHelper.HighlightDBHelper;
import com.book.reading.model.Category;
import com.book.reading.helper.Constants;
import com.book.reading.DatabaseHelper.BookmarkDBHelper;
import com.book.reading.DatabaseHelper.MainDBHelper;
import com.book.reading.helper.Utility;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public Fragment fragment;
    public Fragment subFragment = null;

    //for reading activity from reading fragment
    public static ViewPager pager;
    public static ReadingAdapter adapter;
    public static Context mContext;
    public Book book;
    int selected;

    public Toolbar toolBar;
    private RecyclerView recyclerView;
    public RecyclerView.LayoutManager layout_manager;
    public static MainDBHelper mainDbHelper;
    public static BookmarkDBHelper bookmarkDbHelper;

    public static HighlightDBHelper highlightDBHelper;
    public ArrayList<Category> categoryArrayList;
    public ArrayList<Book> bookArrayList;
    public ListAdapter listAdapter;
    //CategoryListAdapter adapter;

    //The navigation drawer layout
    private DrawerLayout drawer;
    public int id = 0;
    String theIndex;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolBar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        pager = findViewById(R.id.viewPager);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragment = new com.book.reading.fragment.ListFragment();


        Constants.status = true;
        bookmarkDbHelper = new BookmarkDBHelper(getApplicationContext());
        mainDbHelper = new MainDBHelper(getApplicationContext());
        highlightDBHelper = new HighlightDBHelper(getApplicationContext());
        try {
            //create database
            mainDbHelper.createDB();
            bookmarkDbHelper.createDatabase();
            highlightDBHelper.createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        recyclerView =  findViewById(R.id.my_list_main);
        layout_manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout_manager);
        categoryArrayList = new ArrayList<>();
        init();

        // Declaring and initializing the navigation drawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolBar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    public void init() {

        try {
            subFragment = new com.book.reading.fragment.ListFragment();
            bookArrayList = new ArrayList<>();
            bookArrayList = mainDbHelper.getAllHymns();
            listAdapter = new ListAdapter(this,bookArrayList);
            recyclerView.setAdapter(listAdapter);
            initit();

            Bundle extras = getIntent().getExtras();
            theIndex = String.valueOf(extras.getString("index"));

            if (extras ==null){
                bookArrayList = mainDbHelper.getAllHymns();
            }
            if (theIndex.equalsIgnoreCase("one")){
               bookArrayList = mainDbHelper.getBasedOnIndexOne();
            }
            else if (theIndex.equalsIgnoreCase("two")){
                bookArrayList = mainDbHelper.getBasedOnIndexTwo();
            }
            else if (theIndex.equalsIgnoreCase("three")){
                bookArrayList = mainDbHelper.getBasedOnIndexThree();
            }
            else if (theIndex.equalsIgnoreCase("four")){
                bookArrayList = mainDbHelper.getBasedOnIndexFour();
            }
            else if (theIndex.equalsIgnoreCase("five")){
                bookArrayList = mainDbHelper.getBasedOnIndexFive();
            }
            else if (theIndex.equalsIgnoreCase("six")){
                bookArrayList = mainDbHelper.getBasedOnIndexSix();
            }
            else if (theIndex.equalsIgnoreCase("seven")){
                bookArrayList = mainDbHelper.getBasedOnIndexSeven();
            }
            else if (theIndex.equalsIgnoreCase("eight")){
                bookArrayList = mainDbHelper.getBasedOnIndexEight();
            }
            else if (theIndex.equalsIgnoreCase("nine")){
                bookArrayList = mainDbHelper.getBasedOnIndexNine();
            }
            else if (theIndex.equalsIgnoreCase("ten")){
                bookArrayList = mainDbHelper.getBasedOnIndexTen();
            }
            else if (theIndex.equalsIgnoreCase("eleven")){
                bookArrayList = mainDbHelper.getBasedOnIndexEleven();
            }
            else if (theIndex.equalsIgnoreCase("twelve")){
                bookArrayList = mainDbHelper.getBasedOnIndexTwelve();
            }
            else if (theIndex.equalsIgnoreCase("thirteen")){
                bookArrayList = mainDbHelper.getBasedOnIndexThirteen();
            }
            else if (theIndex.equalsIgnoreCase("fourteen")){
                bookArrayList = mainDbHelper.getBasedOnIndexFourteen();
            }
            else if (theIndex.equalsIgnoreCase("fifteen")){
                bookArrayList = mainDbHelper.getBasedOnIndexFifteen();
            }else {
                bookArrayList = mainDbHelper.getAllHymns();
            }
            listAdapter = new ListAdapter(this,bookArrayList);
            recyclerView.setAdapter(listAdapter);
            initit();

    } catch (Exception e) {
        e.printStackTrace();
    }}

    // new added features from reading fragment
    public void initit() {
        mContext = (MainActivity.this);
        try {
            fragment = new ReadingFragment();

            //when we change the hymn. we should change action bar title with selected hymn
            bookArrayList = ListAdapter.bookArrayList;
            adapter = new ReadingAdapter(this, bookArrayList);
            pager.setAdapter(adapter);
            pager.setCurrentItem(selected, true);
        } catch (Exception e) {
            Toast.makeText(this, "error" + e, Toast.LENGTH_LONG);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;//return true so that the menu pop up is opened
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            //search for all the hymns
            case R.id.search:
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("id", "0");
                intent.putExtra("title", "");
                Constants.category = "all_search";
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
    // Navigation drawer
   @Override
   public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.drawer_home:
                onBackPressed();
                break;

            case R.id.drawer_index:
                Intent intoIndex = new Intent(this,IndexActivity.class);
                startActivity(intoIndex);
                break;

            case R.id.drawer_order_of_mass:
                Intent intent = new Intent(MainActivity.this,OrderOfMassActivity.class);
                startActivity(intent);
                break;

            case R.id.drawer_contact:
                contactUs();
                break;

            case R.id.drawer_about:
                aboutUsDialog();
                break;

            case R.id.drawer_rate:
                rateUsDialog();
                break;

            case R.id.drawer_share:
                shareApp();
                break;

            case R.id.drawer_logout:
                logoutDialog();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
    public void rateUsDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CATHOLIC HYMN BOOK");
        builder.setMessage("We hope this App help you in your praises and worship to God, and we kindly ask you to help us;\nPlease rate us on Google Play Store!");
        builder.setPositiveButton("RATE US", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                startActivity(intent);
            }
        });
        builder.setNegativeButton("LATER",(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // close dialog
                dialog.cancel();
            }
        }));
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void logoutDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("LOGGING OUT");
        builder.setMessage("Do you really want to log out from Catholic Hymn Book Application?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void aboutUsDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CATHOLIC HYMN BOOK");
        builder.setMessage("This App was developed in Nigeria.\nDeveloper and Designer: Elijah Ukeme\nAddress: Kubwa, Abuja, Nigeria\nE-mail:ukemedmet@gmail.com");
        builder.setPositiveButton("CONTACT US", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                contactUs();
            }
        });
        builder.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void shareApp(){
        try {
            Intent shareAppIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareAppIntent.setType("text/plain");
            shareAppIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getPackageName());
            startActivity(Intent.createChooser(shareAppIntent, "Share via"));

        } catch (Exception e) {
            Log.e("Share error", e.getMessage());
        }
    }
   public void contactUs(){
       Intent intent = new Intent(this,ContactActivity.class);
       startActivity(intent);
   }

}
