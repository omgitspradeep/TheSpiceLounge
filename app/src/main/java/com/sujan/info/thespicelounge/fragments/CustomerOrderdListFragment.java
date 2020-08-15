package com.sujan.info.thespicelounge.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.sujan.info.thespicelounge.Adapters.OrderedListAdapterCust;
import com.sujan.info.thespicelounge.CustomerFeedbackActivity;
import com.sujan.info.thespicelounge.MainActivity;
import com.sujan.info.thespicelounge.R;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.interfaceT.feedbackListener;
import com.sujan.info.thespicelounge.models.FoodDetails;
import com.sujan.info.thespicelounge.models.OrderDetail;
import com.sujan.info.thespicelounge.webservices.GetCustomerFeedbacks;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class CustomerOrderdListFragment extends Fragment implements OrderedListAdapterCust.FeedbackClickListener{

    MainActivity mContext;
    OrderedListAdapterCust orderedListAdapter;
    RecyclerView customerOrderRecyclerView;
    feedbackListener feedbackListener;
    ProgressBar progressBarFeedbacks;


    public CustomerOrderdListFragment(MainActivity context) {
        // Required empty public constructor
        mContext=context;
        feedbackListener=context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ordered_item_list_of_customer, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {

        progressBarFeedbacks=(ProgressBar)mContext.findViewById(R.id.progressBarFeedbacks);
        customerOrderRecyclerView= (RecyclerView)mContext.findViewById(R.id.customer_orders_list_recycler);
        customerOrderRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        orderedListAdapter = new OrderedListAdapterCust(mContext);
        orderedListAdapter.setFeedbackClickListener(this);
        customerOrderRecyclerView.setAdapter(orderedListAdapter);
    }



    @Override
    public void onFeedbackClick(View view, int position) {
        OrderDetail orderDetail=orderedListAdapter.getItem(position);
        if(orderDetail.getFoodStatus()==0){
            Utils.showAlertDialogBeforeCancelOrder(mContext,mContext,orderDetail.getTableNum(),orderDetail.getOrderID());

        }else {

            // Run api to get feedbacks
            progressBarFeedbacks.setVisibility(View.VISIBLE);
            new GetCustomerFeedbacks(mContext,orderedListAdapter.getItem(position).getItemId(),progressBarFeedbacks).execute();

        }
    }

}
