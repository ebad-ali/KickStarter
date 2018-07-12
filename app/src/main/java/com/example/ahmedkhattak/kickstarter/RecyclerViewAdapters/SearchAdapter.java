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

import com.example.ahmedkhattak.kickstarter.AppModels.ByContentCreators.ByContentCreators;
import com.example.ahmedkhattak.kickstarter.AppModels.ByProject.ByProject;
import com.example.ahmedkhattak.kickstarter.AppModels.SearchByFunders.ByFunders;
import com.example.ahmedkhattak.kickstarter.AppModels.SearchByName.ByName;
import com.example.ahmedkhattak.kickstarter.AppModels.SearchByName.SearchResult;
import com.example.ahmedkhattak.kickstarter.Enums.SearchType;
import com.example.ahmedkhattak.kickstarter.GlideGeneratedApi.GlideApp;
import com.example.ahmedkhattak.kickstarter.R;

import java.util.ArrayList;

/**
 * Created by Ahmed Khattak on 21/12/2017.
 */

public class SearchAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{

    SearchType searchType;
    Context context;
    private ArrayList<Object> searchResultList;

    private final int byName = SearchType.ByName.ordinal();
    private final int byProject = SearchType.ByProject.ordinal();
    private final int byContentCreators = SearchType.ByContentCreators.ordinal();
    private final int byFunders = SearchType.ByFunders.ordinal();


    public SearchAdapter(ArrayList<Object> searchResultList, Context context, SearchType searchType) {

        this.context = context;
        this.searchType = searchType;
        this.searchResultList = searchResultList;
    }


