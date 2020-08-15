package com.sujan.info.thespicelounge;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.sujan.info.thespicelounge.Utils.Constants;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.models.FoodDetails;
import com.sujan.info.thespicelounge.webservices.AddUpdateFoodItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.sujan.info.thespicelounge.Utils.Constants.PICK_IMAGE;

public class ChefItemAddAndModifyActivity extends BaseActivity {

    Button addModifyItem;
    ImageView foodImage;
    boolean isItForItemEdit=false;
    FoodDetails foodDetailsInstanceForEdit,foodDetailsForUpdate;
    EditText foodCategoryChef,foodNameChef,foodServeTypeChef,foodPriceChef,foodTimePrepChef,foodDescChef;
    CheckBox verifyDetails;


    String foodC,foodN,foodST,foodD,foodP,foodT;
    int foodImg,foodPri,foodTim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_item_add_and_modify);
        isItForItemEdit= getIntent().getBooleanExtra("isItForItemEdit",false);
        init();

    }

    @SuppressLint("SetTextI18n")
    private void init() {

        addModifyItem=(Button)findViewById(R.id.push_button_chef_modify);
        foodImage=(ImageView)findViewById(R.id.food_image_chef_modify);
        foodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSelection();
            }
        });
        foodCategoryChef=(EditText)findViewById(R.id.food_catgory_chef_modify);
        foodNameChef=(EditText)findViewById(R.id.food_name_chef_modify);
        foodServeTypeChef=(EditText)findViewById(R.id.serve_type_chef_modify);
        foodPriceChef=(EditText)findViewById(R.id.food_price_chef_modify);
        foodTimePrepChef=(EditText)findViewById(R.id.food_time_chef_modify);
        foodDescChef=(EditText)findViewById(R.id.food_desc_chef_modify);
        verifyDetails=(CheckBox)findViewById(R.id.terms_conditions_chef_modify);

        verifyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(areFieldsEmpty()){
                    verifyDetails.setChecked(false);
                    Utils.displayLongToastMessage(ChefItemAddAndModifyActivity.this,"Please verify the details first.");

                }
            }
        });

        if(isItForItemEdit){
            foodDetailsInstanceForEdit=ResourceManager.getFoodDetailsInstance();
            Utils.displayShortToastMessage(ChefItemAddAndModifyActivity.this,"This item will be edited");

            addModifyItem.setText("Modify Item");
            foodImage.setImageResource(foodDetailsInstanceForEdit.getImage());
            foodCategoryChef.setText(foodDetailsInstanceForEdit.getItemCategory());
            foodNameChef.setText(foodDetailsInstanceForEdit.getItemName());
            foodServeTypeChef.setText(foodDetailsInstanceForEdit.getServeType());
            foodPriceChef.setText(foodDetailsInstanceForEdit.getPrice()+"");
            foodTimePrepChef.setText(foodDetailsInstanceForEdit.getPrepareTimeinMins()+"");
            foodDescChef.setText(foodDetailsInstanceForEdit.getDescription());
            addModifyItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // tO MODIFY THE MENU ITEM.
                    if (verifyDetails.isChecked()) {

                        ResourceManager.getBaseActivityInstance().getMenuItems();
                        foodDetailsForUpdate=getFoodObject(foodDetailsInstanceForEdit.getRatingStar(),foodDetailsInstanceForEdit.getItemId());
                        new AddUpdateFoodItem(ChefItemAddAndModifyActivity.this,ChefItemAddAndModifyActivity.this,foodDetailsForUpdate).execute();


                    }else{
                        Utils.displayLongToastMessage(ChefItemAddAndModifyActivity.this,"Please verify the details first.");
                    }
                }
            });

        }else{ // to ADD A NEW ITEM IN MENU
            Utils.displayShortToastMessage(ChefItemAddAndModifyActivity.this,"New item will be added here.");
            addModifyItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (verifyDetails.isChecked()) {
                        foodDetailsForUpdate=getFoodObject(5,0);
                        new AddUpdateFoodItem(ChefItemAddAndModifyActivity.this,ChefItemAddAndModifyActivity.this,foodDetailsForUpdate).execute();


                    }else{
                        Utils.displayLongToastMessage(ChefItemAddAndModifyActivity.this,"Please verify the details first.");
                    }
                }
            });
        }
    }

    private boolean areFieldsEmpty() {
        boolean flag;
        extractFieldsData();
        if (foodC.isEmpty() || foodN.isEmpty() || foodST.isEmpty() || foodD.isEmpty() || foodP.isEmpty()||foodT.isEmpty()){
            flag=true;
        }else{
            flag=false;
        }

        return flag;
    }

    private void extractFieldsData() {
        try {
            foodC = foodCategoryChef.getText().toString();
            foodN = foodNameChef.getText().toString();
            foodST = foodServeTypeChef.getText().toString();
            foodD = foodDescChef.getText().toString();
            foodT = foodTimePrepChef.getText().toString();
            foodP = foodPriceChef.getText().toString();


            foodPri = Integer.parseInt(foodP);
            foodTim = Integer.parseInt(foodT);
            foodImg = foodImage.getImageAlpha();
        }catch (Exception e){
            // Exception arises when INT fields doesnot parse empty string ""
            Utils.displayLongToastMessage(ChefItemAddAndModifyActivity.this,"Please verify the details first.");
        }
    }

    private FoodDetails getFoodObject(int rating,int foodID) {

        extractFieldsData();
        return new FoodDetails(foodC,foodN,foodST,foodD,foodPri,foodImg,foodTim,rating,foodID);
    }

    private void imageSelection() {
        final CharSequence[] options = {"Choose from Gallery","Cancel" };
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ChefItemAddAndModifyActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if(options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK && data!=null) {

            if(requestCode == PICK_IMAGE ){
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    Log.d(Constants.TAG, String.valueOf(bitmap));
                    foodImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }else{
            Utils.displayShortToastMessage(this,"Some thing went wrong.");
        }
    }

    @Override
    public void onItemAddedOrUpdated(boolean isSuccessful) {
        if(isSuccessful){
            if(isItForItemEdit){

                Utils.displayShortToastMessage(ChefItemAddAndModifyActivity.this,"Food details are modified sucessfully.");
                Intent intent = new Intent(ChefItemAddAndModifyActivity.this,ChefActivity.class);
                startActivity(intent);

            }else{
                Utils.displayShortToastMessage(ChefItemAddAndModifyActivity.this,"New item is added in Menu sucessfully");
                Intent intent = new Intent(ChefItemAddAndModifyActivity.this,ChefActivity.class);
                startActivity(intent);
            }
        }
    }

}