package com.foodemporium;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.foodemporium.adapters.CartAdapter;
import com.foodemporium.adapters.FoodAdapter;
import com.foodemporium.databaseutil.DatabaseHelper;
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

public class FoodCartActivity extends AppCompatActivity implements IResult, View.OnClickListener {

    VollyService vollyService;
    SVProgressHUD svProgressHUD;
    IResult result = this;

    int minteger = 0;


    String TAG = SelectAreaActivity.class.getSimpleName();

    @BindView(R.id.txtRestaurantName)
    TextView txtRestaurantName;

    @BindView(R.id.lbYourOrder)
    TextView lbYourOrder;


    @BindView(R.id.txtSummary)
    TextView txtSummary;

    @BindView(R.id.txtAddSomeMore)
    TextView txtAddSomeMore;

    @BindView(R.id.txttotalprice)
    TextView txtTotalPrice;

    @BindView(R.id.txtsubtotal)
    TextView txtSubTotal;

    @BindView(R.id.cartrecycler)
    RecyclerView cartrecycler;

    FoodCategoryModel foodCategoryModel;

    RestaurantModel restaurantModel;

    @BindView(R.id.btnCheckut)
    Button btnCheckut;

    List<FoodModel> foodModelList;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodcart);
        // Hide ActionBar

        ButterKnife.bind(this);

        db = new DatabaseHelper(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        vollyService = new VollyService(result, this);
        svProgressHUD = new SVProgressHUD(this);

        restaurantModel = (RestaurantModel) getIntent().getSerializableExtra("RESTAURANTMODEL");

        foodCategoryModel = (FoodCategoryModel) getIntent().getSerializableExtra("FOODCATEGORYMODEL");


        txtRestaurantName.setText(restaurantModel.businessName);


        if (isNetworkConnectionAvailable(this, -1)) {

        }

        foodModelList = new ArrayList<>();

        foodModelList = db.getAllCartsItemFromTable();

        if (foodModelList != null && foodModelList.size() > 0) {

            initilizeReyclerView();
        }

        btnCheckut.setOnClickListener(this);
        txtAddSomeMore.setOnClickListener(this);
        updateTotalSubTotal();
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
                FoodCartActivity.this,
                LinearLayoutManager.VERTICAL,
                false);


        cartrecycler.setLayoutManager(verticalLayout);
        CartAdapter cartAdapter = new CartAdapter(foodModelList, FoodCartActivity.this);
        cartrecycler.setAdapter(cartAdapter);

        cartrecycler.addOnItemTouchListener(new RecyclerTouchListener(FoodCartActivity.this, cartrecycler, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, final int position) {

                Log.d(TAG, "onClick: " + position);


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

    public void gotoSelectAreaActivity(FoodModel foodModel) {

//        Intent iWeb = new Intent(FoodActivity.this, FoodActivity.class);
//        iWeb.putExtra("FOODCATEGORYMODEL", foodCategoryModel);
//        startActivity(iWeb);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.txtAddSomeMore:
                finish();
                break;
            case R.id.btnCheckut:
//                svProgressHUD.showWithStatus("Loading");
                updateTotalSubTotal();
                break;
        }


    }


    public void updateTotalSubTotal() {

        txtTotalPrice.setText("Rs " + getTotalSumofSeleted());
        txtSubTotal.setText("Rs " + getTotalSumofSeleted());

    }

    public int getTotalSumofSeleted() {

        int totalPrice = 0;

        if (foodModelList != null && foodModelList.size() > 0) {

            for (int i = 0; i < foodModelList.size(); i++) {


                FoodModel foodModel = foodModelList.get(i);

                int qtySelected = 1;

                int singleTotalPrice = 0;

                try {
                    qtySelected = Integer.parseInt(foodModel.countSelected);

                    Log.d(TAG, "onBindViewHolder: " + qtySelected);

                } catch (NumberFormatException num) {
                    num.printStackTrace();
                }

                try {

                    singleTotalPrice = Integer.parseInt(foodModel.specialPrice) * qtySelected;

                    Log.d(TAG, "onBindViewHolder: " + singleTotalPrice);


                } catch (NumberFormatException numberEx) {

                    numberEx.printStackTrace();
                }
                totalPrice = totalPrice + singleTotalPrice;
            }

        }

        return totalPrice;
    }

}
