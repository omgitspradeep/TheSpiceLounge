package com.sujan.info.thespicelounge.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sujan.info.thespicelounge.R;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.models.FoodDetails;
import com.sujan.info.thespicelounge.models.OrderDetail;
import com.sujan.info.thespicelounge.models.TableInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pradeep on 3/7/20.
 */

public class OrderedListAdapterCust extends RecyclerView.Adapter<OrderedListAdapterCust.ViewHolder> {

    Activity context;
    private LayoutInflater mInflater;
    FeedbackClickListener mFeedbackClickListener;

    HashMap<Integer,TableInformation> tableInformationHashMap;   // To get TableInformation with the help of "tableNum" as key
    TableInformation tableInformation;                           // TableInformation received from "tableInformationHashMap"
    ArrayList<OrderDetail> orderDetailArrayList;             // List of ordered foods received from "tableInformation"
    HashMap<Integer,FoodDetails> foodDetailsHashMap;         // To get FoodDetails with the help of "foodID "

    public interface FeedbackClickListener {
        void onFeedbackClick(View view, int position);
    }


    public OrderedListAdapterCust(Activity mContext) {
        this.mInflater = LayoutInflater.from(mContext);
        this.context=mContext;


        tableInformationHashMap=ResourceManager.getGlobalTableInformationMap();
        tableInformation=tableInformationHashMap.get(ResourceManager.getSelectedTable());
        orderDetailArrayList=tableInformation.getOrderedFoods();
        foodDetailsHashMap=ResourceManager.getGlobalFoodMenuMap();

    }

    @Override
    public OrderedListAdapterCust.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.order_item_recycler_element_cust, parent, false);
        return new OrderedListAdapterCust.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(OrderedListAdapterCust.ViewHolder holder, int position) {

        OrderDetail orderDetail=orderDetailArrayList.get(position);
        int foodID=orderDetail.getItemId();


        FoodDetails foodDetails= (FoodDetails) foodDetailsHashMap.get(foodID);

        holder.custOrderItem.setText(foodDetails.getFullItemName());
        holder.orderPrice.setText(foodDetails.getPrice()+"");

        holder.orderStatus.setText(ResourceManager.getOrderStatus(orderDetail.getFoodStatus()));
        int orderStatus=orderDetail.getFoodStatus();
        holder.qty.setText(orderDetail.getQuantity()+"");
        if (displayFeedbackButton(orderStatus)){
            holder.feedback.setVisibility(View.VISIBLE);
            holder.feedback.setClickable(true);
        }else {
            holder.feedback.setVisibility(View.INVISIBLE);

            // If food is still waiting user can cancel the order.
            if(orderStatus==0){
                holder.feedback.setVisibility(View.VISIBLE);
                holder.feedback.setClickable(true);
                holder.feedback.setImageResource(R.drawable.delete);
            }
        }
    }

    private boolean displayFeedbackButton(int status) {
        boolean isServed=false;
        switch (status){
            case 0:
                isServed= false;
                break;
            case 1:
                isServed= false;
                break;
            case 2:
                isServed= false;
                break;
            case 3:
                isServed= true;
                break;
        }
        return isServed;
    }



    @Override
    public int getItemCount() {
        return orderDetailArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView qty, custOrderItem,orderStatus,orderPrice;
        ImageButton feedback;
        public ViewHolder(View itemView) {
            super(itemView);

            qty=(TextView)itemView.findViewById(R.id.qty);
            custOrderItem=(TextView)itemView.findViewById(R.id.cust_ordered_item);
            orderStatus=(TextView)itemView.findViewById(R.id.cust_order_status);
            orderPrice=(TextView)itemView.findViewById(R.id.cust_order_price);
            feedback=(ImageButton) itemView.findViewById(R.id.customer_feedback_button);
            feedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Utils.isConnected(context)){
                        if (mFeedbackClickListener != null) mFeedbackClickListener.onFeedbackClick(v, getAdapterPosition());

                    }
                }
            });
        }


        @Override
        public void onClick(View v) {
        }
    }

    public void setFeedbackClickListener(FeedbackClickListener feedbackClickListener) {
        this.mFeedbackClickListener = feedbackClickListener;
    }

    public OrderDetail getItem(int id) {
        return orderDetailArrayList.get(id);
    }

}
