package com.sujan.info.thespicelounge;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sujan.info.thespicelounge.Utils.Constants;
import com.sujan.info.thespicelounge.Utils.MyPreferences;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.interfaceT.feedbackListener;
import com.sujan.info.thespicelounge.interfaceT.takeOrderListener;
import com.sujan.info.thespicelounge.models.CustomerFeedback;
import com.sujan.info.thespicelounge.models.FoodDetails;
import com.sujan.info.thespicelounge.models.OrderDetail;
import com.sujan.info.thespicelounge.webservices.GetCustomerFeedbacks;
import com.sujan.info.thespicelounge.webservices.PushFoodOrder;

import java.util.ArrayList;

public class FoodOrderActivity extends AppCompatActivity implements feedbackListener,takeOrderListener {

    Button placeOrder,feedbacks;
    FoodDetails foodDetails;
    ImageView foodImage;
    TextView  foodName, timePreparation, foodDesc, foodPrice;
    Spinner tableNumSpinner;
    CheckBox verifyEntries;
    int tableNumber;
    ArrayAdapter<Integer> adapter;
    boolean isItWaiter=false;
    ProgressBar receivingFeedbacksPB;
    boolean feedbackIsNotClickedOnce=true; // to stop user from clicking feedback option multiple times
    OrderDetail newOrder;
    int selectedFoodID;
    EditText foodQuantity,custumerRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order);

        Intent intent = getIntent();
        selectedFoodID= intent.getIntExtra("foodID",0);

        init();
        dataInflate();

    }

    @SuppressLint("SetTextI18n")
    private void dataInflate() {

     //   foodImage.setImageResource(foodDetails.getImage());
        foodImage.setImageResource(R.drawable.momos);
        foodName.setText(foodDetails.getItemName());
        timePreparation.setText(foodDetails.getPrepareTimeinMins()+"");
        foodDesc.setText(foodDetails.getDescription());
        foodPrice.setText(foodDetails.getPrice()+"");

        feedbacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isConnected(FoodOrderActivity.this)){
                    if(feedbackIsNotClickedOnce){
                        feedbackIsNotClickedOnce=false;
                        receivingFeedbacksPB.setVisibility(View.VISIBLE);
                        new GetCustomerFeedbacks(FoodOrderActivity.this,foodDetails.getItemId(),receivingFeedbacksPB).execute();
                    }

                }else{
                    Utils.displayShortToastMessage(FoodOrderActivity.this,"No Internet.");
                }



            }
        });

        tableNumSpinner.setAdapter(adapter);

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderQuant=foodQuantity.getText().toString();
                if(verifyEntries.isChecked() && !orderQuant.equals("")){

                    tableNumber=Integer.parseInt(tableNumSpinner.getSelectedItem().toString());
                    String customerRequestStr=custumerRequest.getText().toString();

                    Log.d("SIZEy","Quantity of orders are: "+orderQuant);
                    // Here orderId and foodStatus doesnot matter since we place "null" and "0" in php file
                    newOrder=new OrderDetail(0,selectedFoodID,tableNumber,Integer.parseInt(orderQuant),0,customerRequestStr);
                    new PushFoodOrder(newOrder,FoodOrderActivity.this).execute();

                    /*if(MyPreferences.getBooleanPrefrences(Constants.IS_USER_LOGGED_IN,getApplicationContext())){
                        // Waiter orders from here

                    }else {

                        //Customer orders from here
                    }*/
                }else{
                    Utils.displayLongToastMessage(FoodOrderActivity.this, "Please verify the details ");
                }
            }
        });
    }



    private void init() {


        isItWaiter=MyPreferences.getBooleanPrefrences(Constants.IS_USER_LOGGED_IN,this);
        foodDetails= ResourceManager.getFoodDetailsInstance();

        adapter = new ArrayAdapter<Integer>(this,R.layout.customized_spinner,ResourceManager.getListOfTableNumber());

        foodImage=(ImageView)findViewById(R.id.food_image);
        foodName=(TextView)findViewById(R.id.order_fullName);
        foodQuantity=(EditText) findViewById(R.id.order_quantity);
        timePreparation=(TextView)findViewById(R.id.prepare_time_order);
        foodDesc=(TextView)findViewById(R.id.desc_order);
        foodPrice=(TextView)findViewById(R.id.order_price);
        tableNumSpinner=(Spinner) findViewById(R.id.table_num_spinner_order);
        custumerRequest=(EditText) findViewById(R.id.customer_request_order);
        verifyEntries=(CheckBox)findViewById(R.id.verify_order_details);
        placeOrder=(Button)findViewById(R.id.place_order);
        feedbacks=(Button)findViewById(R.id.view_item_feedback_customer);
        receivingFeedbacksPB=(ProgressBar)findViewById(R.id.feedback_pb_placeorder);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isItWaiter){
            Utils.activityTransitionRemovingHistory(this,new Intent(this,WaiterActivity.class));
        }else{
            Utils.activityTransitionRemovingHistory(this,new Intent(this,MainActivity.class));
        }
    }

    @Override
    public void onFeedbackReceived(ArrayList<CustomerFeedback> customerFeedbacks, int foodID, ProgressBar progressBar,boolean isSuccess) {
        progressBar.setVisibility(View.GONE);
        feedbackIsNotClickedOnce=true;
        if(isSuccess){
            ResourceManager.setCustomerFeedbackArrayList(customerFeedbacks);
            Intent intent=new Intent(FoodOrderActivity.this, CustomerFeedbackActivity.class);
            intent.putExtra("foodID",foodDetails.getItemId());
            intent.putExtra("toComment",false);
            startActivity(intent);
        }else {
            Utils.displayShortToastMessage(this,"Try again.");

        }

    }

    @Override
    public void onOrderReceived(boolean isSuccess) {
        if(isSuccess){
            Utils.displayLongToastMessage(getApplication(),"Your order has been successfully placed for table number "+tableNumber);

            // if user is logged in go to waiterActivity else go to Home page since it must be customer who placed order.
            if(isItWaiter){
                startActivity(new Intent(FoodOrderActivity.this, WaiterActivity.class));
            }else {
                startActivity(new Intent(FoodOrderActivity.this, MainActivity.class));
                ResourceManager.setActiveTableForCurrentCustomer(tableNumber+"");

            }
        }else {
            Utils.displayLongToastMessage(this,"Something went wrong.Please try again ");
        }

    }
}
