package com.sujan.info.thespicelounge.webservices;

import android.os.AsyncTask;

import com.sujan.info.thespicelounge.Utils.AppConstants;
import com.sujan.info.thespicelounge.Utils.Constants;
import com.sujan.info.thespicelounge.Utils.JSONParser;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.interfaceT.allOrderedListListener;
import com.sujan.info.thespicelounge.models.OrderDetail;

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
 * Created by pradeep on 3/8/20.
 */

public class GetOrdersList extends AsyncTask<String,Integer,Boolean>{
    ArrayList<OrderDetail> orderDetailArrayList;
    allOrderedListListener listener;

    public GetOrdersList( allOrderedListListener orderedListListener) {
        this.listener = orderedListListener;
    }


    @Override
    protected Boolean doInBackground(String... strings) {
        try{
            URL url=new URL(AppConstants.BASE_ADMIN_URL+AppConstants.GET_ALL_ORDERS);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String line;
            StringBuilder sb = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            String s = sb.toString();
            JSONArray jsonArray = new JSONArray(s);

            orderDetailArrayList = JSONParser.ParseAllOrders(jsonArray);
            System.out.println("Final Orders===="+jsonArray);
            System.out.println("Final Orders===="+orderDetailArrayList.size());


            return true;

        }catch (JSONException | IOException e){
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
            listener.onAllOrderedListReceiver(orderDetailArrayList,true);
        }else{
            listener.onAllOrderedListReceiver(orderDetailArrayList,false);
        }
    }

    // SAMPLE OF RECEIVED JSON

/*    [
    {
        "orderID": "1",
            "foodID": "10",
            "tableNum": "1",
            "orderStatus": "2",
            "quantity": "2",
            "extraRequest": "Make it little spicy"
    },
    {
        "orderID": "2",
            "foodID": "7",
            "tableNum": "2",
            "orderStatus": "1",
            "quantity": "3",
            "extraRequest": "with coke"
    },
    {
        "orderID": "3",
            "foodID": "11",
            "tableNum": "1",
            "orderStatus": "3",
            "quantity": "3",
            "extraRequest": "Bring with tomato ketchup"
    },
    {
        "orderID": "4",
            "foodID": "15",
            "tableNum": "6",
            "orderStatus": "3",
            "quantity": "3",
            "extraRequest": "Make it little crispy"
    }
]*/
}
