package com.book.reading.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.book.reading.R;
import com.book.reading.fragment.ReadingFragment;
import com.book.reading.helper.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Objects;

public class ReadingActivity extends AppCompatActivity {

    int id=0;
    public String actionBarTitle;
    public static SeekBar seekBar;

    public static ImageView playPause;
    public Fragment fragment = null;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    public static Toolbar toolBar;
    public static FloatingActionButton noteAddFab, bookmarkFabButton;

    public InterstitialAd mInterstitialAd;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle b = getIntent().getExtras();
        try {
            id = Integer.parseInt(b.getString("catId"));
        }catch(Exception e){
            e.printStackTrace();
        }
        setActionBarTitle();
        init();


      /*  playPause = (ImageView) findViewById(R.id.tgbtnScrollingSpeed);
        seekBar = (SeekBar) findViewById(R.id.seekbarScrollSpeed);
        noteAddFab = (FloatingActionButton) findViewById(R.id.fab);
        bookmarkFabButton = (FloatingActionButton) findViewById(R.id.bm_list_fab);

        //here we check if Constant.category is bookmark then we hide bookmark fab icon .
        if (Constants.category.equals("bookmark")) {
            //bookmarkFabButton.setVisibility(View.GONE);
            findViewById(R.id.bm_list_fab).setVisibility(View.GONE);
        }
        bookmarkFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.subList = true;
                Intent intent = new Intent(ReadingActivity.this, ListActivity.class);
                //here we choose random id to identify bookmarkList
                //choose id which is not include in category table as category id
                intent.putExtra("id", "100");
                intent.putExtra("title", "Bookmark List");
                Constants.category = "bookmark";

                startActivity(intent);


            }
        });*/

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }

    public void setActionBarTitle() {
        try {
            if (id == 0 || MainActivity.mainDbHelper == null) {
                getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            } else {
                actionBarTitle = MainActivity.mainDbHelper.getActionBarTitle(id);
                if (Constants.category.equals("bookmark")) {
                    getSupportActionBar().setTitle("Bookmark List");
                } else {
                    getSupportActionBar().setTitle(actionBarTitle);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void init() {

        fragmentManager = ReadingActivity.this.getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new ReadingFragment();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //Write your logic here
            //finish();
            case android.R.id.home:

//                onBackPressed();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //clear filter
        Constants.filter = "";
        super.onBackPressed();

        Intent intent = new Intent(ReadingActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();

        super.onBackPressed();

    }
}
