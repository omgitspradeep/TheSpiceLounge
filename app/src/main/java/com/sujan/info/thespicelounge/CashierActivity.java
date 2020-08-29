package com.sujan.info.thespicelounge;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import com.sujan.info.thespicelounge.Adapters.OrderedListAdapterCashier;
import com.sujan.info.thespicelounge.Adapters.TableAdapter;
import com.sujan.info.thespicelounge.Utils.Constants;
import com.sujan.info.thespicelounge.Utils.MyPreferences;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.fragments.CashierOrderFragment;
import com.sujan.info.thespicelounge.fragments.CashierTableFragment;
import com.sujan.info.thespicelounge.interfaceT.orderListWithTableListener;
import com.sujan.info.thespicelounge.models.OrderDetail;
import com.sujan.info.thespicelounge.models.TableInformation;
import com.sujan.info.thespicelounge.webservices.GetOrderListWithTable;

import java.util.ArrayList;

public class CashierActivity extends BaseActivity implements TableAdapter.tableSelectionInterface,OrderedListAdapterCashier.onOrderedListInCashierUpdated,orderListWithTableListener {


    Toolbar tb;
    FrameLayout frameLayout;
    FloatingActionButton fabForBack,fabForRefresh;
    boolean doubleBackToExitPressedOnce = false;
    CashierOrderFragment cashierOrderFragment;
    ProgressBar progressBar;


    @Override
    public void onTableClick(int tableNum,ArrayList<OrderDetail> orderDetailArrayList) {
        cashierOrderFragment=new CashierOrderFragment(this,orderDetailArrayList,tableNum);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_cashier_frame, cashierOrderFragment).commit();
        fabForBack.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);
        init();
    }

    private void init() {

        tb = (Toolbar) findViewById(R.id.toolbar_cashier);
        tb.setTitle(MyPreferences.getStringPrefrences(Constants.EMPLOYEE_NAME_TITLE,this));
        tb.inflateMenu(R.menu.toolbar);
        setActionBar(tb);
        progressBar=(ProgressBar)findViewById(R.id.progressBarRefresh);
        fabForBack=(FloatingActionButton)findViewById(R.id.fab_cashier_back_to_table);
        fabForRefresh=(FloatingActionButton)findViewById(R.id.fab_refresh_to_update);
        frameLayout=(FrameLayout)findViewById(R.id.fragment_cashier_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_cashier_frame, new CashierTableFragment(this)).commit();
        fabForBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    onFabForBackPressed();
            }
        });
        fabForRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                new GetOrderListWithTable(CashierActivity.this,CashierActivity.this).execute();
            }
        });
    }

    public void onFabForBackPressed() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_cashier_frame, new CashierTableFragment(CashierActivity.this)).commit();
        fabForBack.setVisibility(View.INVISIBLE);
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
    public void onCashierListUpdated(int totalAmt, boolean canBeCheckout) {
        cashierOrderFragment.updateTotalPriceAndCheckout(totalAmt,canBeCheckout);
    }

    public void setFabForRefreshInvisible(){
        fabForRefresh.setVisibility(View.INVISIBLE);
    }

    public void progressBarVisibility(int id){
        // id: 0->invisible, 1:visible
        if(id==0){
            progressBar.setVisibility(View.INVISIBLE);
        }else{
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onOrdersForTableReceived(boolean isSuccess) {
        progressBar.setVisibility(View.INVISIBLE);
        if(isSuccess){
            Utils.activityTransitionRemovingHistory(this,new Intent(this,CashierActivity.class));
            Utils.displayLongToastMessage(this,"All table orders are updated.");
        }

    }

    public void setFabForRefreshVisible() {
        fabForRefresh.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCustomerCheckoutSuccessfully(boolean isSuccess) {
        Utils.activityTransitionRemovingHistory(this,new Intent(this,CashierActivity.class));
        Utils.displayShortToastMessage(this,"Successfully checkout");
    }
}
