package com.book.reading.adapter;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import android.widget.SeekBar;
import android.widget.TextView;


import com.book.reading.helper.OnSelectListener;
import com.book.reading.helper.SelectableTextHelper;
import com.book.reading.activity.CurrentNoteActivity;
import com.book.reading.activity.NoteAddActivity;
import com.book.reading.activity.MainActivity;
import com.book.reading.activity.ReadingActivity;
import com.book.reading.fragment.ReadingFragment;
import com.book.reading.model.Book;
import com.book.reading.R;
import com.book.reading.helper.Constants;
import com.book.reading.helper.Utility;
import com.book.reading.model.Highlight;

import java.text.Normalizer;
import java.util.ArrayList;


import static com.book.reading.activity.ReadingActivity.seekBar;
import static com.book.reading.activity.ReadingActivity.playPause;

public class ReadingAdapter extends PagerAdapter {


    public static Activity activity;
    public static ArrayList<Book> chapterList;
    private LayoutInflater inflater;
    private TextView tvTitle;
    public static NestedScrollView scrollView;
    public LinearLayout linearLayout;
    public static TextView tvDescription;
    private static boolean isMoveingSeekBar = false;
    private String search, searchHighlight;
    private String title, description;
    public static Spannable ssb;
    public static int cat_id, id;
    public static BottomSheetDialog bottomSheetDialog;

