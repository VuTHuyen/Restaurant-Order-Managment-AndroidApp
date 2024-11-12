package com.example.orderrestaurantapp.menu;

import java.util.List;

public class Table {
    String nameOfTable;
    boolean statusOfTable;
    boolean payment;
    List<Order> orders;

    public Table(String nameOfTable, boolean statusOfTable, boolean payment){
        this.nameOfTable = nameOfTable;
        this.statusOfTable = statusOfTable;
        this.payment = payment;

    }

    public Table(String nameOfTable, boolean statusOfTable, boolean payment, List<Order> orders){
        this.nameOfTable = nameOfTable;
        this.statusOfTable = statusOfTable;
        this.payment = payment;
        this.orders = orders;
    }
    public Table(String nameOfTable, List<Order> orders){
        this.nameOfTable = nameOfTable;
        this.orders = orders;
    }

    public String getNameOfTable(){
        return this.nameOfTable;
    }
    public boolean getStatusOfTable(){
        return this.statusOfTable;
    }
    public List<Order> getOrders(){
        return this.orders;
    }
    public boolean getPaymentOfTable(){
        return this.statusOfTable;
    }
    public void setStatusOfTable(boolean status){
        this.statusOfTable = status;
    }
}
