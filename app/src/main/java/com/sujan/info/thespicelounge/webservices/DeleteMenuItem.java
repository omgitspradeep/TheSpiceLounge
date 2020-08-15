package com.sujan.info.thespicelounge.webservices;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;

import com.sujan.info.thespicelounge.Utils.AppConstants;
import com.sujan.info.thespicelounge.Utils.Constants;
import com.sujan.info.thespicelounge.Utils.JSONParser;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.interfaceT.deleteItemListener;
import com.sujan.info.thespicelounge.models.FoodDetails;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by pradeep on 5/8/20.
 */

public class DeleteMenuItem extends AsyncTask<String,Integer,Boolean> {

    private ArrayList<FoodDetails> foodDetailsArrayList;
    deleteItemListener listener;
    int foodId;
    Activity context;

    public DeleteMenuItem(Activity activity,deleteItemListener listener, int foodId) {
        this.listener = listener;
        this.foodId = foodId;
        this.context=activity;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try{

            Uri.Builder builder = new Uri.Builder().appendQueryParameter("foodID",foodId+"");

            URL url=new URL(AppConstants.BASE_ADMIN_URL+AppConstants.DELETE_MENU_ITEM);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String query = builder.build().getEncodedQuery();

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            //Send request
            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream ());
            wr.writeBytes (query);
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream isNew = urlConnection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(isNew));

            String line;
            StringBuilder sb = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            rd.close();

            String s = sb.toString();
            JSONArray jsonArray = new JSONArray(s);
            ResourceManager.writeToFile(s, Constants.MENU_FILE, context); // FOr offline uses
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
        listener.onMenuItemDeleted(aBoolean);
    }
}
