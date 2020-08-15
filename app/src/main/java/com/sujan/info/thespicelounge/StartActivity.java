package com.sujan.info.thespicelounge;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.sujan.info.thespicelounge.Utils.Constants;
import com.sujan.info.thespicelounge.Utils.JSONParser;
import com.sujan.info.thespicelounge.Utils.MyPreferences;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.interfaceT.feedbackListener;
import com.sujan.info.thespicelounge.models.CustomerFeedback;
import com.sujan.info.thespicelounge.models.FoodDetails;
import com.sujan.info.thespicelounge.models.OrderDetail;
import com.sujan.info.thespicelounge.models.TableInformation;
import com.sujan.info.thespicelounge.webservices.GetCustomerFeedbacks;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class StartActivity extends BaseActivity {

    Intent intent;
    ArrayList<TableInformation> tableInformationArrayList;
    RelativeLayout relativeLayInitialMsgForNoInternet;
    Button refreshButton;
    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_splash);

        init();

    }

    private void init() {
        isConnected=Utils.isConnected(this);
        relativeLayInitialMsgForNoInternet=(RelativeLayout)findViewById(R.id.relativeLNoInitialMsgForNoInternet);
        refreshButton=(Button)findViewById(R.id.refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isConnected=Utils.isConnected(StartActivity.this);
                if(isConnected) {
                    getData();
                    relativeLayInitialMsgForNoInternet.setVisibility(View.INVISIBLE);
                }else{
                    Utils.displayShortToastMessage(StartActivity.this,"No Internet");
                }
            }
        });


        getData();

    }

    private void getData() {

        if (isConnected){
            //get data from internet
            getMenuItems();
        }else{

            if(ResourceManager.isFileExists(new String[]{Constants.MENU_FILE},this)){
                //getdata from file
                getMenuItemsFromFile();
            }else{
                // Need internet for initial setup message
                relativeLayInitialMsgForNoInternet.setVisibility(View.VISIBLE);
            }
        }
    }


    private void getMenuItemsFromFile() {
        String s=ResourceManager.readFromFile(Constants.MENU_FILE,this);
        try {
            JSONArray jsonArray = new JSONArray(s);
            ArrayList<FoodDetails> foodDetails = JSONParser.ParseMenuItems(jsonArray);
            ResourceManager.setGlobalFoodMenu(foodDetails);
            transitActivity();
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }


    }

    private void prepareOrderData() {
        // store all the value in Hash map after json is received by using key as their unique key
    }

    private void transitActivity() {

            if (MyPreferences.getBooleanPrefrences(Constants.IS_USER_LOGGED_IN,getApplicationContext())){
                // 0: admin, 1: Cashier, 2: Chef, 3: Waiter
                if (Utils.isConnected(this)){
                    int userId=MyPreferences.getIntPrefrences(Constants.LAST_LOGGED_USER,getApplicationContext());

                    switch (userId){
                        case 1:
                            intent=new Intent(this,AdminActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 2:
                            intent=new Intent(this,ChefActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 3:
                            intent=new Intent(this,WaiterActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 4:
                            intent=new Intent(this,CashierActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        default:
                            break;
                    }
                }else{
                    relativeLayInitialMsgForNoInternet.setVisibility(View.VISIBLE);
                }

            }else{
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }





    @Override
    public void MenuItemsReceived(ArrayList<FoodDetails> foodDetailsArrayList, boolean isSuccess) {

        if(isSuccess){
            ResourceManager.setGlobalFoodMenu(foodDetailsArrayList);
            ResourceManager.setFoodMenuHashMap(foodDetailsArrayList);
            Log.d("RECEIVED","All menu items received successfullyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
            getOrderItems();
        }else{
            relativeLayInitialMsgForNoInternet.setVisibility(View.VISIBLE);
        }



    }

    @Override
    public void onOrdersForTableReceived(boolean fileExists) {
        if(fileExists){
            Log.d("RECEIVED","All Order with table data received successfullyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
            transitActivity();
        }else {
            relativeLayInitialMsgForNoInternet.setVisibility(View.VISIBLE);
        }

    }




    /*    private void prepareMenuData() {

        if(isConnected){
            getMenuItems();
        }else{

            Utils.displayLongToastMessage(this,"No internet connection");
            if(ResourceManager.isMenuFileExists(Constants.MENU_FILE,this)){

                String s=ResourceManager.readFromFile(Constants.MENU_FILE,this);
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    ArrayList<FoodDetails> foodDetails = JSONParser.ParseMenuItems(jsonArray);
                    ResourceManager.setGlobalFoodMenu(foodDetails);
                    transitActivity();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }else{
                relativeLayInitialMsgForNoInternet.setVisibility(View.VISIBLE);
            }
        }
    }*/


    // Orders should not be taken from offline file because it need to be lively updated.


}
