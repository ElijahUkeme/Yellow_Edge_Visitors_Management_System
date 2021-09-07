package com.book.reading.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.book.reading.R;
import com.book.reading.adapter.ListAdapter;
import com.book.reading.helper.Constants;
import com.book.reading.DatabaseHelper.HighlightDBHelper;
import com.book.reading.model.Book;
import com.book.reading.model.Highlight;

import java.util.ArrayList;

public class NoteAddActivity extends AppCompatActivity {


    public EditText addNote;
    public static HighlightDBHelper highlightDBHelper;
    public int cat_id, chapter_id, index;
    public String word, note, exist_note;
    public Toolbar toolbar;
    public ArrayList<Highlight> noteList;
    public ArrayList<Book> chapterList;
    public String activity;
    public RecyclerView recyclerView, recyclerViewColor;
    public RecyclerView.LayoutManager layout_manager, vertical_layoutManager;
    public ListAdapter adapter;
    public static TextView titleWord;
    public ColorAdapter colorAdapter;
    public String evalue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Note");
        highlightDBHelper = MainActivity.highlightDBHelper;

        cat_id = getIntent().getIntExtra("cat_id", 0);
        chapter_id = getIntent().getIntExtra("chapter_id", 0);
        index = getIntent().getIntExtra("index", 0);
        word = getIntent().getStringExtra("word");
        activity = getIntent().getStringExtra("activity");

        System.out.println("wor:"+word+"=="+cat_id+"=="+chapter_id+"=="+activity);

        chapterList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerViewColor = (RecyclerView) findViewById(R.id.recyclerViewColor);
        layout_manager = new LinearLayoutManager(NoteAddActivity.this);
        vertical_layoutManager = new LinearLayoutManager(NoteAddActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout_manager);
        recyclerViewColor.setLayoutManager(vertical_layoutManager);
        colorAdapter = new ColorAdapter(Constants.colorArray, NoteAddActivity.this, index, chapter_id);
        recyclerViewColor.setAdapter(colorAdapter);
        addNote = (EditText) findViewById(R.id.add_note);
        titleWord = (TextView) findViewById(R.id.word);
        titleWord.setText(word);
        exist_note = highlightDBHelper.GetNoteByIndex(chapter_id, index);
        addNote.setText(exist_note);

        addNote.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                evalue = "1";
                return false;
            }
        });

        if (exist_note.equals("")) {
            recyclerViewColor.setVisibility(View.GONE);
        } else {
            recyclerViewColor.setVisibility(View.VISIBLE);
        }
        init();

    }

    public void init() {
        recyclerView.setVisibility(View.VISIBLE);
        chapterList = MainActivity.mainDbHelper.get_Bookmarked(chapter_id);
        adapter = new ListAdapter(NoteAddActivity.this, chapterList);
        recyclerView.setAdapter(adapter);
    }

    public void SaveNote() {
        note = addNote.getText().toString();
        if (exist_note.equals("")) {

            if (!note.isEmpty()) {
                highlightDBHelper.insertNoteIntoDB(cat_id, chapter_id, index, note, word);
                finish();
            } else {
                Toast.makeText(NoteAddActivity.this, getString(R.string.note_empty_msg), Toast.LENGTH_SHORT).show();
            }
        } else {

            if (!note.isEmpty()) {
                highlightDBHelper.UpdateNote(chapter_id, index, note);
                finish();
            } else {
                Toast.makeText(NoteAddActivity.this, getString(R.string.note_empty_msg), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class ColorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public String[] colorArray;
        public Activity activity;
        public int index;
        public int chapter_id;

        public ColorAdapter(String[] colorArray, Activity activity, int index, int chapter_id) {
            this.colorArray = colorArray;
            this.activity = activity;
            this.index = index;
            this.chapter_id = chapter_id;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_sheet_color_layout, parent, false);
            return new ColorAdapter.ColorViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            ColorViewHolder colorViewHolder = (ColorViewHolder) holder;
            colorViewHolder.colorImg.setBackgroundColor(Color.parseColor(colorArray[position]));
            colorViewHolder.colorImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    titleWord.setTextColor(Color.parseColor(colorArray[position]));
                    MainActivity.highlightDBHelper.UpdateNoteColor(chapter_id, index, "" + position);


                }
            });
        }

        @Override
        public int getItemCount() {
            return colorArray.length;
        }


        public class ColorViewHolder extends RecyclerView.ViewHolder {
            public ImageView colorImg;

            public ColorViewHolder(View itemView) {
                super(itemView);
                colorImg = (ImageView) itemView.findViewById(R.id.colorImg);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.save_note:
                SaveNote();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        init();

        super.onResume();

    }

    @Override
    public void onBackPressed() {


        note = addNote.getText().toString();
        if (!evalue.equals("")) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Save Note");
            alert.setMessage("Are you  want to save a note?");
            alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    note = addNote.getText().toString();
                    if (exist_note.equals("")) {
                        //  highlightDBHelper.insertIntoDB(v_note, vId);
                        finish();

                    } else {
                        highlightDBHelper.UpdateNote(chapter_id, index, note);
                        finish();
                    }

                }
            });
            alert.setNegativeButton("no", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // close dialog
                    dialog.cancel();
                    finish();
                }
            });
            alert.show();
        } else {
            super.onBackPressed();
        }
    }
}
