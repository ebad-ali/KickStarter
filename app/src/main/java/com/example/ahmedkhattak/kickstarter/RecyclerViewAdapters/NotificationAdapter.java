package com.example.ahmedkhattak.kickstarter.RecyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedkhattak.kickstarter.Activities.IndividualNotificationsActivity;
import com.example.ahmedkhattak.kickstarter.AppModels.Notification;
import com.example.ahmedkhattak.kickstarter.R;

import java.util.List;

/**
 * Created by Ebad Ali on 4/12/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {


    private List<Object> notificationList;

    private Context context;


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public NotificationAdapter(List<Object> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context = context;


    }

    public void swap(List<Object> notificationList) {
        if (this.notificationList != null) {
            this.notificationList.clear();
            this.notificationList.addAll(notificationList);
        } else {
            this.notificationList = notificationList;
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }


    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        public TextView backUserNameTextView, projectNameTextView, amountTextView;
        public RelativeLayout relativeLayout;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            backUserNameTextView = itemView.findViewById(R.id.userNameTextView);
            projectNameTextView = itemView.findViewById(R.id.projectNameTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            relativeLayout  = itemView.findViewById(R.id.relativeLayout);


        }
    }


    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View categoryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_fragment_list_item, parent, false);
        return new NotificationViewHolder(categoryView);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {

        final Notification notificationItem = (com.example.ahmedkhattak.kickstarter.AppModels.Notification) notificationList.get(position);
        NotificationViewHolder notificationViewHolder = holder;
        notificationViewHolder.backUserNameTextView.setText(notificationItem.getName());
        notificationViewHolder.projectNameTextView.setText(notificationItem.getProjectTitle());
        notificationViewHolder.amountTextView.setText(notificationItem.getAmount() + " PKR");

        notificationViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   Toast.makeText(context,notificationItem.getAmount(),Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context, IndividualNotificationsActivity.class);
                intent.putExtra("project_title",notificationItem.getProjectTitle());

                intent.putExtra("project_id",notificationItem.getProjectID());
                intent.putExtra("backer_id",notificationItem.getBackerID());
                intent.putExtra("author_id",notificationItem.getAuthorID());
                intent.putExtra("amount_id",notificationItem.getAmount());
                intent.putExtra("temp_back_id",notificationItem.getTempBackID());


                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return notificationList.size();
    }


}
