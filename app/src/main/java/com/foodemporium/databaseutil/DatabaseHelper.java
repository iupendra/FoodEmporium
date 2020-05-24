package com.foodemporium.databaseutil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.foodemporium.models.FoodModel;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "foodcart.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(FoodModel.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + FoodModel.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }


    public List<FoodModel> getAllCartsItemFromTable() {

        List<FoodModel> foodModelList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + FoodModel.TABLE_NAME + " ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FoodModel foodModel = new FoodModel();
                foodModel.foodItemId = cursor.getString(cursor.getColumnIndex("foodItemId"));
                foodModel.merchantId = cursor.getString(cursor.getColumnIndex("merchantId"));
                foodModel.itemName = cursor.getString(cursor.getColumnIndex("itemName"));
                foodModel.normalPrice = cursor.getString(cursor.getColumnIndex("normalPrice"));
                foodModel.specialPrice = cursor.getString(cursor.getColumnIndex("specialPrice"));
                foodModel.shortDescription = cursor.getString(cursor.getColumnIndex("shortDescription"));
                foodModel.productImagepath = cursor.getString(cursor.getColumnIndex("productImagepath"));
                foodModel.itemCategoryId = cursor.getString(cursor.getColumnIndex("itemCategoryId"));
                foodModel.itemInfo = cursor.getString(cursor.getColumnIndex("itemInfo"));
                foodModel.isSpecial = cursor.getString(cursor.getColumnIndex("isSpecial"));
                foodModel.status = cursor.getString(cursor.getColumnIndex("status"));
                foodModel.totalRecords = cursor.getString(cursor.getColumnIndex("totalRecords"));
                foodModel.createdDate = cursor.getString(cursor.getColumnIndex("createdDate"));
                foodModel.createdatestring = cursor.getString(cursor.getColumnIndex("createdatestring"));
                foodModel.modifiedDate = cursor.getString(cursor.getColumnIndex("modifiedDate"));
                foodModel.modifieddatestring = cursor.getString(cursor.getColumnIndex("modifieddatestring"));
                foodModel.countSelected = cursor.getString(cursor.getColumnIndex("countSelected"));


                foodModelList.add(foodModel);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return foodModelList;
    }

    public void injectFoodModelntoCart(FoodModel foodModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = null;
        try {

            contentValues = new ContentValues();

            contentValues.put("foodItemId", foodModel.foodItemId);
            contentValues.put("merchantId", foodModel.merchantId);
            contentValues.put("itemName", foodModel.itemName);
            contentValues.put("normalPrice", foodModel.normalPrice);
            contentValues.put("specialPrice", foodModel.specialPrice);
            contentValues.put("shortDescription", foodModel.shortDescription);
            contentValues.put("productImagepath", foodModel.productImagepath);
            contentValues.put("itemCategoryId", foodModel.itemCategoryId);
            contentValues.put("itemInfo", foodModel.itemInfo);
            contentValues.put("isSpecial", foodModel.isSpecial);
            contentValues.put("status", foodModel.status);
            contentValues.put("totalRecords", foodModel.totalRecords);
            contentValues.put("createdDate", foodModel.createdDate);
            contentValues.put("createdatestring", foodModel.createdatestring);
            contentValues.put("modifiedDate", foodModel.modifiedDate);
            contentValues.put("modifieddatestring", foodModel.modifieddatestring);
            contentValues.put("countSelected", foodModel.countSelected);

            db.insert(FoodModel.TABLE_NAME, null, contentValues);

        } catch (SQLiteException exception) {

            exception.printStackTrace();
        }

    }

    public boolean CheckIsDataAlreadyInDBorNot(String TableName, String dbfield, String fieldValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + TableName + " where " + dbfield + " = " + fieldValue;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

//    public int updateFoodInCart(FoodModel foodModel) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(Note.COLUMN_NOTE, note.getNote());
//
//        // updating row
//        return db.update(FoodModel.TABLE_NAME, values, FoodModel.COLUMN_ID + " = ?",
//                new String[]{String.valueOf(FoodModel.getId())});
//    }
//

    public void deleteRecordsinTable(String foodItemId, String TABLENAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String strDelete = "DELETE FROM " + TABLENAME + " WHERE  foodItemId ='"
                    + foodItemId + "'";
            db.execSQL(strDelete);

        } catch (SQLiteException sqlEx) {

            sqlEx.printStackTrace();
        }

    }

}
