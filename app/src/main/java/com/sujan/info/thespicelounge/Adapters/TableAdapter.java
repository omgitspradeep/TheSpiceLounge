package com.sujan.info.thespicelounge.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sujan.info.thespicelounge.R;
import com.sujan.info.thespicelounge.CashierActivity;
import com.sujan.info.thespicelounge.Utils.Constants;
import com.sujan.info.thespicelounge.Utils.ResourceManager;
import com.sujan.info.thespicelounge.models.OrderDetail;
import com.sujan.info.thespicelounge.models.TableInformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pradeep on 19/7/20.
 */

public class TableAdapter extends BaseAdapter{

    private List<TableInformation> tableInformation;
    private LayoutInflater layoutInflater;
    private CashierActivity context;
    private tableSelectionInterface mTableSelectionInterface;

    public interface tableSelectionInterface{
        void onTableClick(int tableNum,ArrayList<OrderDetail> orderDetailArrayList);
    }


    public TableAdapter(CashierActivity mContext) {
        this.layoutInflater   = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context=mContext;
        this.tableInformation= ResourceManager.getGlobalTableInfoArraylist();
        this.mTableSelectionInterface=mContext;
    }



    @Override
    public int getCount() {
        return tableInformation.size();
    }

    @Override
    public TableInformation getItem(int position) {
        return tableInformation.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView           = layoutInflater.inflate(R.layout.table_grid_view, null);
            viewHolder            = new ViewHolder();
            viewHolder.noOfSeatTv  = (TextView) convertView.findViewById(R.id.Table_Activity_TV_Seats);
            viewHolder.tableNoTv = (TextView) convertView.findViewById(R.id.Table_Activity_TV_TableNo);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder) convertView.getTag();
        }




        TableInformation localObj = getItem(position);
        int tableNumber=localObj.getTableNum();
        viewHolder.tableNoTv .setText(tableNumber+"");
        viewHolder.noOfSeatTv.setText("Seats: "+localObj.getNumbersOfSeat()+"");

        ArrayList<OrderDetail> orderDetailArrayList=localObj.getOrderedFoods();
        int sizeoforders=orderDetailArrayList.size();
        if(sizeoforders!=0){
            viewHolder.tableNoTv.setBackgroundResource(R.drawable.image_activity_table_busy_table);

            // Table is clickable only if it is not free.
            viewHolder.tableNoTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mTableSelectionInterface!=null ){
                        mTableSelectionInterface.onTableClick(tableNumber,orderDetailArrayList);
                    }
                }
            });

        }else{
            viewHolder.tableNoTv.setBackgroundResource(R.drawable.image_activity_table_free_table);
        }


        return convertView;

    }


    class ViewHolder {
        TextView tableNoTv,noOfSeatTv;
    }

}
