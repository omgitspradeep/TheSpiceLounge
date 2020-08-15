package com.sujan.info.thespicelounge;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.sujan.info.thespicelounge.Adapters.FoodMenuAdapter;
import com.sujan.info.thespicelounge.Utils.JSONParser;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.fragments.AboutUs;
import com.sujan.info.thespicelounge.fragments.CustomerOrderdListFragment;
import com.sujan.info.thespicelounge.fragments.Entertainment;
import com.sujan.info.thespicelounge.fragments.HomeFragment;
import com.sujan.info.thespicelounge.fragments.Login;
import com.sujan.info.thespicelounge.interfaceT.cancelFoodListener;
import com.sujan.info.thespicelounge.interfaceT.feedbackListener;
import com.sujan.info.thespicelounge.models.CustomerFeedback;
import com.sujan.info.thespicelounge.models.FoodDetails;
import com.sujan.info.thespicelounge.webservices.GetMenuItems;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements FoodMenuAdapter.ItemClickListener,feedbackListener,cancelFoodListener {

    BottomNavigationView navigation;
    FrameLayout frameLayout;
    boolean doubleBackToExitPressedOnce = false;
    ProgressBar progressBarMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {

        frameLayout=(FrameLayout)findViewById(R.id.fl_fragment);
        progressBarMain=(ProgressBar)findViewById(R.id.main_activity_progressbar);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment, new HomeFragment(this)).commit();

    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment,new HomeFragment(MainActivity.this)).commit();
                    break;
                case R.id.entertainment:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment,new Entertainment()).commit();
                    break;

                case R.id.login:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment,new Login()).commit();
                    break;

                case R.id.about_us:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment,new AboutUs()).commit();
                    break;
            }
            return true;
        }
    };

    public ProgressBar getProgressBarMain(){
        return progressBarMain;
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
    }


    @Override
    public void onFeedbackReceived(ArrayList<CustomerFeedback> customerFeedbacks, int foodID, ProgressBar progressBar,boolean isSuccess) {
        progressBar.setVisibility(View.GONE);
        if(isSuccess){
            ResourceManager.setCustomerFeedbackArrayList(customerFeedbacks);
            // Once feebacks are received "CustomerFeedbackActivity" is proceed
            Intent intent = new Intent(this, CustomerFeedbackActivity.class);
            intent.putExtra("toComment", true);
            intent.putExtra("foodID",foodID);

            startActivity(intent);
        }else{
            Utils.displayShortToastMessage(this,"Try again.");
        }

    }

    @Override
    public void onFoodCancelled(boolean isSuccess) {
        progressBarMain.setVisibility(View.INVISIBLE);
        Utils.displayLongToastMessage(this,"Food order is cancelled successfully.");
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment,new CustomerOrderdListFragment(this)).commit();

    }
}
