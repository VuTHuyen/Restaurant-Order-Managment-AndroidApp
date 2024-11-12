package com.example.orderrestaurantapp.menu;

public class DrinkThreeFive extends Drink{
    private String priceVarietyZeroThree;

    private String priceVarietyZeroFive;

    public DrinkThreeFive(String name, String currency, String price1, String price2) {
        super(name, currency);
        this.priceVarietyZeroThree = price1;
        this.priceVarietyZeroFive = price2;
    }

    public void setPriceVarietyZeroThree(String price){
        this.priceVarietyZeroThree = price;
    }
    public void setPriceVarietyZeroFive(String price){
        this.priceVarietyZeroFive = price;
    }

    public String getPriceVarietyZeroThree(){
        return this.priceVarietyZeroThree;
    }
    public String getPriceVarietyZeroFive(){
        return this.priceVarietyZeroFive;
    }
}
