package com.foodemporium.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Upendranath on 12/12/2017.
 */

public class FoodCategoryModel implements Serializable {

    public String merchantId = "";

    public String itemCategoryId = "";

    public String foodCategory = "";

    public String createdDate = "";

    public String createdDatestring = "";

    public String totalRecords = "";

    public List<FoodModel> foodModelList = new ArrayList<>();

}
