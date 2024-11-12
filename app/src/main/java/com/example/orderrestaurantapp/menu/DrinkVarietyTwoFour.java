package com.example.orderrestaurantapp.menu;

public class DrinkVarietyTwoFour extends Drink{

    private String priceVarietyZeroTwo;
    private String priceVarietyZeroFour;

    public DrinkVarietyTwoFour(String name, String currency, String price1, String price2) {
        super(name, currency);
        this.priceVarietyZeroTwo = price1;
        this.priceVarietyZeroFour = price2;
    }
    public void setPriceVarietyZeroTwo(String price){
        this.priceVarietyZeroTwo = price;
    }
    public void setPriceVarietyZeroFour(String price){
        this.priceVarietyZeroTwo = price;
    }
    public String getPriceVarietyZeroTwo(){
        return this.priceVarietyZeroTwo;
    }
    public String getPriceVarietyZeroFour(){
        return this.priceVarietyZeroFour;
    }


}
