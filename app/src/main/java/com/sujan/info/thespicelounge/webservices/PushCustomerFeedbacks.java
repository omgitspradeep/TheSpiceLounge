package com.sujan.info.thespicelounge.webservices;

import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.RelativeLayout;

import com.sujan.info.thespicelounge.CustomerFeedbackActivity;
import com.sujan.info.thespicelounge.Utils.AppConstants;
import com.sujan.info.thespicelounge.Utils.JSONParser;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.interfaceT.feedbackAddedListener;
import com.sujan.info.thespicelounge.models.CustomerFeedback;

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

public class PushCustomerFeedbacks extends AsyncTask<String,Integer,Boolean> {
    CustomerFeedback customerFeedback;
    feedbackAddedListener mListener;

    public PushCustomerFeedbacks(feedbackAddedListener listener, CustomerFeedback customerFeedback) {
        this.customerFeedback = customerFeedback;
        this.mListener=listener;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try{

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("foodID",customerFeedback.getFoodId()+"")
                    .appendQueryParameter("custName",customerFeedback.getCustName()+"")
                    .appendQueryParameter("feedbackMessage",customerFeedback.getFeedback()+"")
                    .appendQueryParameter("ratingStarCust",customerFeedback.getRatingstar()+"");

            URL url=new URL(AppConstants.BASE_ADMIN_URL+AppConstants.PUSH_CUSTOMER_FEEDBACKS);
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

            return true;
        }catch ( IOException  e){
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
            mListener.onFBAddedMessage(aBoolean);
    }
}
