package com.sujan.info.thespicelounge.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.sujan.info.thespicelounge.CashierActivity;
import com.sujan.info.thespicelounge.MainActivity;
import com.sujan.info.thespicelounge.R;
import com.sujan.info.thespicelounge.fragments.CustomerOrderdListFragment;
import com.sujan.info.thespicelounge.interfaceT.cancelFoodListener;
import com.sujan.info.thespicelounge.interfaceT.cashierCheckoutListener;
import com.sujan.info.thespicelounge.interfaceT.deleteItemListener;
import com.sujan.info.thespicelounge.models.FoodDetails;
import com.sujan.info.thespicelounge.models.OrderDetail;
import com.sujan.info.thespicelounge.webservices.CancelFoodOrder;
import com.sujan.info.thespicelounge.webservices.CheckoutForCashier;
import com.sujan.info.thespicelounge.webservices.DeleteMenuItem;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pradeep on 3/7/20.
 */

public class Utils {

    public static void displayShortToastMessage(Context context,String message) {
            Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }

    public static void displayLongToastMessage(Context context,String message) {
        Toast.makeText(context,message, Toast.LENGTH_LONG).show();
    }



    public  void displaySnackBarMessage(final Context context, String message) {

    }

    public static boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting());
        } else return false;
    }

    public static void buildAskTableDialog(final MainActivity activity) {

        // Here, customer returns the table number

        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_table_selection, null, false);

       /*HERE  FIND  IDS AND SET TEXTS OR BUTTONS*/
        Spinner busyTableSpinner=(Spinner)view.findViewById(R.id.free_table_spinner);
        Button okButton=(Button)view.findViewById(R.id.table_selected_OK);
        ((Activity) activity).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(
                activity,
                android.R.layout.simple_spinner_item,
                ResourceManager.getListOfFreeTables()
        );
        busyTableSpinner.setAdapter(adapter);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = busyTableSpinner.getSelectedItem().toString();
                Utils.displayLongToastMessage(activity,"You just clicked table number "+text);
                ResourceManager.setSelectedTable(Integer.parseInt(text));
                dialog.dismiss();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment,new CustomerOrderdListFragment(activity)).commit();


            }
        });
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.show();
    }

    public static void showAlertDialogBeforeDelete(Activity activity, deleteItemListener listener, int foodId) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage("Are you sure?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        new DeleteMenuItem(activity,listener,foodId).execute();

                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public static void showAlertDialogBeforeCheckout(CashierActivity activity, cashierCheckoutListener listener, int tableID) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage("Are you sure?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        activity.progressBarVisibility(1);
                        new CheckoutForCashier(activity,listener,tableID).execute();

                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    public static void showAlertDialogBeforeCancelOrder(MainActivity activity, cancelFoodListener listener, int tableum,int orderID) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage("Are you sure?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        activity.getProgressBarMain().setVisibility(View.VISIBLE);
                        new CancelFoodOrder(listener,activity,tableum,orderID).execute();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public static void activityTransitionRemovingHistory(Activity mContext,Intent intent){
      /*  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.finish();
        mContext.startActivity(intent);

    }



}
