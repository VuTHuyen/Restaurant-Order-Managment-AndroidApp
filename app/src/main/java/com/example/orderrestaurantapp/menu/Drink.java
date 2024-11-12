package com.example.orderrestaurantapp.menu;

public class Drink {
    private String name;
    private String currency;

    public Drink(
            String name,
            String currency){
        this.name = name;
        this.currency = currency;
    }
    public void setDrinkName(String name){
        this.name = name;
    }
    public void setDrinkCurrency(String currency){
        this.currency = currency;
    }
    public String getDrinkName(){
        return this.name;
    }
    public String getDrinkCurrency(){
        return this.currency;
    }

}
