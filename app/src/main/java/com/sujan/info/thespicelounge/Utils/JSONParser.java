package com.sujan.info.thespicelounge.Utils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.sujan.info.thespicelounge.models.CustomerFeedback;
import com.sujan.info.thespicelounge.models.FoodDetails;
import com.sujan.info.thespicelounge.models.OrderDetail;
import com.sujan.info.thespicelounge.models.TableInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pradeep on 2/7/20.
 */

public final class JSONParser {


    public static ArrayList<FoodDetails> ParseMenuItems(JSONArray jsonArray) throws JSONException, ParseException {
        ArrayList<FoodDetails> foodDetails = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);


            FoodDetails foodItems = new FoodDetails();

            foodItems.setItemCategory(object.getString("foodCategory"));
            foodItems.setItemName(object.getString("foodName"));
            foodItems.setServeType(object.getString("serveType"));
            foodItems.setDescription(object.getString("Description"));
            //   foodItems.setImage(object.getInt("image"));
          /*  if(object.getString("image")==null){
                foodItems.setImage(0);
            }else{
                foodItems.setImage(object.getInt("image"));
            }*/

            foodItems.setPrice(object.getInt("price"));
            foodItems.setImage(0);
            foodItems.setPrepareTimeinMins(object.getInt("prepareTime"));
            foodItems.setRatingStar(object.getInt("ratingStar"));
            foodItems.setItemId(object.getInt("foodID"));

            foodDetails.add(foodItems);
        }


        return foodDetails;
    }

    public static ArrayList<TableInformation> ParseOrderItemsForTable(JSONArray jsonArray) throws JSONException, ParseException {

        // To store the list of busy tables
        ArrayList<Integer> listOfBusyTables=new ArrayList<>();

        ArrayList<TableInformation> tableInformationArrayList = new ArrayList<TableInformation>();
        HashMap<Integer,TableInformation> tableInformationHashMap=new HashMap<Integer,TableInformation>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);

            TableInformation tableInformation = new TableInformation();

            int tableNumAsKey = object.getInt("tableNum");
            int tableStatus = object.getInt("status");

            tableInformation.setTableNum(tableNumAsKey);
            tableInformation.setNumbersOfSeat(object.getInt("tableCapacity"));
            tableInformation.setTableStatus(tableStatus);
            ArrayList<OrderDetail> orderDetailArrayList = new ArrayList<OrderDetail>();
            if(tableStatus == 1){
                JSONArray foodOrders = object.getJSONArray("orders");
                for (int j = 0; j < foodOrders.length(); j++) {
                    JSONObject innerObj = foodOrders.getJSONObject(j);

                    OrderDetail od = new OrderDetail();

                    od.setTableNum(tableNumAsKey);
                    od.setCustRequest(innerObj.getString("extraRequest"));
                    od.setQuantity(innerObj.getInt("quantity"));
                    od.setOrderID(innerObj.getInt("orderID"));
                    od.setItemId(innerObj.getInt("foodID"));
                    od.setFoodStatus(innerObj.getInt("orderStatus"));

                    orderDetailArrayList.add(od);
                }

                listOfBusyTables.add(tableNumAsKey);
            }

            tableInformation.setOrderedFoods(orderDetailArrayList);
            Log.e("TABLENUM", "TABLE IS :" + tableNumAsKey);
            tableInformationArrayList.add(tableInformation);
            tableInformationHashMap.put(tableNumAsKey,tableInformation);
            ResourceManager.setListOfFreeTables(listOfBusyTables);

        }
        ResourceManager.setGlobalTableInformationMap(tableInformationHashMap);
            return tableInformationArrayList;

    }

    public static ArrayList<CustomerFeedback> ParseFeedbackItems(JSONArray jsonArray)  throws JSONException, ParseException{
        ArrayList<CustomerFeedback> listOfFeedbacks=new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            CustomerFeedback customerFeedback=new CustomerFeedback(jsonObject.getInt("foodID"),jsonObject.getString("custName"),jsonObject.getString("feedbackMessage"),jsonObject.getInt("ratingStarCust"));
            listOfFeedbacks.add(customerFeedback);
        }

        return listOfFeedbacks;
    }

    public static ArrayList<OrderDetail> ParseAllOrders(JSONArray jsonArray) throws JSONException {
        ArrayList<OrderDetail> orderDetailArrayList=new ArrayList<OrderDetail>();

        for(int i=0;i<jsonArray.length();i++){
            OrderDetail orderDetail=new OrderDetail();
            JSONObject jsonObject=jsonArray.getJSONObject(i);

            orderDetail.setItemId(jsonObject.getInt("foodID"));
            orderDetail.setOrderID(jsonObject.getInt("orderID"));
            orderDetail.setFoodStatus(jsonObject.getInt("orderStatus"));
            orderDetail.setQuantity(jsonObject.getInt("quantity"));
            orderDetail.setCustRequest(jsonObject.getString("extraRequest"));
            orderDetail.setTableNum(jsonObject.getInt("tableNum"));

            orderDetailArrayList.add(orderDetail);
        }
        return orderDetailArrayList;
    }
}


