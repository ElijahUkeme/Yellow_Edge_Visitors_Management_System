package com.book.reading.activity;

import android.app.Activity;
import android.content.DialogInterface;
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
import com.book.reading.model.Highlight;

import java.util.ArrayList;

public class CurrentNoteActivity extends AppCompatActivity {
    public Toolbar toolBar;
    private RecyclerView recyclerView;
    public RecyclerView.LayoutManager layout_manager;
    //Database...............
    public ArrayList<Highlight> introList;
    public NoteListAdapter adapter;
    public int chapter_id;
    public TextView emptyMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_note);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Notes");
        chapter_id = getIntent().getIntExtra("chapter_id", 0);
        introList = new ArrayList<>();
        introList = MainActivity.highlightDBHelper.getNoteByChapterId(chapter_id);
        emptyMsg = (TextView) findViewById(R.id.emptyMsg);
        if (introList.size() == 0) {
            emptyMsg.setVisibility(View.VISIBLE);
        } else {
            emptyMsg.setVisibility(View.GONE);
        }
        adapter = new NoteListAdapter(introList, this);

        recyclerView = (RecyclerView) findViewById(R.id.my_list);
        layout_manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout_manager);
        recyclerView.setAdapter(adapter);
    }

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
            return new NoteListAdapter.NoteHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            NoteListAdapter.NoteHolder noteHolder = (NoteListAdapter.NoteHolder) holder;
            final Highlight note = noteList.get(position);


            noteHolder.no.setText("" + (position + 1));
            noteHolder.no.setTextSize(18);
            noteHolder.v_title.setText(note.getWords());
            noteHolder.n_title.setText(note.getNote());

            noteHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                    alert.setTitle("Delete note");
                    alert.setMessage("Are you sure you want to delete a note?");
                    alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            MainActivity.highlightDBHelper.delete_note(note.getChapterId(), note.getIndex());
                            noteList.remove(position);
                            adapter.notifyDataSetChanged();
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
