package com.sujan.info.thespicelounge;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.sujan.info.thespicelounge.Adapters.FoodMenuAdapterChef;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.interfaceT.deleteItemListener;
import com.sujan.info.thespicelounge.models.FoodDetails;
import com.sujan.info.thespicelounge.webservices.DeleteMenuItem;

import java.util.ArrayList;

public class ChefMenuModifyActivity extends AppCompatActivity implements FoodMenuAdapterChef.onEditListener,deleteItemListener {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    FoodMenuAdapterChef foodMenuAdapterChef;
    FoodDetails foodItemtoBedeleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_menu_modify);
        init();


    }

    private void init() {
        foodMenuAdapterChef=new FoodMenuAdapterChef(this);
        recyclerView=(RecyclerView)findViewById(R.id.item_edit_modify_list_chef_chef);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(foodMenuAdapterChef);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChefMenuModifyActivity.this, ChefItemAddAndModifyActivity.class);
                intent.putExtra("isItForItemEdit",false);
                startActivity(intent);
            }
        });
    }

    @Override
    public void editClickListener(FoodDetails foodDetails) {
        ResourceManager.setFoodDetailsInstance(foodDetails);
        Intent intent = new Intent(ChefMenuModifyActivity.this, ChefItemAddAndModifyActivity.class);
        intent.putExtra("isItForItemEdit",true);
        startActivity(intent);

    }

    @Override
    public void deleteButtonClicked(FoodDetails foodDetails) {

        Utils.showAlertDialogBeforeDelete(this,this,foodDetails.getItemId());
        foodItemtoBedeleted=foodDetails;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChefMenuModifyActivity.this, ChefActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMenuItemDeleted(boolean isSuccessful) {
        Utils.displayShortToastMessage(this,"Item: "+foodItemtoBedeleted.getFullItemName()+" has been successfully removed from menu.");
        onBackPressed();
    }
}
