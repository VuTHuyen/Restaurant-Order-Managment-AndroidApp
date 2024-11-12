package com.example.orderrestaurantapp.menu;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.orderrestaurantapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderInfo {
    private String timestamp;
    private String total_price;
    private String type_payment;
    private int number;
    private String wish_type;

    public OrderInfo(String timestamp, String total_price, String type_payment) {
        this.timestamp = timestamp;
        this.total_price = total_price;
        this.type_payment = type_payment;
    }
    public OrderInfo( int number, String wish) {
        this.number = number;
        this.wish_type = wish;
    }
    public String getTimestamp() {
        return this.timestamp;
    }
    public String getTotal_price() {
        return this.total_price;
    }
    public String getType_payment() {
        return this.type_payment;
    }


    public int getNumber() {
        return number;
    }
    public String getWishType(){
        return wish_type;
    }

    public void setNumber(int newVal) {
        this.number = newVal;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public void setTotal_price(String totalPrice) {
        this.total_price = totalPrice;
    }
    public void setType_payment(String type_payment) {
        this.type_payment = type_payment;
    }

    public void setWishType(String toString) {
        this.wish_type = toString;
    }
    public static String getTotalPriceADay(List<OrderInfo> orderList){
        String totalPrice = "";
        double totalpriceDouble = 0.0;
        for(OrderInfo order: orderList){
            String price_string = order.getTotal_price();
            price_string = price_string.replace(",", ".");
            double price = Double.parseDouble(price_string);
            totalpriceDouble += price;
            totalPrice = String.format("%.2f", totalpriceDouble);
        }
        totalPrice=totalPrice.replace(".",",");
        return totalPrice;
    }
}
