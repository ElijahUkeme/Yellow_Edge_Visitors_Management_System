package com.book.reading.helper;

import java.util.HashMap;

import android.graphics.Color;

import com.book.reading.R;

public class Constants {

    // Media controller

    public static long DELAY = 113;
    public static double SPEED = 1.0;

    public static boolean subList = false;

    // preferences strings
    public static final String PREF_NAME = "";
    public static final String PREF_TEXTSIZE = "fontSizePref";
    public static final String PREF_TEXTCOLOR = "textColorPref";


    public static int radio;

    //max text size
    public static final String TEXTSIZE_MAX = "35";

    //minimum text size
    public static final String TEXTSIZE_MIN = "18";

    //default color of textView
    public static final int TEXT_DEF_COLOR = Color.BLACK;
    public static boolean status = false;

    public static int id;
    public static HashMap<Integer, String> ids = new HashMap<Integer, String>();

    //activity_main id value
    public static int mainid = 0;

    //default value of searchView query
    public static String filter = "";

    //we will identify category keyword for all_search and and note
    public static String category;


    //public static String filterTitle;


    public static String[] colorArray = {"#880E4F", "#4A148C", "#311B92",
            "#01579B", "#006064", "#004D40"};


    public static Integer[] colorDrawable = {R.drawable.ic_note_0,
            R.drawable.ic_note_1,
            R.drawable.ic_note_2,
            R.drawable.ic_note_3,
            R.drawable.ic_note_4,
            R.drawable.ic_note_5};

}
