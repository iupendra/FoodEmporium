package com.foodemporium.models;

import java.io.Serializable;

/**
 * Created by Upendranath on 12/12/2017.
 */

public class FoodModel implements Serializable {


    public String foodItemId = "";

    public String merchantId = "";

    public String itemName = "";

    public String normalPrice = "";

    public String specialPrice = "";

    public String shortDescription = "";

    public String itemImage = "";

    public String productImagepath = "";

    public String itemCategoryId = "";

    public String itemInfo = "";

    public String isSpecial = "";

    public String status = "";

    public String totalRecords = "";

    public String createdDate = "";

    public String createdatestring = "";

    public String modifiedDate = "";

    public String modifieddatestring = "";

    public String countSelected = "0";

//    https://futurestud.io/tutorials/gson-mapping-of-arrays-and-lists-of-objects


    // SQLITE

    public static final String TABLE_NAME = "FOODCART";


    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, foodItemId TEXT, merchantId TEXT, itemName TEXT, normalPrice TEXT,specialPrice TEXT, shortDescription TEXT, itemImage TEXT,productImagepath TEXT ,itemCategoryId TEXT ,itemInfo TEXT,isSpecial TEXT,status TEXT,totalRecords TEXT,createdDate TEXT,createdatestring TEXT,modifiedDate TEXT,modifieddatestring TEXT,countSelected TEXT,userID TEXT)";


    public FoodModel(String foodItemId, String merchantId, String itemName, String normalPrice, String specialPrice, String shortDescription, String itemImage, String productImagepath, String itemCategoryId, String itemInfo, String isSpecial, String status, String totalRecords, String createdDate, String createdatestring, String modifiedDate, String modifieddatestring, String countSelected) {
        this.foodItemId = foodItemId;
        this.merchantId = merchantId;
        this.itemName = itemName;
        this.normalPrice = normalPrice;
        this.specialPrice = specialPrice;
        this.shortDescription = shortDescription;
        this.itemImage = itemImage;
        this.productImagepath = productImagepath;
        this.itemCategoryId = itemCategoryId;
        this.itemInfo = itemInfo;
        this.isSpecial = isSpecial;
        this.status = status;
        this.totalRecords = totalRecords;
        this.createdDate = createdDate;
        this.createdatestring = createdatestring;
        this.modifiedDate = modifiedDate;
        this.modifieddatestring = modifieddatestring;
        this.countSelected = countSelected;
    }


    public FoodModel() {
    }
}
