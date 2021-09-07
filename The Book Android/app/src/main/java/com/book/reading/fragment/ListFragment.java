package com.book.reading.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.book.reading.activity.ListActivity;
import com.book.reading.activity.MainActivity;
import com.book.reading.activity.ReadingActivity;
import com.book.reading.model.Book;
import com.book.reading.DatabaseHelper.MainDBHelper;
import com.book.reading.R;
import com.book.reading.adapter.ListAdapter;
import com.book.reading.helper.Constants;
import com.book.reading.helper.Utility;

public class ListFragment extends Fragment {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layout_manager;
    public Fragment subFragment = null;
    public Menu menu;
    public static View view;
    private SharedPreferences preferences;
    public static int catId;


    //Database Objects................
    public MainDBHelper mainDbHelper = MainActivity.mainDbHelper;
    // Third Page......................................
    public int currentVisiblePosition = 0;
    public ArrayList<Book> bookArrayList;
    public SearchView searchView;
    public Activity mActivity;
    public ListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list, container, false);
        mActivity = getActivity();
        recyclerView = (RecyclerView) v.findViewById(R.id.list);
        layout_manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layout_manager);

        setHasOptionsMenu(true);
        this.view = v;
        Bundle b = getActivity().getIntent().getExtras();
//        catId = Integer.parseInt(b.getString("id"));

        init();
        return v;

    }


    public void init() {
        try {
            subFragment = ListFragment.this;
            bookArrayList = new ArrayList<>();

            bookArrayList = mainDbHelper.getAllHymns();

            listAdapter = new ListAdapter(getActivity(), bookArrayList);
            recyclerView.setAdapter(listAdapter);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    @Override
    public void onPause() {
        super.onPause();
        currentVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
    }





    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPosition((int) currentVisiblePosition);
        currentVisiblePosition = 0;

        //in Constant.filter is not empty  then onResume fragment show filterable list
        //other wise set all lst
        if (!Constants.filter.equals("")) {
            init();
            SearchFilter(Constants.filter, ListAdapter.bookArrayList);
        } else {
            init();
        }


    }

    // public  SearchView searchView;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.search_menu, menu);
        try {
            searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));

            //in Constant.filter is  empty  then search from all list
            if (Constants.filter.equals("")) {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextChange(String newText) {

                        //we will set search query in Constant.filter . So, we can highlight that word in list and display list
                        Constants.filter = newText;
                        SearchFilter(newText, bookArrayList);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }
                });
            }
            // if Constant.filter is not empty then adapter set with filter text
            else if (!Constants.filter.equals("")) {

                searchView.setIconified(false);
                searchView.setQuery(Constants.filter, true);
                // SearchFilter(Constants.filter, ListAdapter.bookArrayList);
                searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                    @Override
                    public boolean onClose() {
                        init();
                        return false;
                    }
                });

            }
            //if we search from all chapter . set search view always open
            //otherwise collapse search view.
            if (Constants.category.equals("all_search")) {
                searchView.setIconified(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //Write your logic here
            //finish();
            case android.R.id.home:

                Intent intent = new Intent(getActivity(), MainActivity.class);
                Constants.filter = "";
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.app_share:
                try {
                    Intent shareAppIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareAppIntent.setType("text/plain");
                    shareAppIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
                    startActivity(Intent.createChooser(shareAppIntent, "Share via"));

                } catch (Exception e) {
                    Log.e("Share error", e.getMessage());
                }
                break;
            case R.id.menu_rateus:
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName()));
                startActivity(intent1);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void SearchFilter(String str, ArrayList<Book> bookList) {
        try {
            if (bookList != null) {
                final ArrayList<Book> filteredModelList = filter(bookList, str);

                ListAdapter listAdapter = new ListAdapter(mActivity, filteredModelList);
                listAdapter.setFilter(filteredModelList, str);
                recyclerView.setAdapter(listAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Book> filter(ArrayList<Book> models, String query) {
        query = query.toLowerCase();
        final ArrayList<Book> filteredModelList = new ArrayList<>();
        for (Book model : models) {
            final String text = model.getTitle().toLowerCase();
            final String des = model.getDescription().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            } else if (des.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}

