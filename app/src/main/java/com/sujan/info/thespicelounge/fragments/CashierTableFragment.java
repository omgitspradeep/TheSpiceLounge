package com.sujan.info.thespicelounge.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.sujan.info.thespicelounge.Adapters.TableAdapter;
import com.sujan.info.thespicelounge.R;
import com.sujan.info.thespicelounge.CashierActivity;
import com.sujan.info.thespicelounge.models.TableInformation;

import org.json.JSONArray;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class CashierTableFragment extends Fragment {


    private String baseUrl;
    private JSONArray socketData;
    private GridView tableGridView;
    public TableInformation selectedTableInfo;
    public TextView restNameTV;
    public TableAdapter tableAdapter;
    public  TextView clickedView;
    public CashierActivity mContext;

    @SuppressLint("ValidFragment")
    public CashierTableFragment(CashierActivity temp) {
        this.mContext=temp;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cashier_table, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        restNameTV           = view.findViewById(R.id.Cashier_table_TV_ResturantName);
        tableGridView        = view.findViewById(R.id.Cashier_GV_Tables);
        tableAdapter         = new TableAdapter(mContext);
        mContext.setFabForRefreshVisible();
        tableGridView.setAdapter(tableAdapter);
    }

}
