package com.sujan.info.thespicelounge.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sujan.info.thespicelounge.ChefActivity;
import com.sujan.info.thespicelounge.R;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.models.FoodDetails;
import com.sujan.info.thespicelounge.models.OrderDetail;
import com.sujan.info.thespicelounge.webservices.ChangeOrderStatus;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pradeep on 9/7/20.
 */

public class OrderedListAdapterChef extends RecyclerView.Adapter<OrderedListAdapterChef.ViewHolder> {

    ChefActivity context;
    private LayoutInflater mInflater;
    ArrayList<OrderDetail> orderDetailsList;



    public OrderedListAdapterChef(ChefActivity context,ArrayList<OrderDetail> orderDetailsList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.orderDetailsList=orderDetailsList;
    }

    @Override
    public OrderedListAdapterChef.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.order_item_recycler_element_chef, parent, false);
        return new OrderedListAdapterChef.ViewHolder(view);
    }



   /* java.lang.NullPointerException:
    Attempt to invoke virtual method 'void android.widget.TextView.setText(java.lang.CharSequence)' on a null object reference*/


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        init(holder,position);

    }

    @SuppressLint("SetTextI18n")
    private void init(ViewHolder holder, int position) {
        OrderDetail od=orderDetailsList.get(position);
        int foodId=od.getItemId();
        FoodDetails fd= ResourceManager.getFoodDetails(foodId);

        int orderStatus=od.getFoodStatus();
        String custRequest=od.getCustRequest();
        holder.sNoChef.setText(position+1+"");
        holder.quantity.setText(od.getQuantity()+" ");
        holder.chefOrderItem.setText(fd.getFullItemName());
        holder.chefOrderTime.setText(fd.getPrepareTimeinMins()+"");
        if(!custRequest.equals("")){
            holder.custRequest.setText("Request: "+custRequest);
        }

        changeColor(orderStatus,holder.chefFoodStatus);
        holder.chefFoodStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chef can click on waiting and cooking only
                if(orderStatus==0 || orderStatus == 1){
                    context.getProgressBarChef().setVisibility(View.VISIBLE);
                    new ChangeOrderStatus(context,context,od.getOrderID(),orderStatus).execute();
                }
            }
        });
    }

    private void changeColor(int orderStatus, Button chefFoodStatus) {
        switch (orderStatus){
            case 0:
                chefFoodStatus.setText("waiting");
                chefFoodStatus.setTextColor(Color.parseColor("#4B0082"));
                chefFoodStatus.setClickable(true);
                break;

            case 1:
                chefFoodStatus.setText("cooking");
                chefFoodStatus.setTextColor(Color.parseColor("#0000FF"));
                chefFoodStatus.setClickable(true);
                break;

            case 2:
                chefFoodStatus.setText("cooked");
                chefFoodStatus.setTextColor(Color.parseColor("#FF7F00"));

                break;

            case 3:
                chefFoodStatus.setText("served");
                chefFoodStatus.setTextColor(Color.parseColor("#006400"));
                break;

        }
    }

    @Override
    public int getItemCount() {
        return orderDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView sNoChef,quantity, chefOrderItem,chefOrderTime,custRequest;
        Button chefFoodStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            sNoChef=(TextView)itemView.findViewById(R.id.sNo_chef);
            quantity=(TextView)itemView.findViewById(R.id.chef_order_qty);
            chefOrderItem=(TextView)itemView.findViewById(R.id.chef_ordered_item);
            chefOrderTime=(TextView)itemView.findViewById(R.id.chef_order_time);
            chefFoodStatus=(Button) itemView.findViewById(R.id.chef_status_button);
            custRequest=(TextView) itemView.findViewById(R.id.customer_request_message);

        }

    }
}
