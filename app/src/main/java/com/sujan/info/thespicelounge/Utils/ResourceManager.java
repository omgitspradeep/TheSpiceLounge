package com.sujan.info.thespicelounge.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sujan.info.thespicelounge.BaseActivity;
import com.sujan.info.thespicelounge.MainActivity;
import com.sujan.info.thespicelounge.R;
import com.sujan.info.thespicelounge.WaiterActivity;
import com.sujan.info.thespicelounge.fragments.CustomerOrderdListFragment;
import com.sujan.info.thespicelounge.models.CustomerFeedback;
import com.sujan.info.thespicelounge.models.FoodDetails;
import com.sujan.info.thespicelounge.models.OrderDetail;
import com.sujan.info.thespicelounge.models.TableInformation;
import com.sujan.info.thespicelounge.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pradeep on 2/7/20.
 */

public class ResourceManager {


    public static Activity generalActivity;
    private static ResourceManager ourInstance = new ResourceManager();
    public static String activeTableForCurrentCustomer;    // It is set when user first orders his food and whenever user requests to view his orders this table number is used as reference for this device

    public static BaseActivity baseActivityInstance;
    public static FoodDetails foodDetailsInstance;  // This instance is set when user selects the food items from food menu
    public static WaiterActivity waiterActivityInstance; // It is used to show/hide fab in Home fragment when orders are displayed in Waiter activity.

    public static ArrayList<FoodDetails> globalFoodMenuArraylist;   // to store all foodMenu serially to inflate in foodMenu adapter
    public static ArrayList<TableInformation> globalTableInfoArraylist;

    public static HashMap<Integer,FoodDetails> globalFoodMenuMap; // Key: foodID
    public static HashMap<Integer,TableInformation> globalTableInformationMap; // key: tableNum

    public static ArrayList<CustomerFeedback> customerFeedbackArrayList;
    public static ArrayList<OrderDetail> allOrders;    // arraylist that holds all the list of orders.

    public static List<Integer> listOfBusyTables;
    public static int selectedTable;

    public static User loggedStaff;

    public static User getLoggedStaff() {
        return loggedStaff;
    }

    public static void setLoggedStaff(User loggedStaff) {
        ResourceManager.loggedStaff = loggedStaff;
    }

    public static WaiterActivity getWaiterActivityInstance() {
        return waiterActivityInstance;
    }

    public static void setWaiterActivityInstance(WaiterActivity waiterActivityInstance) {
        ResourceManager.waiterActivityInstance = waiterActivityInstance;
    }

    public static ArrayList<OrderDetail> getAllOrders() {
        return allOrders;
    }

    public static void setAllOrders(ArrayList<OrderDetail> allOrders) {
        ResourceManager.allOrders = allOrders;
    }

    public static void addNewCustomerFeedBack(CustomerFeedback customerFeedback) {
        customerFeedbackArrayList.add(customerFeedback);
    }

    public static ArrayList<CustomerFeedback> getCustomerFeedbackArrayList() {
        return customerFeedbackArrayList;
    }

    public static void setCustomerFeedbackArrayList(ArrayList<CustomerFeedback> customerFeedbackArrayList) {
        ResourceManager.customerFeedbackArrayList = customerFeedbackArrayList;
    }

    public static HashMap<Integer, TableInformation> getGlobalTableInformationMap() {
        return globalTableInformationMap;
    }

    public static void setGlobalTableInformationMap(HashMap<Integer, TableInformation> globalTableInformationMap) {
        ResourceManager.globalTableInformationMap = globalTableInformationMap;
    }

    public static int getSelectedTable() {
        return selectedTable;
    }

    public static void setSelectedTable(int selectedTable) {
        ResourceManager.selectedTable = selectedTable;
    }

    public static FoodDetails getFoodDetailsInstance() {
        return foodDetailsInstance;
    }

    public static void setFoodDetailsInstance(FoodDetails foodDetailsInstance) {
        ResourceManager.foodDetailsInstance = foodDetailsInstance;
    }

    public static ArrayList<FoodDetails> getGlobalFoodMenu() {
        return globalFoodMenuArraylist;
    }

    public static void setGlobalFoodMenu(ArrayList<FoodDetails> globalFoodMenu) {
        ResourceManager.globalFoodMenuArraylist = globalFoodMenu;
    }


    public static List<Integer> getListOfFreeTables() {
        return listOfBusyTables;
    }

    public static void setListOfFreeTables(List<Integer> listOfFreeTables) {
        ResourceManager.listOfBusyTables = listOfFreeTables;
    }

    public static BaseActivity getBaseActivityInstance() {
        return baseActivityInstance;
    }