    public void swap(ArrayList<Object> searchResultList){
        if (this.searchResultList != null) {
            this.searchResultList.clear();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }});
            this.searchResultList.addAll(searchResultList);
        }
        else {
            this.searchResultList = searchResultList;
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }});
    }


    public void setSearchType(SearchType searchType){
        this.searchType = searchType;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }




    public static class ByNameViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView imageView;
        TextView nameTextView;
        TextView companyTextView;
        TextView mailTextView;
        TextView detailTextView;

        public ByNameViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            companyTextView = itemView.findViewById(R.id.companyTextView);
            mailTextView = itemView.findViewById(R.id.mailTextView);
            detailTextView = itemView.findViewById(R.id.detailTextView);
        }
    }

    public static class ByFundersViewHolder extends  RecyclerView.ViewHolder {

        AppCompatImageView imageView;
        TextView nameTextView;
        TextView projectTitleTextView;
        TextView fundedTextView;




        public ByFundersViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            projectTitleTextView = itemView.findViewById(R.id.projectTitleTextView);
            fundedTextView = itemView.findViewById(R.id.fundedTextView);

        }
    }

    public static class  ByProjectViewHolder extends  RecyclerView.ViewHolder {

        AppCompatImageView projectImageView;
        TextView projectTitleTextView;
        TextView fundedTextView;
        TextView plegedTextView;
        TextView projectDescriptionTextView;



        public ByProjectViewHolder(View itemView) {
            super(itemView);
            projectImageView = itemView.findViewById(R.id.projectImageView);
            projectTitleTextView = itemView.findViewById(R.id.projectTitleTextView);
            fundedTextView = itemView.findViewById(R.id.fundedTextView);
            plegedTextView = itemView.findViewById(R.id.plegedTextView);
            projectDescriptionTextView = itemView.findViewById(R.id.projectDescriptionTextView);
        }
    }

    public static class  ByContentCreatorsViewHolder extends  RecyclerView.ViewHolder {

        AppCompatImageView imageView;
        TextView projectTitleTextView;
        TextView projectAuthorNameTextView;

        TextView detailTextView;

        public ByContentCreatorsViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            projectTitleTextView = itemView.findViewById(R.id.projectTitleTextView);
            projectAuthorNameTextView = itemView.findViewById(R.id.projectAuthorNameTextView);

            detailTextView = itemView.findViewById(R.id.detailTextView);
        }

    }



    @Override
    public int getItemViewType(int position) {

        if (searchResultList.get(position) instanceof SearchResult) {
            return byName;
        } else if (searchResultList.get(position) instanceof com.example.ahmedkhattak.kickstarter.AppModels.SearchByFunders.SearchResult) {
             return byFunders;
        }
        else if (searchResultList.get(position) instanceof com.example.ahmedkhattak.kickstarter.AppModels.ByContentCreators.SearchResult) {
            return byContentCreators;
        }
        else if (searchResultList.get(position) instanceof com.example.ahmedkhattak.kickstarter.AppModels.ByProject.SearchResult) {
            return byProject;
        }

        return -1; //should not return -1 under any circumstance


    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        switch (viewType){
//
//
//            case 0:
//                View ByNameView = LayoutInflater.from(getContext()).inflate(R.layout.search_byname,parent,false);
//                return new ByNameViewHolder(ByNameView);
//
//
//            case 1:
//                View byFundersView = LayoutInflater.from(getContext()).inflate(R.layout.search_byfunders,parent,false);
//                return new ByFundersViewHolder(byFundersView);
//
//
//            case 2:
//                View byProjectView = LayoutInflater.from(getContext()).inflate(R.layout.search_byproject,parent,false);
//                return new ByProjectViewHolder(byProjectView);
//
//
//            case 3:
//                View byContentCreatorsView = LayoutInflater.from(getContext()).inflate(R.layout.search_bycontentcreators,parent,false);
//                return new ByContentCreatorsViewHolder(byContentCreatorsView);
//
//
//            default:
//                return null;
//
//
//        }


        if (viewType == byName) {
            View ByNameView = LayoutInflater.from(getContext()).inflate(R.layout.search_byname,parent,false);
            return new ByNameViewHolder(ByNameView);
        } else if (viewType == byFunders) {
            View byFundersView = LayoutInflater.from(getContext()).inflate(R.layout.search_byfunders,parent,false);
            return new ByFundersViewHolder(byFundersView);
        } else if (viewType == byProject) {
            View byProjectView = LayoutInflater.from(getContext()).inflate(R.layout.search_byproject,parent,false);
            return new ByProjectViewHolder(byProjectView);
        } else if (viewType == byContentCreators) {
            View byContentCreatorsView = LayoutInflater.from(getContext()).inflate(R.layout.search_bycontentcreators,parent,false);
            return new ByContentCreatorsViewHolder(byContentCreatorsView);
        } else {
            return  null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (searchType){
            case ByName:

                SearchResult searchResultByName = (SearchResult) searchResultList.get(position);
                ByNameViewHolder byNameViewHolder = (ByNameViewHolder)holder;
                byNameViewHolder.companyTextView.setText(searchResultByName.getCompany());
                byNameViewHolder.detailTextView.setText(searchResultByName.getBiography());
                byNameViewHolder.mailTextView.setText(searchResultByName.getEmail());
                byNameViewHolder.nameTextView.setText(searchResultByName.getName());
                GlideApp.with(context).load("http://ennovayt.com/" + searchResultByName.getImage()).placeholder(R.drawable.gradient).error(R.drawable.gradient).into(byNameViewHolder.imageView);
                break;


            case ByFunders:
                com.example.ahmedkhattak.kickstarter.AppModels.SearchByFunders.SearchResult searchResultByFunders = (com.example.ahmedkhattak.kickstarter.AppModels.SearchByFunders.SearchResult) searchResultList.get(position);
                ByFundersViewHolder byFundersViewHolder = (ByFundersViewHolder)holder;

                byFundersViewHolder.fundedTextView.setText(searchResultByFunders.getAmount());
                byFundersViewHolder.nameTextView.setText(searchResultByFunders.getName());
                byFundersViewHolder.projectTitleTextView.setText(searchResultByFunders.getProjectTitle());

                GlideApp.with(context).load("http://ennovayt.com/" + searchResultByFunders.getImage()).placeholder(R.drawable.gradient).error(R.drawable.gradient).into(byFundersViewHolder.imageView);
                break;



            case ByProject:

                com.example.ahmedkhattak.kickstarter.AppModels.ByProject.SearchResult searchResultByProject =  (com.example.ahmedkhattak.kickstarter.AppModels.ByProject.SearchResult)searchResultList.get(position);
                ByProjectViewHolder byProjectViewHolder = (ByProjectViewHolder)holder;

                byProjectViewHolder.projectTitleTextView.setText(searchResultByProject.getProjectTitle());
                byProjectViewHolder.fundedTextView.setText(searchResultByProject.getFundTarget());
                byProjectViewHolder.plegedTextView.setVisibility(View.GONE);
                byProjectViewHolder.projectDescriptionTextView.setText(searchResultByProject.getProjectDescription());


                GlideApp.with(context).load("http://ennovayt.com/" + searchResultByProject.getImage()).placeholder(R.drawable.gradient).error(R.drawable.gradient).into(byProjectViewHolder.projectImageView);
                 break;


            case ByContentCreators:
                com.example.ahmedkhattak.kickstarter.AppModels.ByContentCreators.SearchResult searchResultByContentCreators  =  (com.example.ahmedkhattak.kickstarter.AppModels.ByContentCreators.SearchResult)searchResultList.get(position);
                ByContentCreatorsViewHolder byContentCreatorsViewHolder = (ByContentCreatorsViewHolder)holder;

                byContentCreatorsViewHolder.detailTextView.setText(searchResultByContentCreators.getProjectDescription());
                byContentCreatorsViewHolder.projectAuthorNameTextView.setText(searchResultByContentCreators.getName());
                byContentCreatorsViewHolder.projectTitleTextView.setText(searchResultByContentCreators.getProjectTitle());

                GlideApp.with(context).load("http://ennovayt.com/" + searchResultByContentCreators.getImage()).placeholder(R.drawable.gradient).error(R.drawable.gradient).into(byContentCreatorsViewHolder.imageView);


                break;



            default:



        }
    }

    @Override
    public int getItemCount() {
        return searchResultList.size();
    }







}
