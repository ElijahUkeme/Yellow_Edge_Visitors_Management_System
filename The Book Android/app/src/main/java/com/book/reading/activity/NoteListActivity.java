package com.book.reading.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.book.reading.R;
import com.book.reading.DatabaseHelper.HighlightDBHelper;
import com.book.reading.model.Highlight;

import java.util.ArrayList;

public class NoteListActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layout_manager;
    public NoteListAdapter adapter;
    public ArrayList<Highlight> noteList;
    public static HighlightDBHelper highlightDBHelper;
    public SharedPreferences preferences;
    public TextView emptyMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("All Notes");
        highlightDBHelper = MainActivity.highlightDBHelper;
        emptyMsg = (TextView) findViewById(R.id.emptyMsg);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layout_manager = new LinearLayoutManager(NoteListActivity.this);
        recyclerView.setLayoutManager(layout_manager);
        noteList = new ArrayList<>();
        init();

    }

    public void init() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        noteList = highlightDBHelper.getAllNote();


        if (noteList.size() == 0) {
            emptyMsg.setVisibility(View.VISIBLE);
        }
        adapter = new NoteListAdapter(noteList, NoteListActivity.this);
        recyclerView.setAdapter(adapter);
    }

    /*
     * noteList Adapter
     */
    public class NoteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public ArrayList<Highlight> noteList;
        public Activity activity;


        public NoteListAdapter(ArrayList<Highlight> noteList, Activity activity) {
            this.noteList = noteList;
            this.activity = activity;
        }

        public class NoteHolder extends RecyclerView.ViewHolder {
            TextView v_title, n_title, no;
            CardView cardView;
            ImageView delete;

            public NoteHolder(View itemView) {
                super(itemView);
                v_title = (TextView) itemView.findViewById(R.id.v_title);
                n_title = (TextView) itemView.findViewById(R.id.n_title);
                no = (TextView) itemView.findViewById(R.id.no);
                cardView = (CardView) itemView.findViewById(R.id.cardView);
                delete = (ImageView) itemView.findViewById(R.id.delete);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notelist_layout, parent, false);
            return new NoteHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            NoteHolder noteHolder = (NoteHolder) holder;
            final Highlight note = noteList.get(position);

            noteHolder.v_title.setText(note.getV_title());
            noteHolder.no.setText(String.valueOf(position + 1));
            noteHolder.no.setBackgroundResource(R.drawable.txt_bg);
            noteHolder.no.setTextColor(Color.WHITE);
            noteHolder.no.setPadding(10, 5, 10, 5);
            noteHolder.n_title.setText(note.getNote());
            noteHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(NoteListActivity.this, NoteAddActivity.class);
                    intent.putExtra("cat_id", Integer.parseInt(note.getV_id()));
                    intent.putExtra("chapter_id",note.getChapterId());
                    intent.putExtra("index",note.getIndex());
                    intent.putExtra("word",note.getWords());
                    intent.putExtra("activity", "list");
                    startActivity(intent);
                }
            });
            noteHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                    alert.setTitle("Delete note");
                    alert.setMessage("Are you sure you want to delete a note?");
                    alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //delete nite from table
                            highlightDBHelper.delete_note(note.getChapterId(),note.getIndex());
                            noteList.remove(position);
                            adapter.notifyDataSetChanged();
                            notifyDataSetChanged();
                        }
                    });
                    alert.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // close dialog
                            dialog.cancel();
                        }
                    });
                    alert.show();

                }
            });

        }

        @Override
        public int getItemCount() {
            return noteList.size();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
