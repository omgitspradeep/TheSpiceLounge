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
import android.widget.ProgressBar;

import com.sujan.info.thespicelounge.MainActivity;
import com.sujan.info.thespicelounge.R;
import com.sujan.info.thespicelounge.Utils.Utils;
import com.sujan.info.thespicelounge.webservices.StaffLogin;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class Login extends Fragment {


    EditText userText,passwordText;
    Button login;
    MainActivity context;
    String userName,userPass;

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
        userText=(EditText)context.findViewById(R.id.input_email);
        passwordText=(EditText)context.findViewById(R.id.input_password);
        login=(Button)context.findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utils.isConnected(context)){
                    userName = userText.getText().toString();
                    userPass= passwordText.getText().toString();
                    if(validate(userName,userPass)){
                        new StaffLogin(context,context,userName,userPass).execute();
                        context.getProgressBarMain().setVisibility(View.VISIBLE);
                    }

                }else {
                    Utils.displayShortToastMessage(context,"No Internet.");
                }
            }
        });


    }


    public boolean validate(String userName,String password) {
        boolean valid = true;
        if (userName.isEmpty()) {
            userText.setError("Username field is empty");
            valid = false;
        } else {
            userText.setError(null);
        }

        if (password.isEmpty()) {
            passwordText.setError("Password Field is empty");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }

}
