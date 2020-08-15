package com.sujan.info.thespicelounge.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sujan.info.thespicelounge.Adapters.FoodMenuAdapter;
import com.sujan.info.thespicelounge.Adapters.TableSelectionDialogAdapter;
import com.sujan.info.thespicelounge.FoodOrderActivity;
import com.sujan.info.thespicelounge.MainActivity;
import com.sujan.info.thespicelounge.R;
import com.sujan.info.thespicelounge.Utils.Constants;
import com.sujan.info.thespicelounge.Utils.MyPreferences;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.WaiterActivity;
import com.sujan.info.thespicelounge.models.FoodDetails;
import com.sujan.info.thespicelounge.webservices.GetOrdersList;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment {

    MainActivity mainActivityInstance;
    WaiterActivity waiterActivityInstance;

    Activity activity;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    FoodMenuAdapter menuFoodAdapter;

    boolean isItWaiter=false;

    @SuppressLint("ValidFragment")
    public HomeFragment(MainActivity mContext) {
        // Required empty public constructor
        mainActivityInstance=mContext;
        activity=mContext;
    }

    public HomeFragment(WaiterActivity waiterActivityInstance) {
        this.waiterActivityInstance = waiterActivityInstance;
        activity=waiterActivityInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }



    private void init() {
        isItWaiter=MyPreferences.getBooleanPrefrences(Constants.IS_USER_LOGGED_IN,activity);

        //This fragment is used in both waiter and customer menu list.
        if(isItWaiter){
            fab = (FloatingActionButton)waiterActivityInstance.findViewById(R.id.fab_order_view);
            recyclerView=(RecyclerView)waiterActivityInstance.findViewById(R.id.food_menu_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(waiterActivityInstance));
            menuFoodAdapter = new FoodMenuAdapter(waiterActivityInstance);
            recyclerView.setAdapter(menuFoodAdapter);
            menuFoodAdapter.setClickListener(waiterActivityInstance);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // GET ORDERS LIST
                    if(Utils.isConnected(activity)){
                        waiterActivityInstance.getWaiterProgressBar().setVisibility(View.VISIBLE);
                        new GetOrdersList(waiterActivityInstance).execute();
                        ResourceManager.setWaiterActivityInstance(waiterActivityInstance);
                    }else{
                        Utils.displayShortToastMessage(activity,"No Internet.");
                    }
                }
            });


        }else {
            fab = (FloatingActionButton)mainActivityInstance.findViewById(R.id.fab_order_view);
            recyclerView=(RecyclerView)mainActivityInstance.findViewById(R.id.food_menu_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(mainActivityInstance));
            menuFoodAdapter = new FoodMenuAdapter(mainActivityInstance);
            recyclerView.setAdapter(menuFoodAdapter);
            menuFoodAdapter.setClickListener(mainActivityInstance);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        if(Utils.isConnected(mainActivityInstance)){
                            if(ResourceManager.listOfBusyTables!=null){
                                Utils.buildAskTableDialog(mainActivityInstance);
                            }else {
                                Utils.displayShortToastMessage(mainActivityInstance,"You have not order your food yet.");
                            }
                        }
                         else{
                            Utils.displayShortToastMessage(mainActivityInstance,"No Internet");

                        }
                }
            });
        }

    }



}
