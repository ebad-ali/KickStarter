package com.example.ahmedkhattak.kickstarter.RecyclerViewAdapters;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ahmedkhattak.kickstarter.GlideGeneratedApi.GlideApp;
import com.example.ahmedkhattak.kickstarter.Network.NetworkConfig;
import com.example.ahmedkhattak.kickstarter.R;
import com.example.ahmedkhattak.kickstarter.AppModels.Hero;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ahmed Khattak on 25/11/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "HomeAdapter";

    private final int heroObject = 0;
    private final int projectObject = 1;


    private ArrayList<Object> homePageProjectsList;



    private Context context;

    public HomeAdapter(ArrayList<Object> homePageProjectsList, Context context) {
        this.homePageProjectsList = homePageProjectsList;
    }


    public void swap(ArrayList<Object> homePageProjectsList){
        if (this.homePageProjectsList != null) {
            this.homePageProjectsList.clear();
            this.homePageProjectsList.addAll(homePageProjectsList);
        }
        else {
            this.homePageProjectsList = homePageProjectsList;
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }});
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }


    // Views

    public static class HomeHeroViewHolder extends RecyclerView.ViewHolder {




        public HomeHeroViewHolder(View itemView) {
            super(itemView);

        }
    }


    public static class HomeProjectViewHolder extends  RecyclerView.ViewHolder {

        public AppCompatImageView projectImageView;
        public TextView projectTitleTextView;
        public TextView projectDescriptionTextView;

        public HomeProjectViewHolder(View itemView) {
            super(itemView);

            projectImageView = itemView.findViewById(R.id.projectImageView);
            projectDescriptionTextView = itemView.findViewById(R.id.projectDescriptionTextView);
            projectTitleTextView = itemView.findViewById(R.id.projectTitleTextView);
        }
    }


    // Recyler View specifics


    @Override
    public int getItemViewType(int position) {

        if (homePageProjectsList.get(position) instanceof Hero) {
            return heroObject;
        } else if (homePageProjectsList.get(position) instanceof com.example.ahmedkhattak.kickstarter.AppModels.Project) {
            return projectObject;
        }

        return -1; //should not return -1 under any circumstance


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {

            case 0:
                View homeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fragment_hero_list_item,parent,false);
                return new HomeHeroViewHolder(homeView);

            default:
                View projectView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fragment_project_list_item,parent,false);
                return  new HomeProjectViewHolder(projectView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){

            case heroObject:
                break;


            case projectObject:
                com.example.ahmedkhattak.kickstarter.AppModels.Project project = (com.example.ahmedkhattak.kickstarter.AppModels.Project) homePageProjectsList.get(position);
                HomeProjectViewHolder homeProjectViewHolder = (HomeProjectViewHolder)holder;
                homeProjectViewHolder.projectTitleTextView.setText(project.getProjectTitle());

                // For reducing displayed text
                homeProjectViewHolder.projectDescriptionTextView.setLines(3);
                homeProjectViewHolder.projectDescriptionTextView.setEllipsize(TextUtils.TruncateAt.END);

                homeProjectViewHolder.projectDescriptionTextView.setText(project.getProjectDescription());

                // TODO fix crash here
                URL url = null;
                try {
                     url = new URL(project.getImage());
                }catch (Exception x){
                    //nope
                    Log.d(TAG, "onBindViewHolder: " + x.getLocalizedMessage());
                }

                    GlideApp.with(context).load("http://ennovayt.com/" + project.getImage()).placeholder(R.drawable.gradient).error(R.drawable.gradient).into(homeProjectViewHolder.projectImageView);

                break;

            default:
                break;

        }

    }

    @Override
    public int getItemCount() {
        return homePageProjectsList.size();
    }


}
