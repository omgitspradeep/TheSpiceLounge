package com.sujan.info.thespicelounge.webservices;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.sujan.info.thespicelounge.MainActivity;
import com.sujan.info.thespicelounge.Utils.AppConstants;
import com.sujan.info.thespicelounge.Utils.Constants;
import com.sujan.info.thespicelounge.Utils.JSONParser;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.interfaceT.feedbackListener;
import com.sujan.info.thespicelounge.models.CustomerFeedback;
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
 * Created by pradeep on 2/8/20.
 */

public class GetCustomerFeedbacks extends AsyncTask<String,Integer,Boolean> {
    feedbackListener fbListener;
    private ArrayList<CustomerFeedback> customerFeedbacks;
    int foodID;
    ProgressBar pb;

    public GetCustomerFeedbacks(feedbackListener fbListener, int foodId, ProgressBar pb) {
        this.fbListener = fbListener;
        this.foodID=foodId;
        this.pb=pb;
    }


    @Override
    protected Boolean doInBackground(String... strings) {
        try{

            Uri.Builder builder = new Uri.Builder().appendQueryParameter("foodID",foodID+"");
            URL url=new URL(AppConstants.BASE_ADMIN_URL+AppConstants.GET_CUSTOMER_FEEDBACKS);
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
            JSONArray jsonArray = new JSONArray(s);

            customerFeedbacks=JSONParser.ParseFeedbackItems(jsonArray);
            System.out.println("Final response===="+jsonArray);

            return true;
        }catch (JSONException | IOException | ParseException e){
            e.printStackTrace();
            pb.setVisibility(View.GONE);
            fbListener.onFeedbackReceived(customerFeedbacks,foodID,pb,false);
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
        fbListener.onFeedbackReceived(customerFeedbacks,foodID,pb,true);
    }
}
