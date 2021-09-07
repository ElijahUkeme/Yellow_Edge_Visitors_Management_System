package com.book.reading.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import androidx.core.view.MenuItemCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.book.reading.activity.MainActivity;
import com.book.reading.activity.ReadingActivity;
import com.book.reading.adapter.ListAdapter;
import com.book.reading.helper.Utils;
import com.book.reading.model.Book;
import com.book.reading.DatabaseHelper.BookmarkDBHelper;

import com.book.reading.R;

import com.book.reading.adapter.ReadingAdapter;
import com.book.reading.helper.Constants;
import com.book.reading.helper.Utility;

import java.util.ArrayList;


public class ReadingFragment extends Fragment {

    public static ViewPager pager;
    public static ReadingAdapter adapter;
    public static Context mContext;
    public ArrayList<Book> bookArrayList;
    public Book book;
    public static View view;
    public Fragment fragment = null;
    public Menu menu;
    public Point p;
    private int isfav = 0;

    private BookmarkDBHelper bookmarkDbHelper = MainActivity.bookmarkDbHelper;


    public SearchView searchView;
    public String id;
    int selected;
    private MenuItem bookItem, menuItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //showactionbarmenu();
       // setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            final View view = inflater.inflate(R.layout.fragment_detail, container, false);
            this.view = view;
            isfav = 0;
            mContext = this.getActivity();
            pager = (ViewPager) view.findViewById(R.id.viewPager);
            fragment = ReadingFragment.this;
            setHasOptionsMenu(true);
            bookArrayList = ListAdapter.bookArrayList;

