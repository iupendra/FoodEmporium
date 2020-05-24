package com.foodemporium;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.foodemporium.models.RestaurantModel;
import com.foodemporium.utilities.PreferencesManager;
import com.foodemporium.utilities.StaticValues;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.foodemporium.utilities.Utilities.isNetworkConnectionAvailable;

public class EnterAddressActivity extends AppCompatActivity implements View.OnClickListener {


    String TAG = EnterAddressActivity.class.getSimpleName();

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

    @BindView(R.id.btnGo)
    Button btnGo;

    PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enteraddressactivity);
        // Hide ActionBar

        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        PreferencesManager.initializeInstance(this);


        preferencesManager = PreferencesManager.getInstance();

        String userID = preferencesManager.getStringValue(StaticValues.KEY_USERJSONOBJ);

        restaurantModel = new RestaurantModel();

        restaurantModel = (RestaurantModel) getIntent().getSerializableExtra("RESTAURANTMODEL");

        btnGo.setOnClickListener(this);

        if (isNetworkConnectionAvailable(this, -1)) {
            updateUI();

        }
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

    @Override
    public void onClick(View v) {

        gotoFoodCategoryActivity();
    }

    public void gotoFoodCategoryActivity() {

        Intent iLogin = new Intent(EnterAddressActivity.this, FoodCategoryActivity.class);
        iLogin.putExtra("RESTAURANTMODEL", restaurantModel);
        startActivity(iLogin);


    }


}
