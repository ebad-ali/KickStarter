package com.example.ahmedkhattak.kickstarter.RecyclerViewAdapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmedkhattak.kickstarter.Activities.IndividualCategoryActivity;
import com.example.ahmedkhattak.kickstarter.AppModels.IndCategory;
import com.example.ahmedkhattak.kickstarter.AppModels.IndividualCategory;
import com.example.ahmedkhattak.kickstarter.GlideGeneratedApi.GlideApp;
import com.example.ahmedkhattak.kickstarter.R;

import java.util.List;

/**
 * Created by Ahmed Khattak on 03/12/2017.
 */

public class IndividualCategoryAdapter extends RecyclerView.Adapter<IndividualCategoryAdapter.IndCategoryProjectViewHolder>  {


    IndividualCategory individualCategory;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    Context context;

    public IndividualCategoryAdapter(IndividualCategory individualCategory,  Context context ){
        this.context = context;
        this.individualCategory = individualCategory;

    }


    public void swap(IndividualCategory individualCategory){
        this.individualCategory = individualCategory;

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }});
    }


    public static class IndCategoryProjectViewHolder extends RecyclerView.ViewHolder {

        public AppCompatImageView projectImageView;
        public TextView projectTitleTextView;
        public TextView projectDescriptionTextView;

        public IndCategoryProjectViewHolder(View itemView) {
            super(itemView);
            projectImageView = itemView.findViewById(R.id.projectImageView);
            projectDescriptionTextView = itemView.findViewById(R.id.projectDescriptionTextView);
            projectTitleTextView = itemView.findViewById(R.id.projectTitleTextView);
        }
    }

    @Override
    public IndCategoryProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View homeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fragment_project_list_item,parent,false);
        return new IndCategoryProjectViewHolder(homeView);
    }

    @Override
    public void onBindViewHolder(IndCategoryProjectViewHolder holder, int position) {

        IndCategory indCategory = individualCategory.getIndCategory().get(position);
        holder.projectTitleTextView.setText(indCategory.getProjectTitle());
        holder.projectDescriptionTextView.setText(indCategory.getProjectDescription());
        GlideApp.with(context).load("http://ennovayt.com/" + indCategory.getImage()).placeholder(R.drawable.gradient).error(R.drawable.gradient).into(holder.projectImageView);

    }

    @Override
    public int getItemCount() {
        return individualCategory.getIndCategory().size();
    }
}
