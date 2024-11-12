package com.example.orderrestaurantapp.menu;

public class DrinkWithoutVarity extends Drink{
    private String price;
    public DrinkWithoutVarity(String name, String currency, String price) {
        super(name, currency);
        this.price = price;
    }
    public String getPrice(){
        return this.price;
    }
}
