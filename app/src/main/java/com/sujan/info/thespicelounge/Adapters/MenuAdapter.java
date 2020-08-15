package com.sujan.info.thespicelounge.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.sujan.info.thespicelounge.interfaceT.MenuItemsListener;
import com.sujan.info.thespicelounge.models.FoodDetails;
import com.sujan.info.thespicelounge.webservices.GetMenuItems;

import java.util.ArrayList;

/**
 * Created by pradeep on 2/7/20.
 */

public class MenuAdapter extends ArrayAdapter<FoodDetails> implements MenuItemsListener {


    public MenuAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }


    @Override
    public void MenuItemsReceived(ArrayList<FoodDetails> foodDetails, boolean fileExists) {
        // place all the received data here
       /* if(fileExists){
            this.bookings = bookings;
            if (!showCanceled.isChecked()) {
                removeCanceled();
            }
            if (bookings.size()==0){
                placeHolder.setVisibility(View.VISIBLE);
                placeHolder.setText(Utils.getLanguageMessage("booking_err"));
            }else{
                placeHolder.setVisibility(View.INVISIBLE);
            }
            adapter = new BookingsAdapter(getActivity(), R.layout.booking_list_item, bookings);
            adapter.setListener(listener);
            listView.setAdapter(adapter);
            progressBar.setVisibility(View.INVISIBLE);
        }else {
            Utils.displayConnectivityStatus(getContext());
            progressBar.setVisibility(View.INVISIBLE);
        }*/
    }
}