    public static void setFoodMenuHashMap(ArrayList<FoodDetails> foodDetailsArrayList){

        HashMap<Integer,FoodDetails> hm = new HashMap<Integer,FoodDetails>();
        for(int i=0;i<foodDetailsArrayList.size();i++){
            FoodDetails fd=foodDetailsArrayList.get(i);
            hm.put(fd.getItemId(),fd);
        }
        globalFoodMenuMap=hm;
    }

    public static String getActiveTableForCurrentCustomer() {
        return activeTableForCurrentCustomer;
    }

    public static void setActiveTableForCurrentCustomer(String activeTableForCurrentCustomer) {
        ResourceManager.activeTableForCurrentCustomer = activeTableForCurrentCustomer;
    }
    public static HashMap<Integer,FoodDetails> getGlobalFoodMenuMap() {
        return globalFoodMenuMap;
    }

    public static ArrayList<TableInformation> getGlobalTableInfoArraylist() {
        return globalTableInfoArraylist;
    }

    public static void setGlobalTableInfoArraylist(ArrayList<TableInformation> globalTableInfoArraylist) {
        ResourceManager.globalTableInfoArraylist = globalTableInfoArraylist;
    }

    public static void setBaseActivityInstance(BaseActivity baseActivityInstance) {
        ResourceManager.baseActivityInstance = baseActivityInstance;
    }



    public static boolean isMenuFileExists(String filename,BaseActivity activity){
        boolean flag=false;
        String path=activity.getFilesDir().getPath()+"/";
        File file = new File(path,filename);
        if (file.exists()) {
            flag=true;
        }
        return flag;
    }

    public static boolean isFileExists(String[] filename,BaseActivity activity){

        boolean flag=false;
        String path=activity.getFilesDir().getPath()+"/";
        for (String aFilename : filename) {
            File file = new File(path, aFilename);
            if (file.exists()) {
                flag = true;
            } else {
                return false;
            }
        }

        return flag;
    }

