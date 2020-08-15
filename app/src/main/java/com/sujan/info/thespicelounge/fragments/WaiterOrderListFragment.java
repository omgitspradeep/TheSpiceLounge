package com.sujan.info.thespicelounge.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sujan.info.thespicelounge.Adapters.OrderedListAdapterWaiter;
import com.sujan.info.thespicelounge.R;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.WaiterActivity;

/**
 * Created by pradeep on 8/7/20.
 */

@SuppressLint("ValidFragment")
public class WaiterOrderListFragment extends android.support.v4.app.Fragment {
    WaiterActivity mContext;
    OrderedListAdapterWaiter orderedListAdapter;
    RecyclerView customerOrderRecyclerView;

    public WaiterOrderListFragment(WaiterActivity waiterActivity) {
        this.mContext=waiterActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.waiter_frag_order_list, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {

        customerOrderRecyclerView= (RecyclerView)mContext.findViewById(R.id.waiter_orders_list_recycler);
        customerOrderRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        orderedListAdapter = new OrderedListAdapterWaiter(mContext,ResourceManager.getAllOrders());
        customerOrderRecyclerView.setAdapter(orderedListAdapter);
    }



}

