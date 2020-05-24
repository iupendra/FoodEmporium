package com.foodemporium;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.foodemporium.adapters.FoodCategoryAdapter;
import com.foodemporium.adapters.SelectStateAdapter;
import com.foodemporium.intrface.RecyclerTouchListener;
import com.foodemporium.intrface.RecyclerViewClickListener;
import com.foodemporium.models.FoodCategoryModel;
import com.foodemporium.models.FoodModel;
import com.foodemporium.models.RestaurantModel;
import com.foodemporium.models.StateModel;
import com.foodemporium.utilities.ApiConstants;
import com.foodemporium.utilities.IResult;
import com.foodemporium.utilities.VollyService;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.foodemporium.utilities.Utilities.isNetworkConnectionAvailable;
import static com.foodemporium.utilities.Utilities.isValidString;

public class FoodCategoryActivity extends AppCompatActivity implements IResult {

    VollyService vollyService;
    SVProgressHUD svProgressHUD;
    IResult result = this;
    List<FoodCategoryModel> foodCategoryModelList;

    String TAG = SelectAreaActivity.class.getSimpleName();

    @BindView(R.id.restaurantrecycler)
    RecyclerView restaurantrecycler;

    @BindView(R.id.txtRestaurantName)
    TextView txtRestaurantName;

    @BindView(R.id.lbCurryName)
    TextView lbCurryName;

    FoodCategoryAdapter foodCategoryAdapter;

    RestaurantModel restaurantModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodcategoryactivity);
        // Hide ActionBar

        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        vollyService = new VollyService(result, this);
        svProgressHUD = new SVProgressHUD(this);

        restaurantModel = (RestaurantModel) getIntent().getSerializableExtra("RESTAURANTMODEL");


        initilizeReyclerView();

        txtRestaurantName.setText(restaurantModel.businessName);

        if (isNetworkConnectionAvailable(this, -1)) {
            getStatesFromApi(false);
        }
    }

    public void getStatesFromApi(Boolean isRefreshed) {
        if (!isRefreshed) {
            svProgressHUD.showWithStatus("Loading");
        }

        String paramsString = ApiConstants.GETPRODUCTSLIST + "1";

        vollyService.getStringResponseVolley("GETPRODUCTSLIST", paramsString);

    }

    public void initilizeReyclerView() {


        LinearLayoutManager verticalLayout
                = new LinearLayoutManager(
                FoodCategoryActivity.this,
                LinearLayoutManager.VERTICAL,
                false);

        foodCategoryModelList = new ArrayList<>();
        restaurantrecycler.setLayoutManager(verticalLayout);
        foodCategoryAdapter = new FoodCategoryAdapter(foodCategoryModelList, FoodCategoryActivity.this);
        restaurantrecycler.setAdapter(foodCategoryAdapter);

        restaurantrecycler.addOnItemTouchListener(new RecyclerTouchListener(FoodCategoryActivity.this, restaurantrecycler, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                Log.d(TAG, "onClick: " + position);

                if (foodCategoryModelList.size() > 1) {

                    if (isNetworkConnectionAvailable(FoodCategoryActivity.this, -1)) {

                        gotoSelectAreaActivity(foodCategoryModelList.get(position));

                    } else {
                        Toast.makeText(FoodCategoryActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }));


    }

    @Override
    public void notifySuccess(String requestType, JSONObject response) {
        Log.d(TAG, "notifySuccess: ");
        svProgressHUD.dismiss();
    }

    @Override
    public void notifyError(String requestType, VolleyError error) {
        Log.d(TAG, "notifyError: ");
        svProgressHUD.dismiss();
    }

    @Override
    public void notifySuccess(String requestType, String response) {
        Log.d(TAG, "notifySuccess: ");
        svProgressHUD.dismiss();
        if (requestType.equalsIgnoreCase("GETPRODUCTSLIST")) {
            if (isValidString(response)) {
                try {
                    foodCategoryModelList = new ArrayList<>();
                    foodCategoryModelList = serializeTheData(response);

                    if (foodCategoryModelList != null && foodCategoryModelList.size() > 0) {
                        foodCategoryAdapter.reloadAllContent(foodCategoryModelList);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void gotoSelectAreaActivity(FoodCategoryModel foodCategoryModel) {

        Intent iWeb = new Intent(FoodCategoryActivity.this, FoodActivity.class);
        iWeb.putExtra("FOODCATEGORYMODEL", foodCategoryModel);
        iWeb.putExtra("RESTAURANTMODEL", restaurantModel);
        iWeb.putExtra("FOODMODELLIST", (Serializable) foodCategoryModel.foodModelList);
        startActivity(iWeb);

    }

    public List<FoodCategoryModel> serializeTheData(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);

        JSONArray jsonArray = jsonObject.getJSONArray("category");

        ArrayList<FoodCategoryModel> foodCategoryModels = new ArrayList<>();

        for (int k = 0; k < jsonArray.length(); k++) {
            Gson gson = new Gson();
            JSONObject jsonObject1 = (JSONObject) jsonArray.get(k);
            FoodCategoryModel foodCategoryModel = gson.fromJson(jsonObject1.toString(), FoodCategoryModel.class);
            JSONArray jsonArray1 = jsonObject1.optJSONArray("products");
            for (int j = 0; j < jsonArray1.length(); j++) {
                Gson gson1 = new Gson();
                JSONObject jsonObject2 = (JSONObject) jsonArray1.get(j);
                FoodModel foodModel = gson1.fromJson(jsonObject2.toString(), FoodModel.class);
                foodCategoryModel.foodModelList.add(foodModel);
            }

            foodCategoryModels.add(foodCategoryModel);
        }

        return foodCategoryModels;
    }

}
