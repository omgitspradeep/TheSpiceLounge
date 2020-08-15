package com.sujan.info.thespicelounge.webservices;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;

import com.sujan.info.thespicelounge.Utils.AppConstants;
import com.sujan.info.thespicelounge.Utils.Constants;
import com.sujan.info.thespicelounge.Utils.JSONParser;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.interfaceT.cashierCheckoutListener;
import com.sujan.info.thespicelounge.interfaceT.deleteItemListener;
import com.sujan.info.thespicelounge.models.FoodDetails;
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
 * Created by pradeep on 5/8/20.
 */

public class CheckoutForCashier extends AsyncTask<String,Integer,Boolean> {

    private ArrayList<TableInformation> tableInformationArrayList;
    cashierCheckoutListener listener;
    int tableNumber;
    Activity context;

    public CheckoutForCashier(Activity activity,cashierCheckoutListener listener, int tableNum) {
        this.listener = listener;
        this.tableNumber = tableNum;
        this.context=activity;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try{

            Uri.Builder builder = new Uri.Builder().appendQueryParameter("tableNum",tableNumber+"");

            URL url=new URL(AppConstants.BASE_ADMIN_URL+AppConstants.CHECKOUT_FOR_CASHIER);
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
            tableInformationArrayList=JSONParser.ParseOrderItemsForTable(jsonArray);
            ResourceManager.setGlobalTableInfoArraylist(tableInformationArrayList);


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
        listener.onCustomerCheckoutSuccessfully(aBoolean);
    }
}