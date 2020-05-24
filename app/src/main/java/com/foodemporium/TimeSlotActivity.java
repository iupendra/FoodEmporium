package com.foodemporium;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.foodemporium.adapters.TimeSlotAdapter;
import com.foodemporium.intrface.RecyclerTouchListener;
import com.foodemporium.intrface.RecyclerViewClickListener;
import com.foodemporium.models.AreaModel;
import com.foodemporium.models.RestaurantModel;
import com.foodemporium.models.TimeModel;
import com.foodemporium.utilities.ApiConstants;
import com.foodemporium.utilities.IResult;
import com.foodemporium.utilities.PreferencesManager;
import com.foodemporium.utilities.VollyService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.foodemporium.utilities.Utilities.isNetworkConnectionAvailable;
import static com.foodemporium.utilities.Utilities.isValidString;

public class TimeSlotActivity extends AppCompatActivity implements IResult {

    VollyService vollyService;
    SVProgressHUD svProgressHUD;
    IResult result = this;
    List<TimeModel> timeModelList;

    String TAG = TimeSlotActivity.class.getSimpleName();

    @BindView(R.id.timeRecycleview)
    RecyclerView timeRecycleview;

    TimeSlotAdapter timeSlotAdapter;

    @BindView(R.id.txtRestaurantName)
    TextView txtRestaurantName;

    String dateSelected = "";

    String userName = "";

    String userId = "";

    RestaurantModel restaurantModel;

    PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeslotactivity);
        // Hide ActionBar

        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        PreferencesManager.initializeInstance(this);
        preferencesManager = PreferencesManager.getInstance();

        vollyService = new VollyService(result, this);
        svProgressHUD = new SVProgressHUD(this);

        initializeRecyclerView();

        restaurantModel = (RestaurantModel) getIntent().getSerializableExtra("RESTAURANTMODEL");

        dateSelected = getIntent().getStringExtra("DATESLECTED");

        txtRestaurantName.setText(restaurantModel.businessName);

        if (isNetworkConnectionAvailable(this, -1)) {

            getMerchantTimingsByDate();

        }
    }

    public void initializeRecyclerView() {

        timeRecycleview.setLayoutManager(new GridLayoutManager(this, 4));
        timeModelList = new ArrayList<>();
        timeSlotAdapter = new TimeSlotAdapter(timeModelList, TimeSlotActivity.this);
        timeRecycleview.setAdapter(timeSlotAdapter);

        timeRecycleview.addOnItemTouchListener(new RecyclerTouchListener(TimeSlotActivity.this, timeRecycleview, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                Log.d(TAG, "onClick: " + position);

                if (timeModelList.size() > 1) {

                    if (isNetworkConnectionAvailable(TimeSlotActivity.this, -1)) {

                        gotoConfirmBookTableActivity(timeModelList.get(position).displayTime);

                    } else {
                        Toast.makeText(TimeSlotActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
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
        Log.d(TAG, "notifySuccess: " + response);
        svProgressHUD.dismiss();
        if (requestType.equalsIgnoreCase("GETMERCHANTTIMINGSBYDATE")) {
            if (isValidString(response)) {

                timeModelList = new ArrayList<>();
                try {
                    timeModelList = serializeTheData(response);

                    if (timeModelList != null) {
                        timeSlotAdapter.reloadAllContent(timeModelList);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void getMerchantTimingsByDate() {

        String paramsString = ApiConstants.GETMERCHANTTIMINGSBYDATE + restaurantModel.merchantId + "&date=" + dateSelected;

        vollyService.getStringResponseVolley("GETMERCHANTTIMINGSBYDATE", paramsString);

    }

    public List<TimeModel> serializeTheData(String response) throws JSONException {

        JSONArray jsonArray = new JSONArray(response);

        JSONObject jsonObject = jsonArray.getJSONObject(0);

        JSONArray jsonArray1 = jsonObject.optJSONArray("timeslot");

        List<TimeModel> timeModels = new ArrayList<>();

        if (jsonArray1 != null && jsonArray1.length() > 0) {

            for (int i = 0; i < jsonArray1.length(); i++) {

                TimeModel timeModel = new TimeModel();
                timeModel.displayTime = jsonArray1.optString(i);
                timeModels.add(timeModel);

            }
        }

        return timeModels;
    }


    public List<TimeModel> getArrayListFromString(String timeslotsString) {

        List<TimeModel> timeModelList = new ArrayList<>();

        List<String> slotString = new ArrayList<>();


        if (timeslotsString.length() <= 0)
            return timeModelList;

        slotString = Arrays.asList(timeslotsString.split(";"));

        if (slotString != null && slotString.size() > 0) {

            for (int i = 0; i < slotString.size(); i++) {
                TimeModel timeModel = new TimeModel();
                timeModel.displayTime = slotString.get(i);
                timeModelList.add(timeModel);
            }


        }

        return timeModelList;

    }


    public void gotoConfirmBookTableActivity(String timeSelected) {

        Intent iWeb = new Intent(TimeSlotActivity.this, ConfirmBookTableActivity.class);
        iWeb.putExtra("RESTAURANTMODEL", restaurantModel);
        iWeb.putExtra("DATESLECTED", dateSelected);
        startActivity(iWeb);

    }

}


