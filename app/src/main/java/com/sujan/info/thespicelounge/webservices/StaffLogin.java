package com.sujan.info.thespicelounge.webservices;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.sujan.info.thespicelounge.Utils.AppConstants;
import com.sujan.info.thespicelounge.Utils.Constants;
import com.sujan.info.thespicelounge.Utils.JSONParser;
import com.sujan.info.thespicelounge.Utils.MyPreferences;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.interfaceT.allOrderedListListener;
import com.sujan.info.thespicelounge.interfaceT.loginListener;
import com.sujan.info.thespicelounge.models.OrderDetail;
import com.sujan.info.thespicelounge.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StaffLogin extends AsyncTask<String,Integer,Boolean> {
    User loggedStaff;
    loginListener listener;
    Context mContext;
    String usrname,password;


    public StaffLogin( loginListener staffloginListener, Context context, String usr, String pass) {
        this.listener = staffloginListener;
        this.mContext= context;
        this.usrname=usr;
        this.password=pass;
    }


    @Override
    protected Boolean doInBackground(String... strings) {
        try{


            Uri.Builder builder = new Uri.Builder().appendQueryParameter("usrname",usrname).appendQueryParameter("pass",password);
            URL url=new URL(AppConstants.BASE_ADMIN_URL+AppConstants.LOGIN_STAFF);
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
            System.out.println("User data ===="+s);

            loggedStaff = JSONParser.ParseStaff(new JSONObject(s));
            System.out.println("User data ===="+loggedStaff.getUsername());

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
            ResourceManager.setLoggedStaff(loggedStaff);
            String employeeTitle=loggedStaff.getFirstname()+" "+loggedStaff.getLastName()+" ( "+loggedStaff.getPost()+ " )";
            MyPreferences.setStringPrefrences(Constants.EMPLOYEE_NAME_TITLE,employeeTitle,mContext);

            listener.onLoginResponse(true);
        }else{
            listener.onLoginResponse(false);
        }
    }


}