    public static void writeToFile(String data,String filename,Activity context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    public static String readFromFile(String filename,Activity context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }


    public static String getOrderStatus(int status) {
        String strStatus="waiting";
        switch (status){
            case Constants.WAITING:
                strStatus= "waiting";               break;

            case Constants.COOKING:
                strStatus= "cooking";               break;

            case Constants.COOKED:
                strStatus= "cooked";                break;

            case Constants.SERVED:
                strStatus= "served";                break;

        }
        return strStatus;
    }

    public static FoodDetails getFoodDetails(int foodID){
        return globalFoodMenuMap.get(foodID);
    }



    public static String getFoodName(FoodDetails foodDetails){
        return foodDetails.getItemName()+" "+foodDetails.getItemCategory()+"( "+foodDetails.getServeType()+" )";
    }

    public static List<FoodDetails> createFoodMenu() {
        ArrayList<FoodDetails> menuList=new ArrayList<FoodDetails>();

        menuList.add(new FoodDetails( "Mo mo", "Chicken", "Steam", "It have chicken in it. We take the best raw material in town and the brilliant chef you could find in the area make it such a delicacy that you would not forget us for long.", 150, R.drawable.momos,5,3,1));
        menuList.add(new FoodDetails( "Mo mo", "Chicken", "Fried", "It have chicken in it.", 200,R.drawable.momos,10,4,2));
        menuList.add(new FoodDetails( "Mo mo", "Chicken", "Spicy", "It have chicken in it.", 2250,R.drawable.momos,15,5,3));
        menuList.add(new FoodDetails( "Mo mo", "Chicken", "Spice Latty", "It have chicken in it.", 260,R.drawable.momos,25,4,4));

        menuList.add(new FoodDetails( "Mo mo", "Mutton", "Steam", "It have Mutton in it.", 233,R.drawable.momos,20,3,5));
        menuList.add(new FoodDetails( "Mo mo", "Mutton", "Fried", "It have Mutton in it.", 240,R.drawable.momos,15,4,7));
        menuList.add(new FoodDetails( "Mo mo", "Mutton", "Spicy", "It have Mutton in it.", 250,R.drawable.momos,20,4,7));
        menuList.add(new FoodDetails( "Mo mo", "Mutton", "Spice Latty", "It have Mutton in it.", 260,R.drawable.momos,25,5,8));

        menuList.add(new FoodDetails( "Chowmen", "Chicken", "jhol", "It have chicken in it.", 150,R.drawable.chowmean,15,3,9));
        menuList.add(new FoodDetails( "Chowmen", "Mutton", "dry", "It have Mutton in it.", 200,R.drawable.chowmean,25,4,10));
        menuList.add(new FoodDetails( "Chowmen", "Veg", "dry", "It have vegetables in it.", 100,R.drawable.chowmean,10,5,11));
        menuList.add(new FoodDetails( "Chowmen", "Chicken", "dry", "It have chicken in it.", 160,R.drawable.chowmean,15,4,12));

        menuList.add(new FoodDetails( "Burger", "Chicken", "normal", "It have chicken in it.", 80,R.drawable.burger,20,3,13));
        menuList.add(new FoodDetails( "Burger", "Mutton", "normal", "It have Mutton in it.", 120,R.drawable.burger,25,4,14));
        menuList.add(new FoodDetails( "Burger", "Veg", "normal", "It have vegetables in it.", 150,R.drawable.burger,30,5,15));

        menuList.add(new FoodDetails( "Burger", "Chicken", "normal", "It have chicken in it.", 150,R.drawable.burger,25,4,26));
        menuList.add(new FoodDetails( "Burger", "Mutton", "normal", "It have Mutton in it.", 200,R.drawable.burger,30,4,27));
        menuList.add(new FoodDetails( "Burger", "Veg", "normal", "It have vegetables in it.", 250,R.drawable.burger,35,4,28));

        return menuList;

    }



    public static boolean isAllFoodOrdersAvailable() {
            boolean flag=false;
        if (allOrders instanceof ArrayList<?>) {
            flag=true;
        }

        return flag;
    }


    public static boolean isFoodOrdersWithTableDataAvailable() {
        boolean flag=false;
        if (globalTableInfoArraylist instanceof ArrayList<?>) {
            flag=true;
        }

        return flag;
    }

    public static Integer[] getListOfTableNumber() {
        int size=globalTableInfoArraylist.size();
        Integer[] arr=  new Integer[size];
        for(int i=0;i<size;i++){
            arr[i]=i+1;
        }
        Log.e("SIZEy",arr.length+" is the no. of tables");
        return arr;
    }



     /*   public static ArrayList<TableInformation> getTableInformationArrayList(String jsonString) {

        ArrayList<TableInformation> tableInformationArrayList = new ArrayList<TableInformation>();

        try {

            JSONObject jObject = new JSONObject(jsonString);
            JSONArray jsonArray = jObject.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject innerJsonObject = jsonArray.getJSONObject(i);
                int tableNumber = innerJsonObject.getInt("tableNum");
                int tableCapacity = innerJsonObject.getInt("numOfSeats");
                JSONArray innerJsonArray = innerJsonObject.getJSONArray("orderDetails");

                ArrayList<OrderDetail> orderDetailArrayList = new ArrayList<OrderDetail>();

                if (innerJsonArray.length() != 0) {
                    for (int j = 0; j < innerJsonArray.length(); j++) {

                        JSONObject deepInnerJsonObject = innerJsonArray.getJSONObject(j);
                        String itemCategory = deepInnerJsonObject.getString("itemCategory");
                        String itemName = deepInnerJsonObject.getString("itemName");
                        String serveType = deepInnerJsonObject.getString("serveType");
                        String custRequest = deepInnerJsonObject.getString("custRequest");
                        int price = deepInnerJsonObject.getInt("price");
                        int itemId = deepInnerJsonObject.getInt("itemId");
                        int timeRemainToServe = deepInnerJsonObject.getInt("time");
                        int status = deepInnerJsonObject.getInt("status");
                        int orderID = deepInnerJsonObject.getInt("orderID");
                        int ratingStar = deepInnerJsonObject.getInt("ratingStar");
                        int quantity = deepInnerJsonObject.getInt("quantity");
                   //     int foodImage = deepInnerJsonObject.getInt("image");
                        int foodImage=0;

                        orderDetailArrayList.add(new OrderDetail(itemId, status, quantity,orderID, foodImage,custRequest));
           //             orderDetailArrayList.add(new OrderDetail(itemCategory, itemName, serveType, price, itemId, timeRemainToServe, status, ratingStar, quantity,orderID, foodImage,custRequest));


                    }
                }

                tableInformationArrayList.add(new TableInformation(orderDetailArrayList, tableNumber, tableCapacity));
            }

            Log.e("SIZEEE", "Numbers of tables are :  " + tableInformationArrayList.size());

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("SIZEEE", " " + e);

        }
        return tableInformationArrayList;

    }*/

      /*   public static void saveJSONToFile(JSONArray jsonArray, String filename, BaseActivity activity) {

        String path = null;
            if (activity == null) {
                path = ResourceManager.generalActivity.getFilesDir().getPath() + "/" + filename;
            } else {
                path = activity.getFilesDir().getPath() + "/" + filename;
            }

        try {
            PrintStream out = null;
            if (path != null) {
                out = new PrintStream(new FileOutputStream(new File(path)));
            }
            if (out != null) {
                out.print(jsonArray.toString());
            }
            assert out != null;
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    */
}
