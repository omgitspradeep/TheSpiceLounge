package com.sujan.info.thespicelounge.webservices;

import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;

import com.sujan.info.thespicelounge.Utils.AppConstants;
import com.sujan.info.thespicelounge.Utils.JSONParser;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.interfaceT.takeOrderListener;
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
 * Created by pradeep on 4/8/20.
 */

public class PushFoodOrder extends AsyncTask<String,Integer,Boolean> {

    private ArrayList<TableInformation> tableInformationArrayList;
    OrderDetail orderDetail;
    takeOrderListener listener;

    public PushFoodOrder(OrderDetail orderDetail,takeOrderListener mListener) {
        this.orderDetail = orderDetail;
        this.listener=mListener;
    }


    @Override
    protected Boolean doInBackground(String... strings) {
        try{

            String foodID=orderDetail.getItemId()+"";
            String tableNum=orderDetail.getTableNum()+"";
            String quantity=orderDetail.getQuantity()+"";
            String custRequest=orderDetail.getCustRequest()+"";


            System.out.println("REQUEST PARAMETERS ARE===" + foodID+"  "+tableNum+"   "+quantity+"   "+custRequest);

            Uri.Builder builder = new Uri.Builder().appendQueryParameter("foodID",foodID)
                    .appendQueryParameter("tableNum",tableNum)
                    .appendQueryParameter("quantity",quantity)
                    .appendQueryParameter("extraRequest",custRequest);

            URL url=new URL(AppConstants.BASE_ADMIN_URL+AppConstants.PUSH_CUSTOMER_ORDER);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String query = builder.build().getEncodedQuery();

            System.out.println("REQUEST PARAMETERS ARE===" + query);

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
            System.out.println("FEEDBACK ADDED RESUTLT:"+s);

            JSONArray jsonArray = new JSONArray(s);
            System.out.println("TESTING 2:"+jsonArray);

            // List of orders for each table is received here.
            tableInformationArrayList=JSONParser.ParseOrderItemsForTable(jsonArray);

            // It is set as global since customer have to see updated orders for his table.
            ResourceManager.setGlobalTableInfoArraylist(tableInformationArrayList);


            return true;
        }catch (IOException |JSONException |ParseException e){
            e.printStackTrace();
        }

        return false;
    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        listener.onOrderReceived(aBoolean);
    }
}
