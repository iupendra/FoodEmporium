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
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.foodemporium.adapters.FoodAdapter;
import com.foodemporium.intrface.RecyclerTouchListener;
import com.foodemporium.intrface.RecyclerViewClickListener;
import com.foodemporium.models.FoodCategoryModel;
import com.foodemporium.models.FoodModel;
import com.foodemporium.models.RestaurantModel;
import com.foodemporium.utilities.ApiConstants;
import com.foodemporium.utilities.IResult;
import com.foodemporium.utilities.VollyService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.foodemporium.utilities.Utilities.isNetworkConnectionAvailable;

public class FoodActivity extends AppCompatActivity implements IResult, View.OnClickListener {

    VollyService vollyService;
    SVProgressHUD svProgressHUD;
    IResult result = this;
    List<FoodModel> foodModelList;

    String TAG = SelectAreaActivity.class.getSimpleName();

    @BindView(R.id.restaurantrecycler)
    RecyclerView restaurantrecycler;

    @BindView(R.id.txtRestaurantName)
    TextView txtRestaurantName;

    @BindView(R.id.lbCurryName)
    TextView lbCurryName;

    FoodCategoryModel foodCategoryModel;

    FoodAdapter foodAdapter;

    RestaurantModel restaurantModel;


    @BindView(R.id.btnProceed)
    TextView btnProceed;

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

        foodModelList = new ArrayList<>();

        foodModelList = (List<FoodModel>) getIntent().getSerializableExtra("FOODMODELLIST");

        foodCategoryModel = (FoodCategoryModel) getIntent().getSerializableExtra("FOODCATEGORYMODEL");

        initilizeReyclerView();

        txtRestaurantName.setText(restaurantModel.businessName);

        lbCurryName.setText(foodCategoryModel.foodCategory);

        if (isNetworkConnectionAvailable(this, -1)) {

        }

        btnProceed.setVisibility(View.VISIBLE);

        btnProceed.setOnClickListener(this);
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
                FoodActivity.this,
                LinearLayoutManager.VERTICAL,
                false);


        restaurantrecycler.setLayoutManager(verticalLayout);
        foodAdapter = new FoodAdapter(foodModelList, FoodActivity.this);
        restaurantrecycler.setAdapter(foodAdapter);

        restaurantrecycler.addOnItemTouchListener(new RecyclerTouchListener(FoodActivity.this, restaurantrecycler, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, final int position) {

                Log.d(TAG, "onClick: " + position);


                if (view instanceof ElegantNumberButton) {


                } else {

//                    if (foodModelList.size() > 1) {
//
//                        if (isNetworkConnectionAvailable(FoodActivity.this, -1)) {
//
//                            gotoSelectAreaActivity(foodModelList.get(position));
//
//                        } else {
//                            Toast.makeText(FoodActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }

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

    }

    public void gotoSelectAreaActivity() {

        Intent iWeb = new Intent(FoodActivity.this, FoodCartActivity.class);
        iWeb.putExtra("FOODCATEGORYMODEL", foodCategoryModel);
        iWeb.putExtra("RESTAURANTMODEL", restaurantModel);
        startActivity(iWeb);

    }

    @Override
    public void onClick(View v) {
        gotoSelectAreaActivity();
    }
}
