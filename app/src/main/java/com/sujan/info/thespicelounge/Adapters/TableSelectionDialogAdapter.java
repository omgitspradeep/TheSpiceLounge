package com.sujan.info.thespicelounge.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.Button;

import com.sujan.info.thespicelounge.R;

/**
 * Created by pradeep on 2/8/20.
 */

public class TableSelectionDialogAdapter extends Dialog implements View.OnClickListener{


     Button ok;
     Activity activity;

    public TableSelectionDialogAdapter(@NonNull Activity context) {
        super(context);
        this.activity=context;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_table_selection);
        ok = (Button) findViewById(R.id.table_selected_OK);
        ok.setOnClickListener(this);
    }
}
