package com.sujan.info.thespicelounge.interfaceT;

import com.sujan.info.thespicelounge.models.FoodDetails;
import com.sujan.info.thespicelounge.models.OrderDetail;

import java.util.ArrayList;

/**
 * Created by pradeep on 3/8/20.
 */

public interface allOrderedListListener {
    public void onAllOrderedListReceiver(ArrayList<OrderDetail> orderDetailArrayList,boolean isSuccess);
}
