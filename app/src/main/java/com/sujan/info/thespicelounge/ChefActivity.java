package com.sujan.info.thespicelounge;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import com.sujan.info.thespicelounge.Adapters.OrderedListAdapterChef;
import com.sujan.info.thespicelounge.Utils.Constants;
import com.sujan.info.thespicelounge.Utils.MyPreferences;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.fragments.WaiterOrderListFragment;
import com.sujan.info.thespicelounge.interfaceT.orderStatusUpdateListener;
import com.sujan.info.thespicelounge.models.OrderDetail;
import com.sujan.info.thespicelounge.webservices.GetOrdersList;

import java.util.ArrayList;
import java.util.Collections;

import static junit.framework.Assert.assertTrue;

public class ChefActivity extends BaseActivity implements orderStatusUpdateListener {

    FloatingActionButton modifyFoodItemFab;
    RecyclerView chefRecyclerViewOrders;
    ArrayList<OrderDetail> orderDetailsListChef;
    OrderedListAdapterChef orderedListAdapterChef;
    Toolbar tb;
    ProgressBar progressBarChef;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef);
        init();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return onOptionsItemSelected(item);
                    }
                });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_logout:
                Utils.activityTransitionRemovingHistory(this,new Intent(this, MainActivity.class));
                MyPreferences.resetAllPreferences(getApplicationContext());
                break;

            default:
                break;
        }

        return true;
    }



    private void init() {


        tb = (Toolbar) findViewById(R.id.toolbar_chef_activity);
        tb.setTitle(MyPreferences.getStringPrefrences(Constants.EMPLOYEE_NAME_TITLE,this));
        tb.inflateMenu(R.menu.toolbar);
        setActionBar(tb);

        orderDetailsListChef=new ArrayList<OrderDetail>();


        progressBarChef=(ProgressBar)findViewById(R.id.progress_bar_chef);
        modifyFoodItemFab=(FloatingActionButton)findViewById(R.id.fab_chef_itemview);
        chefRecyclerViewOrders=(RecyclerView)findViewById(R.id.ordered_items_recycler_chef);
        chefRecyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

        modifyFoodItemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChefActivity.this, ChefMenuModifyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if(ResourceManager.isAllFoodOrdersAvailable()){

            inflateAdapter();

        }else{
            new GetOrdersList(this).execute();
        }
    }

    public void inflateAdapter(){
        orderedListAdapterChef = new OrderedListAdapterChef(this,ResourceManager.getAllOrders());
        chefRecyclerViewOrders.setAdapter(orderedListAdapterChef);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Utils.displayShortToastMessage(this, "Please click BACK again to exit");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }


    public ProgressBar getProgressBarChef(){
        return progressBarChef;
    }

    @Override
    public void onAllOrderedListReceiver(ArrayList<OrderDetail> orderDetailArrayList, boolean isSuccess) {
        ResourceManager.setAllOrders(orderDetailArrayList);

        if (isSuccess){
            Collections.sort(ResourceManager.allOrders, OrderDetail.statusComparator);
        }else {
            Utils.displayShortToastMessage(this,"Something went wrong.Try again");
        }
     inflateAdapter();
    }

    @Override
    public void onOrderStatusUpdated(boolean isSuccessful) {
        progressBarChef.setVisibility(View.INVISIBLE);
        if (isSuccessful){
            inflateAdapter();
        }else{
            Utils.displayShortToastMessage(this,"Try again");
        }
    }
}
