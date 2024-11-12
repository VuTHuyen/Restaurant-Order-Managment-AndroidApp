package com.example.orderrestaurantapp.menu;


import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuReader {

    public static List<String> getAllMenu(AssetManager assetManager, String filePath){
        List<String> menu = new ArrayList<>();
        try {
            InputStream inputStream = assetManager.open(filePath);

            // Create a BufferedReader to read the file
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                menu.add(line);
            }
            br.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return menu;
    }

    public static List<Food> getMenuFoodOfRestaurant(AssetManager assetManager, String filePath) {
        final String delimiter = ";";
        List<Food> foods = new ArrayList<>();
        try {
            InputStream inputStream = assetManager.open(filePath);

            // Create a BufferedReader to read the file
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            boolean isFirstLine = true; // Flag to skip the first line
            String line;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the first line
                }
                String[] tempArr = line.split(delimiter);
                if (tempArr.length == 3) { // Ensure there are three parts in each line
                    foods.add(new Food(tempArr[0], tempArr[1], tempArr[2]));
                } else {
                    System.err.println("Skipping invalid line: " + line);
                }

            }
            br.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return foods;
    }
    public static List<DrinkWithoutVarity> getMenuDrinksWVOfRestaurant(AssetManager assetManager, String filePath) {

        final String delimiter = ";";
        List<DrinkWithoutVarity> drinks = new ArrayList<>();
        try {
            InputStream inputStream = assetManager.open(filePath);

            // Create a BufferedReader to read the file
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            boolean isFirstLine = true; // Flag to skip the first line
            String line;
            while ((line = br.readLine()) != null) {
                System.err.println("Skipping invalid line: " + line);

                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the first line
                }
                String[] tempArr = line.split(delimiter);
                if (tempArr.length == 3) { // Ensure there are three parts in each line
                    drinks.add(new DrinkWithoutVarity(tempArr[0], tempArr[2],tempArr[1]));
                } else {
                    System.err.println("Skipping invalid line: " + line);
                }

            }
            br.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return drinks;
    }
    public static List<DrinkVarietyTwoFour> getDrinkVarietyTwoFour(AssetManager assetManager, String filePath) {
        final String delimiter = ";";
        List<DrinkVarietyTwoFour> drinks = new ArrayList<>();
        try {
            InputStream inputStream = assetManager.open(filePath);

            // Create a BufferedReader to read the file
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            boolean isFirstLine = true; // Flag to skip the first line
            String line;
            while ((line = br.readLine()) != null) {
                System.err.println("Skipping invalid line: " + line);

                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the first line
                }
                String[] tempArr = line.split(delimiter);
                if (tempArr.length == 4) { // Ensure there are three parts in each line
                    drinks.add(new DrinkVarietyTwoFour(tempArr[0], tempArr[1],tempArr[2],tempArr[3]));
                } else {
                    System.err.println("Skipping invalid line: " + line);
                }

            }
            br.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return drinks;
    }

    public static List<DrinkThreeFive> getDrinkThreeFive(AssetManager assetManager ,String filePath) {
        final String delimiter = ";";
        List<DrinkThreeFive> drinks = new ArrayList<>();
        try {
            InputStream inputStream = assetManager.open(filePath);

            // Create a BufferedReader to read the file
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            boolean isFirstLine = true; // Flag to skip the first line
            String line;
            while ((line = br.readLine()) != null) {
                System.err.println("Skipping invalid line: " + line);

                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the first line
                }
                String[] tempArr = line.split(delimiter);
                if (tempArr.length == 4) { // Ensure there are three parts in each line
                    drinks.add(new DrinkThreeFive(tempArr[0], tempArr[1],tempArr[2],tempArr[3]));
                } else {
                    System.err.println("Skipping invalid line: " + line);
                }

            }
            br.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return drinks;
    }


}
