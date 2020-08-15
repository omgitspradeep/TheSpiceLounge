package com.sujan.info.thespicelounge.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sujan.info.thespicelounge.AdminActivity;
import com.sujan.info.thespicelounge.ChefActivity;
import com.sujan.info.thespicelounge.MainActivity;
import com.sujan.info.thespicelounge.R;
import com.sujan.info.thespicelounge.CashierActivity;
import com.sujan.info.thespicelounge.Utils.Constants;
import com.sujan.info.thespicelounge.Utils.MyPreferences;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.WaiterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class Login extends Fragment {


    EditText userText,passwordText;
    Button login;
    MainActivity context;
    String userName,userPass;
    private RequestQueue mQueue;
    public String jsonUsername,jsonUserPass;

    public Login() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context= (MainActivity) getActivity();
        mQueue= Volley.newRequestQueue(context);
        userText=(EditText)context.findViewById(R.id.input_email);
        passwordText=(EditText)context.findViewById(R.id.input_password);
        login=(Button)context.findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utils.isConnected(context)){
                    userName = userText.getText().toString();
                    userPass= passwordText.getText().toString();

                    //Temporary
                    if(userName.equals("admin") ){
                        //login for admin or manager : It is supposed to be redirect to webview,
                        MyPreferences.setBooleanPrefrences(Constants.IS_USER_LOGGED_IN,true,context);
                        MyPreferences.setIntPrefrences(Constants.LAST_LOGGED_USER,Constants.ADMIN_ID,context);
                        Utils.activityTransitionRemovingHistory(context, new Intent(context, AdminActivity.class));

                    }else if(userName.equals("chef")){
                        //login for chef,
                        MyPreferences.setBooleanPrefrences(Constants.IS_USER_LOGGED_IN,true,context);
                        MyPreferences.setIntPrefrences(Constants.LAST_LOGGED_USER,Constants.CHEF_ID,context);
                        Utils.activityTransitionRemovingHistory(context, new Intent(context, ChefActivity.class));

                    }else if(userName.equals("waiter")){
                        //login for waiter
                        MyPreferences.setBooleanPrefrences(Constants.IS_USER_LOGGED_IN,true,context);
                        MyPreferences.setIntPrefrences(Constants.LAST_LOGGED_USER,Constants.WAITER_ID,context);
                        Utils.activityTransitionRemovingHistory(context, new Intent(context, WaiterActivity.class));


                    }else if(userName.equals("cashier")){
                       /* When user opens app without Internet then only foodmenu list is available as saved data  but not food orders with table
                        As cashier logs in and internet is available then app crashes since there is no data about tables.
                                Therefore, we need to bring table data.*/
                        if(ResourceManager.isFoodOrdersWithTableDataAvailable()){
                            //login for cashier
                            MyPreferences.setBooleanPrefrences(Constants.IS_USER_LOGGED_IN,true,context);
                            MyPreferences.setIntPrefrences(Constants.LAST_LOGGED_USER,Constants.CASHIER_ID,context);
                            Utils.activityTransitionRemovingHistory(context, new Intent(context, CashierActivity.class));
                        }else{
                            Utils.displayShortToastMessage(context,"Please restart app once again.");
                        }

                    }
                }else {
                    Utils.displayShortToastMessage(context,"No Internet.");
                }



                //Parmanent
                /*jsonParse();


                if(validate(userName,userPass)){
                    if(jsonUsername.equals("admin") ){
                        //login for admin or manager : It is supposed to be redirect to webview,
                        Intent intent = new Intent(context, AdminActivity.class);
                        startActivity(intent);

                    }else if(jsonUsername.equals("chef")){
                        //login for chef,
                        Intent intent = new Intent(context, ChefActivity.class);
                        startActivity(intent);

                    }else if(jsonUsername.equals("waiter")){
                        //login for waiter
                        Intent intent = new Intent(context, WaiterActivity.class);
                        startActivity(intent);

                    }else if(jsonUsername.equals("cashier")){
                        //login for cashier
                        Intent intent = new Intent(context, CashierActivity.class);
                        startActivity(intent);


                    }
                }*/

            }
        });


    }

    private void jsonParse() {
        String url="https://thespicelounge.000webhostapp.com/logindetails.php";
        JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String jsonUsername=response.getString("name");
                    Toast.makeText(context,"I am "+jsonUsername,Toast.LENGTH_LONG).show();
                //    String jsonUserPass=response.getString("pass");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);

    }

    public boolean validate(String email,String password) {
        boolean valid = true;



        if (email.isEmpty()) {
            userText.setError("enter a valid username");
            valid = false;
        } else {
            userText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }

}
