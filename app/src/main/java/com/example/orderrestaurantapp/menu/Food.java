package com.example.orderrestaurantapp.menu;

public class Food {
    private String name;
    private String price;
    private String currency;

    public Food(String name, String price, String currency){
        this.name = name  ;
        this.price = price;
        this.currency = currency;
    }


    public void setFoodName(String name){
        this.name = name;
    }
    public void setFoodPrice(String price){
        this.price = price;
    }
    public void setFoodCurrency(String currency){
        this.currency = currency;
    }
    public String getFoodName(){
        return this.name;
    }
    public String getFoodPrice(){
        return this.price;
    }
    public String getFoodCurrency(){
        return this.currency;
    }

}
