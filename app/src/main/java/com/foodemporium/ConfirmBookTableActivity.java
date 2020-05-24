package com.foodemporium;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.foodemporium.models.RestaurantModel;
import com.foodemporium.utilities.ApiConstants;
import com.foodemporium.utilities.IResult;
import com.foodemporium.utilities.PreferencesManager;
import com.foodemporium.utilities.StaticValues;
import com.foodemporium.utilities.VollyService;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.foodemporium.utilities.Utilities.isValidEmail;
import static com.foodemporium.utilities.Utilities.isValidString;

public class ConfirmBookTableActivity extends AppCompatActivity implements View.OnClickListener, IResult, AdapterView.OnItemSelectedListener {


    @BindView(R.id.editBookingname)
    TextInputEditText editBookingname;

    @BindView(R.id.editMobile)
    TextInputEditText editMobile;

    @BindView(R.id.editDateandtime)
    TextInputEditText editDateandtime;

    @BindView(R.id.editshortmesage)
    TextInputEditText editshortmesage;

    @BindView(R.id.btnConfirmBooking)
    Button btnConfirmBooking;

    @BindView(R.id.txtRestaurantName)
    TextView txtRestaurantName;

    @BindView(R.id.spinnerTimeSlot)
    Spinner spinnerTimeSlot;

    @BindView(R.id.spinnerNoOfGusts)
    Spinner spinnerNoOfGusts;

    String TAG = ConfirmBookTableActivity.class.getSimpleName();

    VollyService vollyService;
    SVProgressHUD svProgressHUD;
    IResult result = this;

    String dateSelected = "";

    String userName = "";

    String userId = "";

    RestaurantModel restaurantModel;

    PreferencesManager preferencesManager;

    ArrayList<String> timeCountArray = new ArrayList<>();

    ArrayList<String> gustLimitArray = new ArrayList<>();


    ArrayAdapter<String> gustAdapter = null;


    ArrayAdapter<String> timeAdater = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookatable);
        ButterKnife.bind(this);

        PreferencesManager.initializeInstance(this);
        preferencesManager = PreferencesManager.getInstance();

        // Hide ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        btnConfirmBooking.setOnClickListener(this);

        restaurantModel = (RestaurantModel) getIntent().getSerializableExtra("RESTAURANTMODEL");

        dateSelected = getIntent().getStringExtra("DATESLECTED");

        txtRestaurantName.setText(restaurantModel.businessName);
        vollyService = new VollyService(result, this);
        svProgressHUD = new SVProgressHUD(this);


        getMerchantGustLimit();
        getMerchantTimingsByDate();

        try {
            getJsonDataFromJsonObj();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        updateUI();

        initilizeSpinners();
    }


    public void getJsonDataFromJsonObj() throws JSONException {


        String userdetails = preferencesManager.getStringValue(StaticValues.KEY_USERJSONOBJ);

        JSONObject jsonObject = new JSONObject(userdetails);

        Log.d(TAG, "getJsonDateFrom: " + jsonObject);

        userId = jsonObject.optString("memberId");

        userName = jsonObject.optString("firstName");

    }

    public void updateUI() {

        editDateandtime.setText(dateSelected);

        editBookingname.setText(userName);
    }

    public void initilizeSpinners() {

        for (int i = 0; i < 10; i++) {
            timeCountArray.add("time " + i);
        }

        timeAdater =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, timeCountArray);
        timeAdater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerTimeSlot.setAdapter(timeAdater);

        gustAdapter =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, gustLimitArray);
        gustAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerNoOfGusts.setAdapter(gustAdapter);

    }

    public void methodConfirmBooking() {


        String strBookingname = editBookingname.getText().toString();

        String strnoofgust = "2";

        String strMobile = editMobile.getText().toString();

        String strDateandtime = editDateandtime.getText().toString();

        String strshortmesage = editshortmesage.getText().toString();

        String urlStr = ApiConstants.TABLEBOOKING;

        JSONObject parameters = new JSONObject();


        if (!isValidString(strBookingname)) {

            Toast.makeText(this, "Enter Booking Name", Toast.LENGTH_LONG).show();

        } else if (!isValidString(strnoofgust) && !strnoofgust.equalsIgnoreCase("0")) {

            Toast.makeText(this, "Enter number of gusts", Toast.LENGTH_LONG).show();

        } else if (!isValidString(strMobile)) {

            Toast.makeText(this, "Enter Mobile", Toast.LENGTH_LONG).show();

        } else if (!isValidEmail(strDateandtime)) {

            Toast.makeText(this, "Enter booking date", Toast.LENGTH_LONG).show();

        }
//        else if (isValidString(strshortmesage) & strPin.length() > 3) {
//
//            Toast.makeText(this, "Enter Password  is Invalid", Toast.LENGTH_LONG).show();
//
//        }
        else {

            try {

                parameters.put("MemberId", userId);
                parameters.put("MerchantId", restaurantModel.merchantId);
                parameters.put("BookingDate", strDateandtime);
                parameters.put("Parsons", strnoofgust);
                parameters.put("BookOnName", strBookingname);
                parameters.put("CallBackNumber", strMobile);
                parameters.put("RequestMessage", strshortmesage);

            } catch (JSONException e) {
                e.printStackTrace();
                svProgressHUD.dismiss();
            }
            String parameterString = parameters.toString();

//            svProgressHUD.showWithStatus("Confirming Booking");

//            vollyService.getStringResponseFromPostMethod(parameterString, "GETMEMBERLOGIN", urlStr);
        }
    }

    public void getMerchantTimingsByDate() {

        String paramsString = ApiConstants.GETMERCHANTTIMINGSBYDATE + restaurantModel.merchantId + "&date=" + dateSelected;

        vollyService.getStringResponseVolley("GETMERCHANTTIMINGSBYDATE", paramsString);

    }

    public void getMerchantGustLimit() {

        String paramsString = ApiConstants.GETMERCHANTGUESTLIMIT + restaurantModel.merchantId;

        vollyService.getStringResponseVolley("GETMERCHANTGUESTLIMIT", paramsString);

    }


    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        methodConfirmBooking();

    }

    @Override
    public void notifySuccess(String requestType, JSONObject response) {
        Log.d(TAG, "notifySuccess: " + response);

    }

    @Override
    public void notifyError(String requestType, VolleyError error) {
        Log.d(TAG, "notifyError: ");
    }

    @Override
    public void notifySuccess(String requestType, String response) {
        Log.d(TAG, "notifySuccess: " + response);


        if (requestType.equalsIgnoreCase("GETMERCHANTTIMINGSBYDATE")) {


        }


        if (requestType.equalsIgnoreCase("GETMERCHANTGUESTLIMIT")) {
            if (isValidString(response)) {
                try {
                    prepareGustLimitArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

    }


    public void prepareGustLimitArray(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);

        int limitInt = jsonObject.optInt("guestLimit");
        gustAdapter.clear();
        for (int i = 1; i <= limitInt; i++) {
            gustLimitArray.add("" + i);
        }
        gustAdapter.addAll(gustLimitArray);
        gustAdapter.notifyDataSetChanged();

    }

    public void prepareTimeSlotArray(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);

        int limitInt = jsonObject.optInt("guestLimit");
        gustAdapter.clear();
        for (int i = 1; i <= limitInt; i++) {
            timeCountArray.add("" + i);
        }
        gustAdapter.addAll(gustLimitArray);
        gustAdapter.notifyDataSetChanged();

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.spinnerNoOfGusts) {
            //do this
            Log.d(TAG, "onItemSelected: ");
        } else if (parent.getId() == R.id.spinnerTimeSlot) {
            //do this
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}