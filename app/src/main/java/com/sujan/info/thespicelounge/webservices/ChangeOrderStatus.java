package com.sujan.info.thespicelounge.webservices;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.sujan.info.thespicelounge.Utils.AppConstants;
import com.sujan.info.thespicelounge.Utils.JSONParser;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.interfaceT.cashierCheckoutListener;
import com.sujan.info.thespicelounge.interfaceT.orderStatusUpdateListener;
import com.sujan.info.thespicelounge.models.OrderDetail;
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

public class ChangeOrderStatus extends AsyncTask<String,Integer,Boolean> {

    orderStatusUpdateListener listener;
    int orderID;
    int orderStatus;
    private Activity context;

    public ChangeOrderStatus(Activity activity,orderStatusUpdateListener listener, int orderID,int orderStat) {
        this.listener = listener;
        this.orderID = orderID;
        // Since status must be incremented per click  (0: waiting,1:cooking, 2:cooked, 3:served)
        this.orderStatus=orderStat+1;
        this.context=activity;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try{

            Log.e("QWERTY",orderID+":"+orderStatus);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("orderID",orderID+"")
                    .appendQueryParameter("orderStatus",orderStatus+"");

            URL url=new URL(AppConstants.BASE_ADMIN_URL+AppConstants.UPDATE_ORDER_STATUS);
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

            Log.e("QWERTY",s);
            ArrayList<OrderDetail> orderDetailArrayList = JSONParser.ParseAllOrders(jsonArray);
            ResourceManager.setAllOrders(orderDetailArrayList);

            return true;
        }catch (IOException |JSONException e){
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
        listener.onOrderStatusUpdated(aBoolean);
    }
}