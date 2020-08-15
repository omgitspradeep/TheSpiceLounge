package com.sujan.info.thespicelounge.webservices;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.sujan.info.thespicelounge.BaseActivity;
import com.sujan.info.thespicelounge.Utils.AppConstants;
import com.sujan.info.thespicelounge.Utils.JSONParser;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.interfaceT.orderListWithTableListener;
import com.sujan.info.thespicelounge.models.TableInformation;

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
 * Created by pradeep on 1/8/20.
 */

public class GetOrderListWithTable extends AsyncTask<String,Integer,Boolean> {


    private ArrayList<TableInformation> tableInformationArrayList;
    private orderListWithTableListener listener;

    @SuppressLint("StaticFieldLeak")
    private BaseActivity context;

    public GetOrderListWithTable(orderListWithTableListener listener, BaseActivity activity) {
        this.listener = listener;
        this.context = activity;
    }



    @Override
    protected Boolean doInBackground(String... strings) {
        try{
            URL url=new URL(AppConstants.BASE_ADMIN_URL+AppConstants.GET_ORDERS_ITEMS_WITH_TABLE);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String line;
            StringBuilder sb = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            String s = sb.toString();
            JSONArray jsonArray = new JSONArray(s);
            System.out.println("TESTING:"+jsonArray);

            tableInformationArrayList=JSONParser.ParseOrderItemsForTable(jsonArray);
            ResourceManager.setGlobalTableInfoArraylist(tableInformationArrayList);

            return true;

        }catch (JSONException | IOException |ParseException e){
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
        if(aBoolean){
            listener.onOrdersForTableReceived(true);
        }else{
            listener.onOrdersForTableReceived(false);
        }
    }
}
