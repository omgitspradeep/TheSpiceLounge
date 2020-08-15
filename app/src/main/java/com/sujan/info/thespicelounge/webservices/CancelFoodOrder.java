package com.sujan.info.thespicelounge.webservices;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;

import com.sujan.info.thespicelounge.BaseActivity;
import com.sujan.info.thespicelounge.MainActivity;
import com.sujan.info.thespicelounge.Utils.AppConstants;
import com.sujan.info.thespicelounge.Utils.JSONParser;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.interfaceT.cancelFoodListener;
import com.sujan.info.thespicelounge.interfaceT.orderListWithTableListener;
import com.sujan.info.thespicelounge.models.TableInformation;

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
 * Created by pradeep on 6/8/20.
 */

public class CancelFoodOrder extends AsyncTask<String,Integer,Boolean> {


    private ArrayList<TableInformation> tableInformationArrayList;
    private cancelFoodListener listener;

    @SuppressLint("StaticFieldLeak")
    private MainActivity context;
    int tableNum,orderID;

    public CancelFoodOrder(cancelFoodListener listener, MainActivity activity, int tableId, int orderId) {
        this.listener = listener;
        this.context = activity;
        this.tableNum=tableId;
        this.orderID=orderId;
    }


    @Override
    protected Boolean doInBackground(String... strings) {
        try{
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("tableNum",tableNum+"")
                    .appendQueryParameter("orderID",orderID+"");

            URL url=new URL(AppConstants.BASE_ADMIN_URL+AppConstants.CANCEL_FOOD_ORDER);
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
            String line;
            StringBuilder sb = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            String s = sb.toString();
            JSONArray jsonArray = new JSONArray(s);
            System.out.println("TESTING:"+jsonArray);

            tableInformationArrayList= JSONParser.ParseOrderItemsForTable(jsonArray);
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
            listener.onFoodCancelled(true);
        }else{
            listener.onFoodCancelled(false);
        }
    }
}