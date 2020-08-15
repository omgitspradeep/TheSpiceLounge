package com.sujan.info.thespicelounge.webservices;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.sujan.info.thespicelounge.Utils.AppConstants;
import com.sujan.info.thespicelounge.Utils.Constants;
import com.sujan.info.thespicelounge.Utils.JSONParser;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.interfaceT.deleteItemListener;
import com.sujan.info.thespicelounge.interfaceT.menuItemAddUpdateListener;
import com.sujan.info.thespicelounge.models.FoodDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by pradeep on 5/8/20.
 */

public class AddUpdateFoodItem extends AsyncTask<String,Integer,Boolean> {

    private ArrayList<FoodDetails> foodDetailsArrayList;
    menuItemAddUpdateListener listener;
    FoodDetails foodDetails;
    Activity context;

    public AddUpdateFoodItem(Activity activity,menuItemAddUpdateListener listener, FoodDetails fd) {
        this.listener = listener;
        this.foodDetails = fd;
        this.context=activity;
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        try{

            URL url=new URL(AppConstants.BASE_ADMIN_URL+AppConstants.ADD_UPDATE_MENU_ITEM);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            String jsonString=foodDetails.toJSON();

            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }



            String line;
            StringBuilder sb = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }

            String s = sb.toString();
            JSONArray jsonArray = new JSONArray(s);
            Log.e("Hoooooooo",s);
            ResourceManager.writeToFile(s, Constants.MENU_FILE, context); // For offline uses
            foodDetailsArrayList = JSONParser.ParseMenuItems(jsonArray);

            ResourceManager.setGlobalFoodMenu(foodDetailsArrayList);  // to store all foodMenu serially to inflate in foodMenu adapter
            ResourceManager.setFoodMenuHashMap(foodDetailsArrayList);  // to get item detail using "foodId"

            return true;
        }catch (IOException |JSONException |ParseException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        listener.onItemAddedOrUpdated(aBoolean);
    }
}