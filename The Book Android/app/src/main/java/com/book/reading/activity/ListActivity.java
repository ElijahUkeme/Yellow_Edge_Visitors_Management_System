package com.book.reading.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.book.reading.R;
import com.book.reading.fragment.ListFragment;
import com.book.reading.helper.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Objects;


public class ListActivity extends AppCompatActivity {

    public Fragment fragment = null;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    public Toolbar toolBar;
    public int id=0;
    //public String activity;
    public String actionBarTitle;
    public SharedPreferences preferences;
    public InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        try {
//            id = Integer.parseInt(b.getString("id"));
        }catch(Exception e){
            e.printStackTrace();
        }
       // setActionBarTitle();
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        init();
    }

   /* public void setActionBarTitle() {
        try {
            if (id == 0 || MainActivity.mainDbHelper == null) {
                getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            } else {

                preferences = PreferenceManager.getDefaultSharedPreferences(ListActivity.this);
                actionBarTitle = MainActivity.mainDbHelper.getActionBarTitle(id);

                //100 id we choose for bookmark
                if (id == 100) {
                    getSupportActionBar().setTitle("Bookmark List");
                } else {
                    getSupportActionBar().setTitle(actionBarTitle);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

    //Second page ........................................
    public void init() {

        fragmentManager = ListActivity.this.getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new ListFragment();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.invalidateOptionsMenu();

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.invalidateOptionsMenu();
        }
    }


   /* private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {


                case R.id.app_share:
                    try {
                        Intent shareAppIntent = new Intent(android.content.Intent.ACTION_SEND);
                        shareAppIntent.setType("text/plain");
                        shareAppIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getPackageName());
                        startActivity(Intent.createChooser(shareAppIntent, "Share via"));

                    } catch (Exception e) {
                        Log.e("Share error", e.getMessage());
                    }
                    break;
                case R.id.menu_rateus:
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                    startActivity(intent);
                    break;

            }

            return true;
        }
    };*/




    @Override
    protected void onResume() {
        super.onResume();

    }



    @Override
    public void onBackPressed() {

        super.onBackPressed();
        //clear filter
        Constants.filter = "";

            Intent intent = new Intent(ListActivity.this, MainActivity.class);
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();

         super.onBackPressed();


    }


}