    public ReadingAdapter(Activity activity, ArrayList<Book> chapterList) {

        this.activity = activity;
        this.chapterList = chapterList;
        this.search = Constants.filter;
        this.inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View instantiateItem(ViewGroup container, final int position) {


        final View view = inflater.inflate(R.layout.view_pager_layout, container, false);
        tvDescription = (TextView) view.findViewById(R.id.tvDisplayDetails);
        tvTitle = (TextView) view.findViewById(R.id.play_title);
        scrollView = (NestedScrollView) view.findViewById(R.id.vertical_scrollview_id);
        scrollView.setTag("play");
        linearLayout = (LinearLayout) view.findViewById(R.id.vertical_outer_layout_id);

       /* playPause.setTag("play");
        playPause.setImageResource(R.drawable.ic_play);
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chapterList.size() != 0) {
                    String tag = playPause.getTag().toString();
                    String tag1 = scrollView.getTag().toString();

                    if (tag.equalsIgnoreCase("play") && tag1.equalsIgnoreCase("play")) {
                        scrollView = (NestedScrollView) view.findViewWithTag(scrollView.getTag().toString());
                        Utility.startAutoScrolling(activity, (NestedScrollView) ReadingFragment.view.findViewById(R.id.vertical_scrollview_id));
                        playPause.setImageResource(R.drawable.ic_pause);
                        playPause.setTag("pause");
                        scrollView.setTag("pause");
                    } else {

                        Utility.stopAutoScrolling();
                        playPause.setImageResource(R.drawable.ic_play);
                        playPause.setTag("play");
                        scrollView.setTag("play");
                    }
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isMoveingSeekBar = false;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isMoveingSeekBar = true;

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (chapterList.size() != 0) {
                    if (isMoveingSeekBar) {

                        Utility.startAutoScrolling(activity, (NestedScrollView) ReadingFragment.view.findViewById(R.id.vertical_scrollview_id));
                        Constants.SPEED = progress / 20;
                        if (Utility.scrollTimer != null) {
                            playPause.setImageResource(R.drawable.ic_pause);
                        }
                        scrollView.setTag("pause");
                    }
                }
            }
        });

        //SelectableHelper is custom class which is help us to mark,underline selected text
        SelectableTextHelper mSelectableTextHelper = new SelectableTextHelper.Builder(tvDescription)
                .setSelectedColor(activity.getResources().getColor(R.color.selected_blue))//selected text color
                .setCursorHandleSizeInDp(20)//cursor size
                .setCursorHandleColor(activity.getResources().getColor(R.color.cursor_handle_color))//cursor color
                .build();
        mSelectableTextHelper.setSelectListener(new OnSelectListener() {
            @Override
            public void onTextSelected(CharSequence content) {


            }
        });*/

        title = MainActivity.mainDbHelper.getTitle(chapterList.get(ReadingFragment.pager.getCurrentItem()).getId());
        description = MainActivity.mainDbHelper.getDescription(chapterList.get(position).getId());
        cat_id = chapterList.get(ReadingFragment.pager.getCurrentItem()).getCatId();
        id = chapterList.get(ReadingFragment.pager.getCurrentItem()).getId();
        tvTitle.setText(title);
        Highlight(description);
        SearchHightLightText(description);


        //method  call when change textSize or color
        Utility.setCurrentSetting(tvDescription, tvTitle, activity);

       /* ReadingActivity.noteAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, CurrentNoteActivity.class);
                intent.putExtra("chapter_id", chapterList.get(ReadingFragment.pager.getCurrentItem()).getId());
                activity.startActivity(intent);

            }
        });*/

        ViewTreeObserver vTreeObserver = linearLayout.getViewTreeObserver();

        vTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Utility.getScrollMaxAmount(linearLayout);
            }
        });

        ((ViewPager) container).addView(view);
        return view;
    }

    public void SearchHightLightText(String originalText) {
        if (searchHighlight != null && !searchHighlight.equalsIgnoreCase("")) {
            String normalizedText = Normalizer.normalize(originalText, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
            int start = normalizedText.indexOf(searchHighlight);
            if (start < 0) {
                // return originalText;
            } else {

                while (start >= 0) {
                    int spanStart = Math.min(start, originalText.length());
                    int spanEnd = Math.min(start + searchHighlight.length(), originalText.length());

                    ssb.setSpan(new BackgroundColorSpan(activity.getResources().getColor(R.color.colorPrimary)), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssb.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.off_white)), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = normalizedText.indexOf(searchHighlight, spanEnd);
                }
                tvDescription.setText(ssb);
            }
        }
    }


    public static void NoteWord(final int start, int end) {
        try {

            tvDescription = (TextView) ReadingFragment.view.findViewById(R.id.tvDisplayDetails);
            final String s = tvDescription.getText().toString().substring(start, end);
            //start is index of selected word
            //end is selected word'd last index
            //here we parse word , word index, chapter id, category id to note add activity to create not on that word
            Intent intent = new Intent(activity, NoteAddActivity.class);
            System.out.println("word   " + s);
            intent.putExtra("word", s);
            intent.putExtra("index", (start));
            intent.putExtra("cat_id", cat_id);
            intent.putExtra("chapter_id", chapterList.get(ReadingFragment.pager.getCurrentItem()).getId());
            activity.startActivity(intent);
            Highlight(chapterList.get(ReadingFragment.pager.getCurrentItem()).getDescription());
        } catch (IllegalStateException e) {
            tvDescription.clearFocus();
        }

    }

    public static void MarkWord(int start, int end) {
        try {
            //start is index of selected word
            //end is selected word'd last index

            tvDescription = (TextView) ReadingFragment.view.findViewById(R.id.tvDisplayDetails);

            //ForGround color span apply between start and end index of text
            ssb.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.off_white)), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            //BackGround color  span apply between start and end index of text background
            ssb.setSpan(new BackgroundColorSpan(activity.getResources().getColor(R.color.colorPrimary)), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            //get word from String between start and end index
            final String s = tvDescription.getText().toString().substring(start, end);

            //here we insert mark word in database
            //we add category id, current chapter id, start index of word, and selected word
            MainActivity.highlightDBHelper.insertMarkWordIntoDB(cat_id, chapterList.get(ReadingFragment.pager.getCurrentItem()).getId(), start, s);

            //after save word in database we have show marked word in text view
            //highlight method set mark span in textview
            Highlight(chapterList.get(ReadingFragment.pager.getCurrentItem()).getDescription());

        } catch (IllegalStateException e) {
            tvDescription.clearFocus();

        }
    }

    public static void UnderlineWord(int start, int end) {
        try {
            //start is index of selected word
            //end is selected word'd last index
            tvDescription = (TextView) ReadingFragment.view.findViewById(R.id.tvDisplayDetails);

            //Underline  span apply between start and end index of text
            ssb.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            //get word from String between start and end index
            final String s = tvDescription.getText().toString().substring(start, end);

            //here we insert underline word in database
            //we add category id, current chapter id, start index of word, and selected word
            MainActivity.highlightDBHelper.insertUnderlineWordIntoDB(cat_id, chapterList.get(ReadingFragment.pager.getCurrentItem()).getId(), start, s);

            //after save word in database we have show underline word in text view
            //highlight method set underline span in textview
            Highlight(chapterList.get(ReadingFragment.pager.getCurrentItem()).getDescription());

        } catch (IllegalStateException e) {
            tvDescription.clearFocus();

        }
    }


    public static void Highlight(final String description) {

        ArrayList<Highlight> noteArrayList, markArrayList, underLineArrayList;

        //get current chapter's note list from note table
        noteArrayList = MainActivity.highlightDBHelper.getNoteByChapterId(chapterList.get(ReadingFragment.pager.getCurrentItem()).getId());

        //get current chapter's markWord list from mark table
        markArrayList = MainActivity.highlightDBHelper.getMarkByChapterId(chapterList.get(ReadingFragment.pager.getCurrentItem()).getId());

        //get current chapter's underline word list from underline table
        underLineArrayList = MainActivity.highlightDBHelper.getUnderlineByChapterId(chapterList.get(ReadingFragment.pager.getCurrentItem()).getId());

        //current chapter id
        final int id = chapterList.get(ReadingFragment.pager.getCurrentItem()).getId();


        //initialise Spannable String
        ssb = new SpannableString(description);

        //for loop for span notList word
        for (final Highlight note : noteArrayList) {

            //index of  word
            final int index = note.getIndex();

            //when note's custom color is null,then we use default note drawable
            //other wise set custom drawable icon
            Drawable drawable;
            if (note.getCustomColor() == null) {
                drawable = activity.getResources().getDrawable(R.drawable.ic_note1);
            } else {
                drawable = activity.getResources().getDrawable(Constants.colorDrawable[Integer.parseInt(note.getCustomColor())]);
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
            ssb.setSpan(imageSpan, (index + note.getWords().length()), (index + note.getWords().length() + 1), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            //to click note image span  we have to use clickable span
            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {

                    // when click span call ,we pass required data to AddNoteActivity to save or edit note
                    Intent intent = new Intent(activity, NoteAddActivity.class);
                    intent.putExtra("word", note.getWords());
                    intent.putExtra("index", index);
                    intent.putExtra("cat_id", cat_id);
                    intent.putExtra("chapter_id", id);
                    intent.putExtra("activity", "add");
                    activity.startActivity(intent);

                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);


                }
            }, (index + note.getWords().length()), (index + note.getWords().length() + 1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        }

        //for loop for span markList word
        for (final Highlight mark : markArrayList) {

            //index of mark word
            final int pos = mark.getIndex();

            //change marked word color and remove mark we use clickable span
            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {

                    //in method bottomSheetColorDialog we pass color array, click method as mark and marked word
                    BottomSheetColorDialog(Constants.colorArray, pos, "mark", mark.getWords());

                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);

                    //this color apply to text color
                    ds.setColor(Color.parseColor("#FFFFFF"));

                }
            }, pos, (pos + mark.getWords().length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            //when custom color is null,then we use colorPrimary As default color
            //other wise set custom color in marked word
            if (mark.getCustomColor() == null) {
                ssb.setSpan(new BackgroundColorSpan(activity.getResources().getColor(R.color.colorPrimary)), pos, (pos + mark.getWords().length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                ssb.setSpan(new BackgroundColorSpan(Color.parseColor(mark.getCustomColor())), pos, (pos + mark.getWords().length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }

        //for loop for span underline List word
        for (final Highlight note : underLineArrayList) {
            //index of word
            final int pos = note.getIndex();
            //change underline word color and remove underline we use clickable span
            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {

                    //in method bottomSheetColorDialog we pass color array, click method as underline and underlineword
                    BottomSheetColorDialog(Constants.colorArray, pos, "underline", note.getWords());

                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    //when custom color is null,then we use black As default color
                    //other wise set custom color in underline word
                    if (note.getCustomColor() == null) {
                        ds.setColor(Color.BLACK);
                    } else {
                        ds.setColor(Color.parseColor(note.getCustomColor()));
                    }

                }
            }, pos, (pos + note.getWords().length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            //set underline text to textView
            ssb.setSpan(new UnderlineSpan(), pos, (pos + note.getWords().length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        }

        //setText method call after all spannable method
        tvDescription.setText(ssb);

        //setMovementMethod use when we use clickable span
        //if you didn't call this method your clickable span method not work
        tvDescription.setMovementMethod(LinkMovementMethod.getInstance());
    }


    /*
     * BottomSheetColorDialog to show bottom sheet dialog
     */
    public static void BottomSheetColorDialog(String[] colorArray, final int index, final String clickedMethod, final String word) {


        bottomSheetDialog = new BottomSheetDialog(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_color_view, null);
        bottomSheetDialog.setContentView(view);
        final TextView changeColor = (TextView) view.findViewById(R.id.changeColor);
        ImageView removeMark = (ImageView) view.findViewById(R.id.remove);


        //remove mark or underline
        removeMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if clickMethod  is mark we remove that marked word from table
                if (clickedMethod.equals("mark")) {

                    MainActivity.highlightDBHelper.delete_mark(id, index);
                    tvDescription = (TextView) ReadingFragment.view.findViewById(R.id.tvDisplayDetails);
                    Highlight(chapterList.get(ReadingFragment.pager.getCurrentItem()).getDescription());

                } else if (clickedMethod.equals("underline")) {
                    //if clickMethod  is under we remove that underline word from table
                    MainActivity.highlightDBHelper.delete_underline(id, index);
                    tvDescription = (TextView) ReadingFragment.view.findViewById(R.id.tvDisplayDetails);
                    Highlight(chapterList.get(ReadingFragment.pager.getCurrentItem()).getDescription());

                }

                //dismiss bottom dialog
                bottomSheetDialog.dismiss();
            }
        });

        if (clickedMethod.equals("mark")) {
            changeColor.setText("Change Mark Color");
        } else if (clickedMethod.equals("underline")) {
            changeColor.setText("Change Underline Color");
        }


        //recycler view for show custom color inside
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layout_manager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layout_manager);

        //ColorAdapter for recyclerView
        ColorAdapter adapter = new ColorAdapter(colorArray, activity, index, clickedMethod);
        recyclerView.setAdapter(adapter);

        //show bottomSheetDialog
        bottomSheetDialog.show();
    }

    public static class ColorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public String[] colorArray;
        public Activity activity;
        public int index;
        public String clickedMethod;

        public ColorAdapter(String[] colorArray, Activity activity, int index, String clickedMethod) {
            this.colorArray = colorArray;
            this.activity = activity;
            this.index = index;
            this.clickedMethod = clickedMethod;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_sheet_color_layout, parent, false);
            return new ColorViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            ColorViewHolder colorViewHolder = (ColorViewHolder) holder;
            colorViewHolder.colorImg.setBackgroundColor(Color.parseColor(colorArray[position]));
            colorViewHolder.colorImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    switch (clickedMethod) {

                        case "mark":
                            tvDescription = (TextView) ReadingFragment.view.findViewById(R.id.tvDisplayDetails);
                            MainActivity.highlightDBHelper.UpdateMarkColor(id, index, colorArray[position]);
                            Highlight(chapterList.get(ReadingFragment.pager.getCurrentItem()).getDescription());
                            break;
                        case "underline":
                            tvDescription = (TextView) ReadingFragment.view.findViewById(R.id.tvDisplayDetails);
                            MainActivity.highlightDBHelper.UpdateUnderlineColor(id, index, colorArray[position]);
                            Highlight(chapterList.get(ReadingFragment.pager.getCurrentItem()).getDescription());
                            break;
                    }

                    bottomSheetDialog.dismiss();

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


    // Get searchable from search view
    public void setFilter(String search) {
        this.searchHighlight = search.toString().toLowerCase();
        notifyDataSetChanged();
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getCount() {
        return chapterList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);

    }


}
