package com.foodemporium;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.foodemporium.models.HomeModel;
import com.foodemporium.models.RestaurantModel;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.foodemporium.utilities.Utilities.isNetworkConnectionAvailable;

public class RestaurantsBookingTypeActivity extends AppCompatActivity implements View.OnClickListener {


    String TAG = RestaurantsBookingTypeActivity.class.getSimpleName();

    RestaurantModel restaurantModel;

    @BindView(R.id.txtRestaurantName)
    TextView txtRestaurantName;

    @BindView(R.id.txtName)
    TextView txtName;

    @BindView(R.id.txtAddress)
    TextView txtAddress;


    @BindView(R.id.txtTelNumber)
    TextView txtTelNumber;

    @BindView(R.id.rat_adapt_ratingbar)
    RatingBar ratingBar;

    @BindView(R.id.btnDelivery)
    Button btnDelivery;

    @BindView(R.id.btnPickup)
    Button btnPickup;

    @BindView(R.id.btnTakeDelivery)
    Button btnTakeDelivery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resturantsbookingtypeactivity);
        // Hide ActionBar

        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        restaurantModel = new RestaurantModel();

        restaurantModel = (RestaurantModel) getIntent().getSerializableExtra("RESTAURANTMODEL");

        if (isNetworkConnectionAvailable(this, -1)) {
            updateUI();
        }

        btnTakeDelivery.setOnClickListener(this);
        btnDelivery.setOnClickListener(this);
        btnPickup.setOnClickListener(this);
    }

    public void updateUI() {

        txtRestaurantName.setText(restaurantModel.businessName);

        txtName.setText(restaurantModel.businessName);

        txtAddress.setText(restaurantModel.location);

        txtTelNumber.setText(restaurantModel.contactNumber);

        float ratingValue = 0;

        try {
            ratingValue = Float.parseFloat(restaurantModel.rating);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            ratingValue = 0;
        }
        ratingBar.setRating(ratingValue);
    }


    public void gotoEnterAddressActivity() {

        Intent iLogin = new Intent(RestaurantsBookingTypeActivity.this, EnterAddressActivity.class);
        iLogin.putExtra("RESTAURANTMODEL", restaurantModel);
        startActivity(iLogin);

    }

    public void gotoFoodCalendarActivity() {

        Intent iLogin = new Intent(RestaurantsBookingTypeActivity.this, FoodCalendarActivity.class);
        iLogin.putExtra("RESTAURANTMODEL", restaurantModel);
        startActivity(iLogin);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnDelivery:
                gotoFoodCalendarActivity();
                break;
            case R.id.btnPickup:
                Log.d(TAG, "onClick: ");
                break;
            case R.id.btnTakeDelivery:
                gotoEnterAddressActivity();
                break;
        }
    }
}
