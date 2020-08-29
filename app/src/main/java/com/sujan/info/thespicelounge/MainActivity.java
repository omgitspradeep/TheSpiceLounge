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
import com.sujan.info.thespicelounge.Utils.Constants;
import com.sujan.info.thespicelounge.Utils.JSONParser;
import com.sujan.info.thespicelounge.Utils.MyPreferences;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.fragments.AboutUs;
import com.sujan.info.thespicelounge.fragments.CustomerOrderdListFragment;
import com.sujan.info.thespicelounge.fragments.Entertainment;
import com.sujan.info.thespicelounge.fragments.HomeFragment;
import com.sujan.info.thespicelounge.fragments.Login;
import com.sujan.info.thespicelounge.interfaceT.cancelFoodListener;
import com.sujan.info.thespicelounge.interfaceT.feedbackListener;
import com.sujan.info.thespicelounge.interfaceT.loginListener;
import com.sujan.info.thespicelounge.models.CustomerFeedback;
import com.sujan.info.thespicelounge.models.FoodDetails;
import com.sujan.info.thespicelounge.webservices.GetMenuItems;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements FoodMenuAdapter.ItemClickListener,feedbackListener,cancelFoodListener, loginListener {

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

    @Override
    public void onLoginResponse(boolean isSuccess) {
        progressBarMain.setVisibility(View.INVISIBLE);

        if (isSuccess){
            String staffPost=ResourceManager.getLoggedStaff().getPost();
            MyPreferences.setBooleanPrefrences(Constants.IS_USER_LOGGED_IN,true,this);

                if(staffPost.equals("manager") ){
                    //login for admin or manager : It is supposed to be redirect to webview,
                    MyPreferences.setIntPrefrences(Constants.LAST_LOGGED_USER,Constants.ADMIN_ID,this);
                    Utils.activityTransitionRemovingHistory(this, new Intent(this, AdminActivity.class));


                }else if(staffPost.equals("chef")){
                    //login for chef,
                    MyPreferences.setIntPrefrences(Constants.LAST_LOGGED_USER,Constants.CHEF_ID,this);
                    Utils.activityTransitionRemovingHistory(this, new Intent(this, ChefActivity.class));


                }else if(staffPost.equals("waiter")){
                    //login for waiter
                    MyPreferences.setIntPrefrences(Constants.LAST_LOGGED_USER,Constants.WAITER_ID,this);
                    Utils.activityTransitionRemovingHistory(this, new Intent(this, WaiterActivity.class));

                }else if(staffPost.equals("cashier")) {
                    //login for cashier
                            /*When user opens app without Internet then only foodmenu list is available as saved data  but not food orders with table
                            As cashier logs in and internet is available then app crashes since there is no data about tables.
                            Therefore, we need to bring table data.*/

                    if (ResourceManager.isFoodOrdersWithTableDataAvailable()) {
                        //login for cashier
                        MyPreferences.setIntPrefrences(Constants.LAST_LOGGED_USER,Constants.CASHIER_ID,this);
                        Utils.activityTransitionRemovingHistory(this, new Intent(this, CashierActivity.class));
                    } else {
                        Utils.displayShortToastMessage(this, "Please restart app once again.");
                    }
                }
        }else{
            Utils.displayShortToastMessage(this, "You have entered wrong credentials.");
        }
    }
}