            Bundle b = getActivity().getIntent().getExtras();
            selected = Integer.parseInt(b.getString("position"));
            setHasOptionsMenu(true);
            init();
            pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {

                    selected = position;
                    isfav = bookmarkDbHelper.getBookmarks(bookArrayList.get(selected).getId());

                    if (isfav == bookArrayList.get(selected).getId()) {
                        bookItem.setIcon(getResources().getDrawable(R.drawable.ic_bookmark));
                        bookItem.setTitle(getString(R.string.bookmarked));
                    } else {
                        bookItem.setIcon(getResources().getDrawable(R.drawable.ic_unbook));
                        bookItem.setTitle(getString(R.string.unbook));
                    }
                    if (Utility.getmain("Main", getActivity()) == null) {
                    } else if (Integer.parseInt(Utility.getcategory("Category", getActivity())) == bookArrayList.get(selected).getCatId()) {
                        if (Integer.parseInt(Utility.getsubid("Subid", getActivity())) == (selected)) {
                            menuItem.setIcon(R.drawable.ic_pin);
                        } else {
                            menuItem.setIcon(R.drawable.ic_unpin);
                        }
                    }
                    if (Utility.scrollTimer != null) {
                        Utility.stopAutoScrolling();
                        view.findViewById(R.id.vertical_scrollview_id).scrollTo(0, 0);
                        ReadingActivity.seekBar.setProgress(0);
                    }
                    if (ReadingActivity.seekBar.getProgress() != 0) {
                        ReadingActivity.seekBar.setProgress(0);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
            book = bookArrayList.get(selected);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


    /*void showactionbarmenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(getActivity());
            java.lang.reflect.Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");

            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            // presumably, not relevant
        }
    }*/
    public void init() {
        mContext = this.getActivity();
        try {
            fragment = ReadingFragment.this;

            //when we change language of book. we should change action bar title with selected language
            ((ReadingActivity) getActivity()).setActionBarTitle();
            bookArrayList = ListAdapter.bookArrayList;
            adapter = new ReadingAdapter(getActivity(), bookArrayList);
            pager.setAdapter(adapter);
            pager.setCurrentItem(selected, true);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "error" + e, Toast.LENGTH_LONG).show();
        }
    }/*
    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
        this.menu = menu;

        bookItem = menu.findItem(R.id.bookmark);
        menuItem = menu.findItem(R.id.indicator);

        //when category is bookmark then we will hide indicator menu
      /*  if (Constants.category.equals("bookmark")) {
            menuItem.setVisible(false);

        }*/
        /*try {
            if (bookArrayList.size() != 0) {
                isfav = bookmarkDbHelper.getBookmarks(bookArrayList.get(selected).getId());

                if (isfav == bookArrayList.get(selected).getId()) {
                    bookItem.setIcon(R.drawable.ic_bookmark);
                    bookItem.setTitle(getString(R.string.bookmarked));
                } else {
                    bookItem.setIcon(R.drawable.ic_unbook);
                    bookItem.setTitle(getString(R.string.unbook));
                }
                if (Integer.parseInt(Utility.getcategory("Category", getActivity())) == bookArrayList.get(selected).getCatId()) {
                    if (Integer.parseInt(Utility.getsubid("Subid", getActivity())) == (selected)) {
                        menuItem.setIcon(R.drawable.ic_pin);
                    } else {
                        menuItem.setIcon(R.drawable.ic_unpin);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.play_screen_menu, menu);

        try {
            searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.setFilter(newText);
                    return false;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }
            });
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.menu = menu;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.brightness:

                int[] location = new int[2];
                p = new Point();
                p.x = location[0];
                p.y = location[1];
                showPopup(p);
                break;

            case R.id.colorPick:
                Utility.colorPickerDialog(mContext, view, fragment);
                break;

            case R.id.textSize:
                Utility.fontSizeDialog(mContext, view, fragment);
                break;


            case R.id.indicator:

                Utility.setmain("Main", "" + Constants.mainid, getActivity());
                Utility.setcategory("Category", "" + bookArrayList.get(selected).getCatId(), getActivity());
                Utility.setsubid("Subid", "" + selected, getActivity());
                item.setIcon(R.drawable.ic_pin);
                Toast.makeText(getActivity(), "Indicator set succesfully", Toast.LENGTH_LONG).show();
                break;


            case R.id.menu_resetDefault:
                Utility.setDefaultSetting(mContext, view, fragment);
                break;

            case R.id.bookmark:
                if (item.getTitle().equals(getString(R.string.unbook))) {
                    bookmarkDbHelper.insertIntoDB(bookArrayList.get(selected).getId());
                    item.setIcon(R.drawable.ic_bookmark);
                    item.setTitle(getString(R.string.bookmarked));



                } else if (item.getTitle().equals(getString(R.string.bookmarked))) {
                    bookmarkDbHelper.delete_id(bookArrayList.get(selected).getId());
                    item.setIcon(R.drawable.ic_unbook);
                    item.setTitle(getString(R.string.unbook));

                    //remove bookmark list from list when list fill with bookmark list
                    //other wise only change bookmark icon
                    if (Constants.category.equals("bookmark")) {
                        ListAdapter.bookArrayList.remove(selected);
                    }
                   adapter.notifyDataSetChanged();
                }
                break;

        }

        return super.onOptionsItemSelected(item);

    }*/


    private void showPopup(Point p) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getActivity())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getActivity().getPackageName()));
                getActivity().startActivityForResult(intent, 200);
            } else {
                    BrightnessPopup();
            }
        } else {
            BrightnessPopup();
        }
    }

    public void BrightnessPopup() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int popupWidth = size.x;

        int popupHeight = Utils.getToolbarHeight(getActivity());
        SeekBar seekBar;
        final PopupWindow popup = new PopupWindow(getActivity());

        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) getActivity().findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.seekbar, viewGroup);


        seekBar = (SeekBar) layout.findViewById(R.id.seekBar1);
        seekBar.setMax(255);
        Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        float curBrightnessValue = 0;
        try {
            curBrightnessValue = android.provider.Settings.System.getInt(getActivity().getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }

        int screen_brightness = (int) curBrightnessValue;

        seekBar.setProgress(screen_brightness);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue,
                                          boolean fromUser) {
                progress = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do something here,
                // if you want to do anything at the start of
                // touching the seekbar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                android.provider.Settings.System.putInt(getActivity().getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS,
                        progress);
            }
        });

        // Creating the PopupWindow

        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);


        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = 30;
        int OFFSET_Y = 30;

        // Clear the default translucent background
        //popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setBackgroundDrawable(new ShapeDrawable());

        // Displaying the popup at the specified location, + offsets.
        //popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x , p.y );

        popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);



        // Getting a reference to Close button, and close the popup when clicked.
    }


    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


}
