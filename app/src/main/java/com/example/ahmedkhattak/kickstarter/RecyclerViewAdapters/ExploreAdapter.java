package com.example.ahmedkhattak.kickstarter.RecyclerViewAdapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ahmedkhattak.kickstarter.AppModels.Category;
import com.example.ahmedkhattak.kickstarter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed Khattak on 25/11/2017.
 */

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ExploreCategoryViewHolder>{




    private List<Category> categoryArrayList;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Context context;


    public ExploreAdapter(List<Category> categoryArrayList, Context context) {
        this.categoryArrayList = categoryArrayList;
        this.context = context;
    }

    public void swap(List<Category> categoryArrayList){
        this.categoryArrayList = categoryArrayList;

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }});
    }

    // view holders
    public static  class ExploreCategoryViewHolder extends RecyclerView.ViewHolder {

        public TextView categoryTextView;

        public ExploreCategoryViewHolder(View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
        }
    }



    @Override
    public ExploreCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View categoryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.explore_fragment_category_list,parent,false);
        return new ExploreCategoryViewHolder(categoryView);
    }

    @Override
    public void onBindViewHolder(ExploreCategoryViewHolder holder, int position) {

        Category categoryItem = categoryArrayList.get(position);
        ExploreCategoryViewHolder exploreCategoryViewHolder = holder;
        exploreCategoryViewHolder.categoryTextView.setText(categoryItem.getCategoryName());


    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }


}
