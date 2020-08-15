package com.sujan.info.thespicelounge.webservices;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;

import com.sujan.info.thespicelounge.BaseActivity;
import com.sujan.info.thespicelounge.Utils.AppConstants;
import com.sujan.info.thespicelounge.Utils.Constants;
import com.sujan.info.thespicelounge.Utils.JSONParser;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.interfaceT.MenuItemsListener;
import com.sujan.info.thespicelounge.models.FoodDetails;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by pradeep on 2/7/20.
 */

public class GetMenuItems extends AsyncTask<String,Integer,Boolean> {


    private ArrayList<FoodDetails> foodDetails;
    private MenuItemsListener listener;
    @SuppressLint("StaticFieldLeak")
    private BaseActivity activity;


    public GetMenuItems(BaseActivity activity, MenuItemsListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try{
            URL url=new URL(AppConstants.BASE_ADMIN_URL+AppConstants.GET_MENU_ITEMS);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String line;
            StringBuilder sb = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            String s = sb.toString();
            JSONArray jsonArray = new JSONArray(s);
//            ResourceManager.saveJSONToFile(jsonArray, "MenuItems.txt", activity);
            ResourceManager.writeToFile(s, Constants.MENU_FILE, activity);
            foodDetails = JSONParser.ParseMenuItems(jsonArray);
            return true;

        }catch (JSONException | IOException |ParseException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        if(aBoolean){
            listener.MenuItemsReceived(foodDetails,true);
        }else{
            listener.MenuItemsReceived(null,false);
            Utils.displayShortToastMessage(activity,"Something went wrong");
        }
    }

}
