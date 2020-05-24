package com.foodemporium;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodemporium.adapters.HomeAdapter;
import com.foodemporium.databaseutil.DatabaseHelper;
import com.foodemporium.intrface.RecyclerTouchListener;
import com.foodemporium.intrface.RecyclerViewClickListener;
import com.foodemporium.models.AreaModel;
import com.foodemporium.models.HomeModel;
import com.foodemporium.models.StateModel;
import com.foodemporium.utilities.PreferencesManager;
import com.foodemporium.utilities.StaticValues;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.foodemporium.utilities.Utilities.getJsonFromAssets;
import static com.foodemporium.utilities.Utilities.isNetworkConnectionAvailable;

public class HomeActivity extends AppCompatActivity {


    String TAG = HomeActivity.class.getSimpleName();

    HomeAdapter homeAdapter;

    @BindView(R.id.homeRecycleview)
    RecyclerView homeRecycleview;

    List<HomeModel> homeModelList;

    PreferencesManager preferencesManager;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeactivity);
        ButterKnife.bind(this);

        PreferencesManager.initializeInstance(this);
        preferencesManager = PreferencesManager.getInstance();

        db = new DatabaseHelper(this);

        // Hide ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        String jsonFileString = getJsonFromAssets(getApplicationContext(), "homedata.json");

        Log.d(TAG, "onCreate: " + jsonFileString);

        try {
            homeModelList = new ArrayList<>();
            homeModelList = serializeTheData(jsonFileString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        initilizeReyclerView();

        String userID = preferencesManager.getStringValue(StaticValues.KEY_USERID);
    }

    public void initilizeReyclerView() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

        homeRecycleview.setLayoutManager(gridLayoutManager);

        homeAdapter = new HomeAdapter(homeModelList, HomeActivity.this);
        homeRecycleview.setAdapter(homeAdapter);

        homeRecycleview.addOnItemTouchListener(new RecyclerTouchListener(HomeActivity.this, homeRecycleview, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                Log.d(TAG, "onClick: " + position);

                if (homeModelList.size() > 1) {

                    if (isNetworkConnectionAvailable(HomeActivity.this, -1)) {

                        gotoRestaurantActivity(homeModelList.get(position));

                    } else {
                        Toast.makeText(HomeActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }));

    }


    public void gotoRestaurantActivity(HomeModel homeModel) {


        Intent intent = new Intent(HomeActivity.this, RestaurantsActivity.class);
        intent.putExtra("HOMEMODEL", homeModel);
        startActivity(intent);
    }

    public List<HomeModel> serializeTheData(String response) throws JSONException {

        JSONArray jsonArray = new JSONArray(response);

        ArrayList<HomeModel> homeModels = new ArrayList<>();

        for (int k = 0; k < jsonArray.length(); k++) {
            Gson gson = new Gson();
            JSONObject jsonObject1 = (JSONObject) jsonArray.get(k);
            HomeModel homeModel = gson.fromJson(jsonObject1.toString(), HomeModel.class);

            homeModels.add(homeModel);
        }

        return homeModels;
    }


}