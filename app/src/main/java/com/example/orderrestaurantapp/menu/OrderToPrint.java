package com.example.orderrestaurantapp.menu;

import java.util.List;

public class OrderToPrint {
    String tableName;
    List<Order> orderList;
    String total_price;
    String rabatt;
    String last_price;

    OrderToPrint(String tableName, List<Order> orderList,String total_price,String rabatt,String last_price){
        this.tableName = tableName;
        this.orderList = orderList;
        this.total_price = total_price;
        this.rabatt = rabatt;
        this.last_price = last_price;
    }
    public List<Order> getOrderListToPrint(){
        return this.orderList;
    }
    public void setOrderListToPrint(List<Order> orders){
        this.orderList = orders;
    }
    public String getTableName(){
        return this.tableName;
    }
    public void setTableName(String tableName){
        this.tableName = tableName;
    }
}
