package com.sujan.info.thespicelounge.models;

import java.util.ArrayList;

/**
 * Created by pradeep on 19/7/20.
 */

public class TableInformation {
    int tableNum,numbersOfSeat,tableStatus;
    ArrayList<OrderDetail> orderedFoods;

    public TableInformation(ArrayList<OrderDetail> orderedFoods, int tableNum, int numbersOfSeat) {
        this.orderedFoods = orderedFoods;
        this.tableNum = tableNum;
        this.numbersOfSeat=numbersOfSeat;
    }

    public TableInformation() {
    }

    public ArrayList<OrderDetail> getOrderedFoods() {
        return orderedFoods;
    }

    public void setOrderedFoods(ArrayList<OrderDetail> orderedFoods) {
        this.orderedFoods = orderedFoods;
    }

    public int getNumbersOfSeat() {
        return numbersOfSeat;
    }

    public void setNumbersOfSeat(int numbersOfSeat) {
        this.numbersOfSeat = numbersOfSeat;
    }

    public int getTableNum() {
        return tableNum;
    }

    public void setTableNum(int tableNum) {
        this.tableNum = tableNum;
    }

    public int getTableStatus() {
        return tableStatus;
    }

    public void setTableStatus(int tableStatus) {
        this.tableStatus = tableStatus;
    }
}
