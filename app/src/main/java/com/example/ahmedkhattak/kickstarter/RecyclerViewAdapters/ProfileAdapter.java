package com.example.ahmedkhattak.kickstarter.RecyclerViewAdapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ahmedkhattak.kickstarter.AppModels.Header;
import com.example.ahmedkhattak.kickstarter.AppModels.MyProject;
import com.example.ahmedkhattak.kickstarter.GlideGeneratedApi.GlideApp;
import com.example.ahmedkhattak.kickstarter.R;
import com.example.ahmedkhattak.kickstarter.AppModels.BackedProject;

import java.util.ArrayList;

/**
 * Created by Ahmed Khattak on 25/11/2017.
 */

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int profileObject = 0;
    private final int backedProjectObject = 1;
    private final int myProjectObject = 2;
    private final int header = 3;


    private ArrayList<Object> profileProjects;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Context context;


    public ProfileAdapter(ArrayList<Object> profileProjects, Context context) {
        this.profileProjects = profileProjects;
        this.context = context;
    }


    public void swap(ArrayList<Object> profileProjects) {
        if (this.profileProjects != null) {
            this.profileProjects.clear();
            this.profileProjects.addAll(profileProjects);
        } else {
            this.profileProjects = profileProjects;
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }


    public static class ProfileViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView profileImageView;
        TextView profileNameTextView;
        TextView profileEmailTextView;
        TextView profileBackedProjectsTextView;
        TextView profileDescriptionTextView;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            profileBackedProjectsTextView = itemView.findViewById(R.id.profileBackedProjectsTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            profileEmailTextView = itemView.findViewById(R.id.profileEmailTextView);
            profileNameTextView = itemView.findViewById(R.id.profileNameTextView);
            profileDescriptionTextView = itemView.findViewById(R.id.profileDescriptionTextView);
        }
    }


    public static class ProfileProjectListViewHolder extends RecyclerView.ViewHolder {


        AppCompatImageView projectImageView;
        TextView projectTitleTextView;
        TextView projectDescriptionTextView;

        public ProfileProjectListViewHolder(View itemView) {
            super(itemView);
            projectImageView = itemView.findViewById(R.id.projectImageView);
            projectDescriptionTextView = itemView.findViewById(R.id.projectDescriptionTextView);

            // For reducing displayed text


            projectTitleTextView = itemView.findViewById(R.id.projectTitleTextView);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView headerTextView;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            headerTextView = itemView.findViewById(R.id.headerTextView);
        }
    }


    @Override
    public int getItemViewType(int position) {

        if (profileProjects.get(position) instanceof com.example.ahmedkhattak.kickstarter.AppModels.Profile) {
            return profileObject;
        } else if (profileProjects.get(position) instanceof BackedProject) {
            return backedProjectObject;
        } else if (profileProjects.get(position) instanceof MyProject) {
            return myProjectObject;
        } else if (profileProjects.get(position) instanceof Header) {
            return header;
        }

        return -1;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {

            case profileObject:
                View profileView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_fragment_profile_list_item, parent, false);
                return new ProfileViewHolder(profileView);

            case backedProjectObject:
                View backedProjectView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fragment_project_list_item, parent, false);
                return new ProfileProjectListViewHolder(backedProjectView);

            case myProjectObject:
                View myProjectView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fragment_project_list_item, parent, false);
                return new ProfileProjectListViewHolder(myProjectView);

            case header:
                View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);
                return new HeaderViewHolder(headerView);


            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {

            case profileObject:
                com.example.ahmedkhattak.kickstarter.AppModels.Profile profile = (com.example.ahmedkhattak.kickstarter.AppModels.Profile) profileProjects.get(position);
                ProfileViewHolder profileViewHolder = (ProfileViewHolder) holder;
                profileViewHolder.profileNameTextView.setText(profile.getName());
                profileViewHolder.profileDescriptionTextView.setText(profile.getBiography());
                profileViewHolder.profileEmailTextView.setText(profile.getEmail());
                profileViewHolder.profileBackedProjectsTextView.setVisibility(View.GONE);
                GlideApp.with(context).load("http://ennovayt.com/" + profile.getImage()).placeholder(R.drawable.gradient).error(R.drawable.gradient).into(profileViewHolder.profileImageView);


                break;


            case backedProjectObject:
                BackedProject project = (BackedProject) profileProjects.get(position);
                ProfileProjectListViewHolder profileProjectListViewHolder = (ProfileProjectListViewHolder) holder;

                // For reducing displayed text
                profileProjectListViewHolder.projectDescriptionTextView.setLines(3);
                profileProjectListViewHolder.projectDescriptionTextView.setEllipsize(TextUtils.TruncateAt.END);
                profileProjectListViewHolder.projectDescriptionTextView.setText(project.getProjectDescription());

                profileProjectListViewHolder.projectTitleTextView.setText(project.getProjectTitle());

                GlideApp.with(context).load("http://ennovayt.com/" + project.getImage()).placeholder(R.drawable.gradient).error(R.drawable.gradient).into(profileProjectListViewHolder.projectImageView);

                break;

            case myProjectObject:
                MyProject myProject = (MyProject) profileProjects.get(position);
                ProfileProjectListViewHolder myProfileProjectListViewHolder = (ProfileProjectListViewHolder) holder;

                // For reducing displayed text
                myProfileProjectListViewHolder.projectDescriptionTextView.setLines(3);
                myProfileProjectListViewHolder.projectDescriptionTextView.setEllipsize(TextUtils.TruncateAt.END);
                myProfileProjectListViewHolder.projectDescriptionTextView.setText(myProject.getProjectDescription());


                myProfileProjectListViewHolder.projectTitleTextView.setText(myProject.getProjectTitle());

                GlideApp.with(context).load("http://ennovayt.com/" + myProject.getImage()).placeholder(R.drawable.gradient).error(R.drawable.gradient).into(myProfileProjectListViewHolder.projectImageView);
                break;

            case header:
                Header header = (Header) profileProjects.get(position);
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

                headerViewHolder.headerTextView.setText(header.getHeaderText());


            default:
                break;

        }

    }

    @Override
    public int getItemCount() {
        return profileProjects.size();
    }

}
