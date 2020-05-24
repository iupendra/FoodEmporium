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
import com.foodemporium.adapters.ResturantsAdapter;
import com.foodemporium.intrface.RecyclerTouchListener;
import com.foodemporium.intrface.RecyclerViewClickListener;
import com.foodemporium.models.AreaModel;
import com.foodemporium.models.HomeModel;
import com.foodemporium.models.RestaurantModel;
import com.foodemporium.utilities.ApiConstants;
import com.foodemporium.utilities.IResult;
import com.foodemporium.utilities.VollyService;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.foodemporium.utilities.Utilities.isNetworkConnectionAvailable;
import static com.foodemporium.utilities.Utilities.isValidString;

public class RestaurantsActivity extends AppCompatActivity implements IResult {

    VollyService vollyService;
    SVProgressHUD svProgressHUD;
    IResult result = this;
    List<RestaurantModel> restaurantModelList;

    String TAG = RestaurantsActivity.class.getSimpleName();

    @BindView(R.id.restaurantrecycler)
    RecyclerView restaurantRecycler;

    ResturantsAdapter resturantsAdapter;

    @BindView(R.id.lbRestaurantName)
    TextView lbRestaurantName;

    HomeModel homeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resturantsactivity);
        // Hide ActionBar

        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        homeModel = new HomeModel();

        homeModel = (HomeModel) getIntent().getSerializableExtra("HOMEMODEL");

        lbRestaurantName.setText(homeModel.cuisineName + " Restaurants");

        vollyService = new VollyService(result, this);
        svProgressHUD = new SVProgressHUD(this);
        initilizeReyclerView();


        if (isNetworkConnectionAvailable(this, -1)) {

            getMerchantlList(false);
        }
    }

    public void getMerchantlList(Boolean isRefreshed) {
        if (!isRefreshed) {
            svProgressHUD.showWithStatus("Loading restaurants");
        }

        String paramsString = ApiConstants.GETMERCHANTLIST;

        vollyService.getStringResponseVolley("GETMERCHANTLIST", paramsString);

    }

    public void initilizeReyclerView() {

        LinearLayoutManager verticalLayout
                = new LinearLayoutManager(
                RestaurantsActivity.this,
                LinearLayoutManager.VERTICAL,
                false);

        restaurantModelList = new ArrayList<>();
        restaurantRecycler.setLayoutManager(verticalLayout);
        resturantsAdapter = new ResturantsAdapter(restaurantModelList, RestaurantsActivity.this);
        restaurantRecycler.setAdapter(resturantsAdapter);

        restaurantRecycler.addOnItemTouchListener(new RecyclerTouchListener(RestaurantsActivity.this, restaurantRecycler, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                Log.d(TAG, "onClick: " + position);

                if (restaurantModelList.size() > 1) {

                    if (isNetworkConnectionAvailable(RestaurantsActivity.this, -1)) {

                        gotoRestaurantBookingType(restaurantModelList.get(position));

                    } else {
                        Toast.makeText(RestaurantsActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
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
        if (requestType.equalsIgnoreCase("GETMERCHANTLIST")) {
            if (isValidString(response)) {
                try {

                    restaurantModelList = new ArrayList<>();
                    restaurantModelList = serializeTheData(response);

                    if (restaurantModelList != null && restaurantModelList.size() > 0) {
                        resturantsAdapter.reloadAllContent(restaurantModelList);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<RestaurantModel> serializeTheData(String response) throws JSONException {

        JSONArray jsonArray = new JSONArray(response);

        ArrayList<RestaurantModel> restaurantModels = new ArrayList<>();

        for (int k = 0; k < jsonArray.length(); k++) {
            Gson gson = new Gson();
            JSONObject jsonObject1 = (JSONObject) jsonArray.get(k);
            RestaurantModel restaurantModel = gson.fromJson(jsonObject1.toString(), RestaurantModel.class);
            restaurantModels.add(restaurantModel);
        }

        return restaurantModels;
    }

    public void gotoRestaurantBookingType(RestaurantModel restaurantModel) {

        Intent iWeb = new Intent(RestaurantsActivity.this, RestaurantsBookingTypeActivity.class);
        iWeb.putExtra("RESTAURANTMODEL", restaurantModel);
//        iWeb.putExtra("STATEMODEL", stateModel);
        startActivity(iWeb);

    }

}
