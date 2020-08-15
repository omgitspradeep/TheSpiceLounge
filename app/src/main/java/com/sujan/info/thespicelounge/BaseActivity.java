package com.sujan.info.thespicelounge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.interfaceT.MenuItemsListener;
import com.sujan.info.thespicelounge.interfaceT.allOrderedListListener;
import com.sujan.info.thespicelounge.interfaceT.cashierCheckoutListener;
import com.sujan.info.thespicelounge.interfaceT.menuItemAddUpdateListener;
import com.sujan.info.thespicelounge.interfaceT.orderListWithTableListener;
import com.sujan.info.thespicelounge.models.FoodDetails;
import com.sujan.info.thespicelounge.models.OrderDetail;
import com.sujan.info.thespicelounge.models.TableInformation;
import com.sujan.info.thespicelounge.webservices.GetMenuItems;
import com.sujan.info.thespicelounge.webservices.GetOrderListWithTable;

import org.json.JSONArray;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity implements MenuItemsListener,allOrderedListListener,orderListWithTableListener,menuItemAddUpdateListener,cashierCheckoutListener {

    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*// Download menuitems from internet only when chef makes changes in menu or when there is no local file that have menu items.
        if(ResourceManager.isMenuFileExists("MenuItems.obj",this)){
            Utils.displayLongToastMessage(this,"This file does exists.");
            jsonArray=ResourceManager.getFileToJSON("MenuItems.obj",this);
        }else{
            getMenuItems();
        }*/
        ResourceManager.setBaseActivityInstance(this);

    }

    public void runApi(){

    }

    public void getMenuItems() {
        new GetMenuItems(this,this).execute("Url address here");
    }

    public void getOrderItems(){
        new GetOrderListWithTable(this,this).execute("get order items");
    }

    public void getTableInformation(){}

    @Override
    public void MenuItemsReceived(ArrayList<FoodDetails> foodDetails, boolean fileExists) {
    }





    @Override
    public void onOrdersForTableReceived( boolean fileExists) {

    }

    @Override
    public void onAllOrderedListReceiver(ArrayList<OrderDetail> orderDetailArrayList, boolean isSuccess) {

    }

    @Override
    public void onItemAddedOrUpdated(boolean isSuccessful) {

    }

    @Override
    public void onCustomerCheckoutSuccessfully(boolean isSuccess) {

    }
}
