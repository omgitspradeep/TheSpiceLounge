package com.sujan.info.thespicelounge.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sujan.info.thespicelounge.CustomerFeedbackActivity;
import com.sujan.info.thespicelounge.R;
import com.sujan.info.thespicelounge.models.CustomerFeedback;

import java.util.ArrayList;

/**
 * Created by pradeep on 6/7/20.
 */

public class FeedbackListAdapter extends RecyclerView.Adapter<FeedbackListAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    ArrayList<CustomerFeedback> mCustomerFeedbackList;
    CustomerFeedbackActivity mContext;

    public FeedbackListAdapter(ArrayList<CustomerFeedback> customerFeedbackList, CustomerFeedbackActivity context) {
        this.mContext=context;
        this.mInflater = LayoutInflater.from(context);
        this.mCustomerFeedbackList=customerFeedbackList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_feedback_recycler, parent, false);
        return new FeedbackListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.feedback.setText(mCustomerFeedbackList.get(position).getFeedback());
        holder.name.setText(mCustomerFeedbackList.get(position).getCustName());
        holder.rating.setNumStars(mCustomerFeedbackList.get(position).getRatingstar());
    }

    @Override
    public int getItemCount() {
        return mCustomerFeedbackList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView feedback,name;
        RatingBar rating;
        public ViewHolder(View itemView) {
            super(itemView);
            feedback=(TextView)itemView.findViewById(R.id.feedback_desc);
            name=(TextView)itemView.findViewById(R.id.customer_name);
            rating=(RatingBar) itemView.findViewById(R.id.customer_rating_bar_);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
