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

import com.example.ahmedkhattak.kickstarter.AppModels.DataProject;
import com.example.ahmedkhattak.kickstarter.AppModels.Datum;
import com.example.ahmedkhattak.kickstarter.GlideGeneratedApi.GlideApp;
import com.example.ahmedkhattak.kickstarter.R;

/**
 * Created by Ahmed Khattak on 03/12/2017.
 */

public class ProjectTypeAdapter extends RecyclerView.Adapter<ProjectTypeAdapter.ProjectTypeViewHolder> {


    DataProject dataProject;



    Context context;

    public ProjectTypeAdapter(  DataProject dataProject ,Context context ){
        this.context = context;
        this.dataProject = dataProject;

    }

    public void swap( DataProject dataProject){
        this.dataProject = dataProject;

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }});
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static class ProjectTypeViewHolder extends RecyclerView.ViewHolder {

        public AppCompatImageView projectImageView;
        public TextView projectTitleTextView;
        public TextView projectDescriptionTextView;
        public ProjectTypeViewHolder(View itemView) {
            super(itemView);
            projectImageView = itemView.findViewById(R.id.projectImageView);
            projectDescriptionTextView = itemView.findViewById(R.id.projectDescriptionTextView);
            projectTitleTextView = itemView.findViewById(R.id.projectTitleTextView);
        }
    }



    @Override
    public ProjectTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View homeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fragment_project_list_item,parent,false);
        return new ProjectTypeViewHolder(homeView);
    }

    @Override
    public void onBindViewHolder(ProjectTypeViewHolder holder, int position) {


        Datum datum = dataProject.getData().get(position);
        holder.projectTitleTextView.setText(datum.getProjectTitle());
        holder.projectDescriptionTextView.setText(datum.getProjectDescription());
        GlideApp.with(context).load("http://ennovayt.com/" + datum.getImage()).placeholder(R.drawable.gradient).error(R.drawable.gradient).into(holder.projectImageView);

    }

    @Override
    public int getItemCount() {
        return dataProject.getData().size();
    }
}
