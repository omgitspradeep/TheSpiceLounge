package com.sujan.info.thespicelounge.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sujan.info.thespicelounge.R;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.WaiterActivity;
import com.sujan.info.thespicelounge.models.FoodDetails;
import com.sujan.info.thespicelounge.models.OrderDetail;
import com.sujan.info.thespicelounge.webservices.ChangeOrderStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pradeep on 8/7/20.
 */

public class OrderedListAdapterWaiter extends RecyclerView.Adapter<OrderedListAdapterWaiter.ViewHolder> {


    ArrayList<OrderDetail> orderDetailList;
    WaiterActivity context;
    private LayoutInflater mInflater;
    HashMap<Integer,FoodDetails> foodDetailsHashMap;

    public OrderedListAdapterWaiter(WaiterActivity mContext, ArrayList<OrderDetail> orderDetailsList) {
        this.mInflater = LayoutInflater.from(mContext);
        this.context=mContext;
        this.orderDetailList=orderDetailsList;
        this.foodDetailsHashMap=ResourceManager.getGlobalFoodMenuMap();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.order_item_recycler_element_waiter, parent, false);
        return new OrderedListAdapterWaiter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final OrderDetail orderDetail=orderDetailList.get(position);

        int foodId=orderDetail.getItemId();
        FoodDetails foodDetails=foodDetailsHashMap.get(foodId);

        final int orderStatus=orderDetail.getFoodStatus();
        holder.sNo.setText(position+1+"");
        holder.qty.setText(orderDetail.getQuantity()+"");
        holder.waiterOrderItem.setText(foodDetails.getFullItemName());
        Log.e("HEHEHE",""+foodDetails.getFullItemName());
        holder.waiterorderPrice.setText(foodDetails.getPrice()+"");
        changeColor(orderStatus,holder.waiterFoodStatus);
        holder.waiterFoodStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderStatus==2){
                  //  context.getWaiterProgressBar().setVisibility(View.VISIBLE);
                    new ChangeOrderStatus(context,context,orderDetail.getOrderID(),orderStatus).execute();
                }
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void changeColor(int orderStatus, Button waiterFoodStatus) {
        switch (orderStatus){
            case 0:
               waiterFoodStatus.setText("waiting");
               waiterFoodStatus.setTextColor(Color.parseColor("#4B0082"));
               break;

            case 1:
                waiterFoodStatus.setText("cooking");
                waiterFoodStatus.setTextColor(Color.parseColor("#0000FF"));
                break;

            case 2:
                waiterFoodStatus.setText("cooked");
                waiterFoodStatus.setTextColor(Color.parseColor("#FF7F00"));
                waiterFoodStatus.setClickable(true);

                break;

            case 3:
                waiterFoodStatus.setText("served");
                waiterFoodStatus.setTextColor(Color.parseColor("#006400"));
                break;

        }
    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView sNo,qty, waiterOrderItem,waiterorderPrice;
        Button waiterFoodStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            sNo=(TextView)itemView.findViewById(R.id.sNo_waiter);
            qty=(TextView)itemView.findViewById(R.id.waiter_order_qty);
            waiterOrderItem=(TextView)itemView.findViewById(R.id.waiter_ordered_item);
            waiterorderPrice=(TextView)itemView.findViewById(R.id.waiter_order_price);
            waiterFoodStatus=(Button) itemView.findViewById(R.id.waiter_status_button);
        }


    }
}
