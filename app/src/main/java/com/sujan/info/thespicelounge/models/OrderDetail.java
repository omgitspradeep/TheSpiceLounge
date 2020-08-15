package com.sujan.info.thespicelounge.models;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by pradeep on 3/7/20.
 */

public class OrderDetail{

    int orderID,foodID,tableNum,quantity;
    int foodStatus;                   // 0,1,2,3 are waiting,cooking,cooked and served respectively.
    String custRequest;

    public OrderDetail() {
    }

    public OrderDetail(int orderID, int foodID, int tableNum, int quantity, int foodStatus, String custRequest) {
        this.orderID = orderID;
        this.foodID = foodID;
        this.tableNum = tableNum;
        this.quantity = quantity;
        this.foodStatus = foodStatus;
        this.custRequest = custRequest;
    }

    public String getCustRequest() {
        return custRequest;
    }

    public void setCustRequest(String custRequest) {
        this.custRequest = custRequest;
    }

    public int getTableNum() {
        return tableNum;
    }

    public void setTableNum(int tableID) {
        this.tableNum = tableID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getItemId() {
        return foodID;
    }

    public void setItemId(int itemId) {
        this.foodID = itemId;
    }

    public int getFoodStatus() {
        return foodStatus;
    }

    public void setFoodStatus(int status) {
        this.foodStatus = status;
    }


    public static Comparator<OrderDetail> statusComparator = new Comparator<OrderDetail>() {

        public int compare(OrderDetail o1, OrderDetail o2) {

            int status1 = o1.getFoodStatus();
            int status2 = o2.getFoodStatus();

	   /*For ascending order*/
            return status1-status2;

	   /*For descending order*/
            //rollno2-rollno1;
        }};




}
