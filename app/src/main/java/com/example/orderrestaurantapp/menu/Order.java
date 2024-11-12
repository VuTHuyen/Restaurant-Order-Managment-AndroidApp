package com.example.orderrestaurantapp.menu;

import android.annotation.SuppressLint;

import java.util.List;

public class Order {
    private String ordername;
    private long number;
    private String priceItem;
    private String total_price;
    private String wishfood_typdrink;
    private boolean removeable;
    private String documentOrderID;
    private boolean printed;
    public Order(String ordername, long number, String priceItem, String wishfood_typdrink){
        this.ordername = ordername;
        this.number = number;
        this.priceItem = priceItem;
        this.wishfood_typdrink = wishfood_typdrink;
    }
    public Order(String ordername, long number, String priceItem, String total_price, String wishfood_typdrink, boolean removeable, boolean printed) {
        this.ordername = ordername;
        this.number = number;
        this.priceItem = priceItem;
        this.total_price = total_price;
        this.wishfood_typdrink = wishfood_typdrink;
        this.removeable = removeable;
        this.printed = printed;
    }
    public Order(String ordername, long number, String priceItem, String total_price, String wishfood_typdrink, boolean removeable, String documentOrderID, boolean printed){
        this.ordername = ordername;
        this.number = number;
        this.priceItem = priceItem;
        this.total_price = total_price;
        this.wishfood_typdrink = wishfood_typdrink;
        this.removeable = removeable;
        this.documentOrderID = documentOrderID;
        this.printed = printed;
    }

    public void setOrderName(String name){
        this.ordername = name;
    }
    public void setDocumentOrderID(String name){
        this.documentOrderID= name;
    }
    public void setNumber(long number){
        this.number = number;
    }
    public void setPrice(String price){
        this.priceItem = price;
    }
    public void setWishfood_typdrink(String wishfood_typdrink){
        this.wishfood_typdrink = wishfood_typdrink;
    }
    public void setPrinted(boolean printed){
        this.printed = printed;
    }

    public void setRemoveable(boolean removeable){this.removeable = removeable;}
    public void setTotalPrice(String price, long number){
        String price_string = price.replace(",", ".");
        double price_double = Double.parseDouble(price_string);
        double total = price_double*number;
        @SuppressLint("DefaultLocale") String result = String.format("%.2f", total);
        //System.out.println(result);
        this.total_price = (result+"").replace(".", ",");
    }
    public String getOrdername(){
        return this.ordername;
    }
    public long getNumber(){
        return this.number;
    }
    public String getPrice(){
        return this.priceItem;
    }
    public String getTotalPrice(){return this.total_price;}
    public boolean getPrinted(){return this.printed;}
    public String getwishfood_typdrink(){
        return this.wishfood_typdrink;
    }
    public boolean getRemovable(){return this.removeable;}
    public String getDocumentOrderID(){
        return this.documentOrderID;
    }
    public static void updateData(List<Order> orderList) {
        for (int i = 1; i < orderList.size(); i++) {
            Order currentOrder = orderList.get(i);
            Order previousOrder = orderList.get(i - 1);

            if (currentOrder.getOrdername().equals(previousOrder.getOrdername())) {
                String price_string = currentOrder.getPrice();
                price_string = price_string.replace(",", ".");
                double price = Double.parseDouble(price_string);
                long total_number = previousOrder.getNumber() + currentOrder.getNumber();
                previousOrder.setNumber(total_number);
                orderList.remove(i);
                i--;
            }
        }
    }

    @SuppressLint("DefaultLocale")
    public static String getTotalPrice(List<Order> orderList){
        String totalPrice = "";
        double totalpriceDouble = 0.0;
        for(Order order: orderList){
            String price_string = order.getTotalPrice();
            price_string = price_string.replace(",", ".");
            double price = Double.parseDouble(price_string);
            totalpriceDouble = totalpriceDouble+price;
            totalPrice = String.format("%.2f", totalpriceDouble);
        }
        totalPrice=totalPrice.replace(".",",");
        return totalPrice;
    }

    public static String getRabatt(String totalPrice, int rabat){
        String price_string = totalPrice.replace(",", ".");
        double price = Double.parseDouble(price_string);
        if (rabat == 5) {
            double rabatt = price * 0.05;
            return Order.stringFormat(rabatt);
        } else if (rabat == 10){
            double rabatt = price * 0.1;
            return Order.stringFormat(rabatt);
        } else if (rabat == 15) {
            double rabatt = price * 0.15;
            return Order.stringFormat(rabatt);
        }
        else return "No Rabatt";
    }
    public static String stringFormat(double price){
        @SuppressLint("DefaultLocale") String result = String.format("%.2f", price);
        String priceString = result.replace(".", ",");
        return priceString;
    }

    public static String getFinalPrice (String totalPrice, String rabatt) {
        String price_string = totalPrice.replace(",", ".");
        double price = Double.parseDouble(price_string);
        if (!rabatt.equals("No Rabatt")) {
            String rabatt_string = rabatt.replace(",", ".");
            double rabatt_double = Double.parseDouble(rabatt_string);

            double finalPrice = price - rabatt_double;
            return Order.stringFormat(finalPrice);
        }
        else return totalPrice;
    }
}
