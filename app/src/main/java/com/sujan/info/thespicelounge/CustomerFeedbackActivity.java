package com.sujan.info.thespicelounge;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sujan.info.thespicelounge.Adapters.FeedbackListAdapter;
import com.sujan.info.thespicelounge.Utils.Constants;
import com.sujan.info.thespicelounge.Utils.MyPreferences;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.interfaceT.feedbackAddedListener;
import com.sujan.info.thespicelounge.models.CustomerFeedback;
import com.sujan.info.thespicelounge.models.FoodDetails;
import com.sujan.info.thespicelounge.models.OrderDetail;
import com.sujan.info.thespicelounge.webservices.PushCustomerFeedbacks;

import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.spec.IvParameterSpec;

public class CustomerFeedbackActivity extends AppCompatActivity implements feedbackAddedListener {


    TextView feedbackFoodName,customerFeedbacksTitle;
    RatingBar userFeedbackRating, foodFeedbackRating;
    EditText customerFeedbackName, customerFeedbackEdit;
    ImageView foodImageFeedback;
    Button feedbackPush;
    RecyclerView recyclerViewFeedbacks;
    FeedbackListAdapter feedbackListAdapter;
    RelativeLayout userFeedbackContainer;
    Boolean isItForCommentByCustomer=false;
    boolean isItWaiter=false;
    FoodDetails foodDetails;
    int selectedFoodID;
    CustomerFeedback customerFeedbackToBeAdded;
    String FeedbackTitle="";
    ProgressBar progressBarFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_feedback);
        init();
        inflateData();
    }

    private void inflateData() {
        if(ResourceManager.getCustomerFeedbackArrayList().size()!=0){  // If there are no feedbacks for that particular food items
            feedbackListAdapter=new FeedbackListAdapter(ResourceManager.getCustomerFeedbackArrayList(),this);
            recyclerViewFeedbacks.setAdapter(feedbackListAdapter);
            customerFeedbacksTitle.setText(R.string.cust_feedbacks_avl);

        }else{
            customerFeedbacksTitle.setText(R.string.no_cust_feedbacks);
        }


        String foodname =foodDetails.getFullItemName();
        feedbackFoodName.setText(foodname);
        foodFeedbackRating.setNumStars(foodDetails.getRatingStar());

    }

    private void init() {


        isItWaiter= MyPreferences.getBooleanPrefrences(Constants.IS_USER_LOGGED_IN,this);
        isItForCommentByCustomer= getIntent().getBooleanExtra("toComment",false);
        selectedFoodID= getIntent().getIntExtra("foodID",0);
        foodDetails= (FoodDetails) ResourceManager.getGlobalFoodMenuMap().get(selectedFoodID);

        progressBarFeedback=(ProgressBar)findViewById(R.id.progressbar_cust_feedback);
        customerFeedbacksTitle=(TextView)findViewById(R.id.feedback_title_message_two);
        feedbackFoodName=(TextView)findViewById(R.id.food_name_feedback);
        userFeedbackRating = (RatingBar) findViewById(R.id.user_rating_feedback);
        foodFeedbackRating = (RatingBar) findViewById(R.id.food_rating_feedback);
        customerFeedbackName = (EditText) findViewById(R.id.current_customer_name);
        customerFeedbackEdit = (EditText) findViewById(R.id.user_feedback);
        foodImageFeedback=(ImageView)findViewById(R.id.food_image_feedback);
        feedbackPush=(Button)findViewById(R.id.cust_feedback_push);
        userFeedbackContainer=(RelativeLayout)findViewById(R.id.userFeedbackContainer);
        recyclerViewFeedbacks=(RecyclerView)findViewById(R.id.feedbacks_list_recycler);
        recyclerViewFeedbacks.setLayoutManager(new LinearLayoutManager(this));


        if(isItForCommentByCustomer){
            feedbackPush.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String customerName=customerFeedbackName.getText().toString();
                    String customerFeedbackStr=customerFeedbackEdit.getText().toString();
                    int customerRating= userFeedbackRating.getNumStars();

                    if(verifyFeedbackDetails(customerName,customerFeedbackStr)){
                        progressBarFeedback.setVisibility(View.VISIBLE);
                        customerFeedbackToBeAdded= new CustomerFeedback(selectedFoodID,customerName,customerFeedbackStr,customerRating);
                        new PushCustomerFeedbacks(CustomerFeedbackActivity.this,customerFeedbackToBeAdded).execute();
                    }else{
                        Utils.displayShortToastMessage(CustomerFeedbackActivity.this,"Please verify your data and submit again.");
                    }
                }
            });
        }else{
            userFeedbackContainer.setVisibility(View.GONE);
        }
    }


    private boolean verifyFeedbackDetails(String name,String feedback) {
        boolean okey=true;
        if(name.equals("") || feedback.equals("")){
            okey=false;
        }
        return okey;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isItForCommentByCustomer){
            Utils.activityTransitionRemovingHistory(this,new Intent(this,MainActivity.class));
        }
    }

    @Override
    public void onFBAddedMessage(boolean isSuccessful) {
        progressBarFeedback.setVisibility(View.INVISIBLE);
        if(isSuccessful){
            userFeedbackContainer.setVisibility(View.GONE);
            ResourceManager.addNewCustomerFeedBack(customerFeedbackToBeAdded); // we did not bring added feedback from server but add new feedback in temporary
            Utils.displayShortToastMessage(this,"You have successfully submit your feedback.");
            inflateData();

        }else {
            Utils.displayShortToastMessage(this,"You have successfully submit your feedback.");
        }
    }
}
