package com.sujan.info.thespicelounge.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sujan.info.thespicelounge.Adapters.OrderedListAdapterCashier;
import com.sujan.info.thespicelounge.R;
import com.sujan.info.thespicelounge.CashierActivity;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.models.OrderDetail;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class CashierOrderFragment extends Fragment{


    RecyclerView recyclerView;
    ArrayList<OrderDetail> orderDetailArrayList;
    OrderedListAdapterCashier orderedListAdapterCashier;
    TextView totalAmount;
    Button checkOut;
    int tableNumber;
    CashierActivity mContext;


    @SuppressLint("ValidFragment")
    public CashierOrderFragment(CashierActivity context, ArrayList<OrderDetail> orderDetailArrayList, int tableNum) {
        this.orderDetailArrayList=orderDetailArrayList;
        this.tableNumber=tableNum;
        this.mContext=context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cashier_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

    }

    private void init(View view) {
        mContext.setFabForRefreshInvisible();
        totalAmount=(TextView)view.findViewById(R.id.cashier_total_amount);
        checkOut=(Button)view.findViewById(R.id.checkout_cashier);
        recyclerView=(RecyclerView)view.findViewById(R.id.ordered_items_recycler_cashier);
        orderedListAdapterCashier=new OrderedListAdapterCashier(mContext,orderDetailArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(orderedListAdapterCashier);

    }


    @SuppressLint("SetTextI18n")
    public void updateTotalPriceAndCheckout(int totalAmt, boolean canBeCheckout){
        totalAmount.setText(totalAmt+"");
        if (canBeCheckout){
            checkOut.setVisibility(View.VISIBLE);
            checkOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Utils.showAlertDialogBeforeCheckout(mContext,mContext,tableNumber);
                }
            });

        }else {
            checkOut.setVisibility(View.INVISIBLE);
        }
    }

}