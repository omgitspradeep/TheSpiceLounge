package com.sujan.info.thespicelounge;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import com.sujan.info.thespicelounge.Adapters.FoodMenuAdapter;
import com.sujan.info.thespicelounge.Utils.MyPreferences;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.fragments.HomeFragment;
import com.sujan.info.thespicelounge.fragments.WaiterOrderListFragment;
import com.sujan.info.thespicelounge.interfaceT.orderStatusUpdateListener;
import com.sujan.info.thespicelounge.models.FoodDetails;
import com.sujan.info.thespicelounge.models.OrderDetail;

import java.util.ArrayList;

public class WaiterActivity extends BaseActivity implements FoodMenuAdapter.ItemClickListener,orderStatusUpdateListener {


    FrameLayout displayFoodItemsFrame;
    FloatingActionButton fab;
    Toolbar tb=null;
    boolean doubleBackToExitPressedOnce = false;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter);
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
                MyPreferences.resetAllPreferences(getApplicationContext());
                Utils.activityTransitionRemovingHistory(this,new Intent(this, MainActivity.class));
                break;

            default:
                break;
        }

        return true;
    }

    public FloatingActionButton getFab() {
        return fab;
    }

    private void init() {
        tb = (Toolbar)findViewById(R.id.toolbar_waiter_activity);
        tb.setTitle("Waiter Ram");
        tb.inflateMenu(R.menu.toolbar);
        setActionBar(tb);
        displayFoodItemsFrame=(FrameLayout) findViewById(R.id.display_food_items_frame);
        progressBar=(ProgressBar)findViewById(R.id.waiter_activity_progressbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.display_food_items_frame, new HomeFragment(WaiterActivity.this)).commit();
                fab.setVisibility(View.GONE);
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.display_food_items_frame, new HomeFragment(WaiterActivity.this)).commit();
        fab.setVisibility(View.GONE);
    }


    public ProgressBar getWaiterProgressBar(){
        return progressBar;
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

    @Override
    public void onItemClick(View view, int position) {

        Intent intent = new Intent(this, FoodOrderActivity.class);
        intent.putExtra("foodID",ResourceManager.getFoodDetailsInstance().getItemId());
        startActivity(intent);
//        finish();
    }

    public void inflateAdapter(){
        getSupportFragmentManager().beginTransaction().replace(R.id.display_food_items_frame, new WaiterOrderListFragment(this)).commit();
        ResourceManager.getWaiterActivityInstance().getFab().setVisibility(View.VISIBLE);
    }

    @Override
    public void onAllOrderedListReceiver(ArrayList<OrderDetail> orderDetailArrayList, boolean isSuccess) {
        progressBar.setVisibility(View.INVISIBLE);
        if (isSuccess){
            ResourceManager.setAllOrders(orderDetailArrayList);
            inflateAdapter();
        }else {
            Utils.displayShortToastMessage(this,"Try again");
        }
    }

    @Override
    public void onOrderStatusUpdated(boolean isSuccessful) {
        progressBar.setVisibility(View.INVISIBLE);
        if (isSuccessful){
            inflateAdapter();
        }
    }
}
