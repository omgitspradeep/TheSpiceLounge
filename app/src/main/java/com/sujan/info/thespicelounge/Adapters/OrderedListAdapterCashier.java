package com.sujan.info.thespicelounge.Adapters;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sujan.info.thespicelounge.R;
import com.sujan.info.thespicelounge.CashierActivity;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.models.FoodDetails;
import com.sujan.info.thespicelounge.models.OrderDetail;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pradeep on 10/7/20.
 */

public class OrderedListAdapterCashier extends RecyclerView.Adapter<OrderedListAdapterCashier.ViewHolder>{

    LayoutInflater mInflator;
    CashierActivity mContext;
    ArrayList<OrderDetail> orderDetailArrayList;
    int totalAmount=0;
    boolean areAllFoodItemsServed=true;
    onOrderedListInCashierUpdated afterUpdatationInterface;
    HashMap<Integer,FoodDetails> foodDetailsHM;

    public interface onOrderedListInCashierUpdated{
        void onCashierListUpdated(int totalAmt,boolean canBeCheckout);
    }

    public OrderedListAdapterCashier(CashierActivity context, ArrayList<OrderDetail> orderDetailArrayList) {
        this.mContext = context;
        this.afterUpdatationInterface=context;
        this.mInflator=LayoutInflater.from(context);
        this.orderDetailArrayList = orderDetailArrayList;
        this.foodDetailsHM=ResourceManager.getGlobalFoodMenuMap();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= mInflator.inflate(R.layout.ordered_item_recycler_element_cashier,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderDetail orderDetail=orderDetailArrayList.get(position);
        int foodID=orderDetail.getItemId();

        FoodDetails foodDetailsArrayList= foodDetailsHM.get(foodID);


        int orderStatus=orderDetail.getFoodStatus();
        int quantity=orderDetail.getQuantity();
        int orderPrice=foodDetailsArrayList.getPrice();
        int tableNum=orderDetail.getTableNum();
        String foodName= foodDetailsArrayList.getFullItemName();

        Log.e("FOODNAME","\n"+foodName);

        if(orderStatus!=0){
            totalAmount=totalAmount+(orderPrice*quantity);
            Log.e("TotalAMT",totalAmount+"    :    "+orderPrice*quantity);
        }

        if(orderStatus!=3){
            areAllFoodItemsServed=false;
        }
        holder.sNo.setText(position+1+"");
        holder.quantity.setText(quantity+"");
        holder.orderedItem.setText(foodName);
        holder.orderStatus.setText(ResourceManager.getOrderStatus(orderStatus));
        holder.orderPrice.setText(orderPrice+"");

        // Total price should be updated once the last item's price is calculated

        if((position+1)==getItemCount()) {
           if(afterUpdatationInterface!=null){
               afterUpdatationInterface.onCashierListUpdated(totalAmount,areAllFoodItemsServed);
           }
        }
    }

    @Override
    public int getItemCount() {
        return orderDetailArrayList.size();
    }

    public int getTotalAmount() {
        return totalAmount;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView quantity, orderedItem,orderStatus,orderPrice,sNo;

        public ViewHolder(View itemView) {
            super(itemView);
            sNo=(itemView).findViewById(R.id.sNo_cashier_title);
            quantity=(TextView)itemView.findViewById(R.id.quantity_cashier);
            orderedItem=(TextView)itemView.findViewById(R.id.item_cashier);
            orderStatus=(TextView)itemView.findViewById(R.id.food_status_cashier);
            orderPrice=(TextView) itemView.findViewById(R.id.price_item_cashier);
        }
    }


}
