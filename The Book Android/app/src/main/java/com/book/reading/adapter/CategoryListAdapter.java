package com.book.reading.adapter;

import android.app.Activity;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.book.reading.R;
import com.book.reading.activity.ListActivity;
import com.book.reading.model.Category;
import com.book.reading.helper.Constants;

import java.util.ArrayList;

public class CategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static ArrayList<Category> categoryArrayList;
    private Activity activity;

    public CategoryListAdapter(Activity activity, ArrayList<Category> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
        final Category category = categoryArrayList.get(position);


        categoryViewHolder.txt_eng.setText(category.getTitle());
        categoryViewHolder.innerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ListActivity.class);
                Constants.mainid = category.getCategoryId();
                intent.putExtra("id", "" + category.getCategoryId());
                intent.putExtra("title", "" + category.getTitle());
                Constants.category = "" + category.getCategoryId();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        LinearLayout innerLayout;
        TextView txt_eng;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            txt_eng = (TextView) itemView.findViewById(R.id.cat_id);
            innerLayout = (LinearLayout) itemView.findViewById(R.id.inner_layout);
        }
    }
}
