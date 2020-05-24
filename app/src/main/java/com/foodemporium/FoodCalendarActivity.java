package com.foodemporium;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.foodemporium.adapters.FoodCategoryAdapter;
import com.foodemporium.intrface.RecyclerTouchListener;
import com.foodemporium.intrface.RecyclerViewClickListener;
import com.foodemporium.models.FoodCategoryModel;
import com.foodemporium.models.FoodModel;
import com.foodemporium.models.RestaurantModel;
import com.foodemporium.utilities.ApiConstants;
import com.foodemporium.utilities.IResult;
import com.foodemporium.utilities.VollyService;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.foodemporium.utilities.Utilities.getCurrentDateTimeInUTC;
import static com.foodemporium.utilities.Utilities.isNetworkConnectionAvailable;
import static com.foodemporium.utilities.Utilities.isValidString;

@RequiresApi(api = Build.VERSION_CODES.N)
public class FoodCalendarActivity extends AppCompatActivity implements IResult, View.OnClickListener, CalendarView.OnDateChangeListener {

    VollyService vollyService;
    SVProgressHUD svProgressHUD;
    IResult result = this;

    String TAG = SelectAreaActivity.class.getSimpleName();

    @BindView(R.id.txtRestaurantName)
    TextView txtRestaurantName;

    @BindView(R.id.yearandmonth)
    TextView txtYearandmonth;

    @BindView(R.id.lbToday)
    TextView lbToday;

    @BindView(R.id.lbDate)
    TextView lbDate;

    @BindView(R.id.txtContinue)
    TextView txtContinue;

    RestaurantModel restaurantModel;

    @BindView(R.id.calendarView)
    CalendarView calendarView;

    boolean isDateSelected = false;

    String dateSlected = "";

    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());

    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodcalendarbookactivity);
        // Hide ActionBar

        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        vollyService = new VollyService(result, this);
        svProgressHUD = new SVProgressHUD(this);

        restaurantModel = (RestaurantModel) getIntent().getSerializableExtra("RESTAURANTMODEL");
        initilizeReyclerView();
        initilizeCalander();
        txtRestaurantName.setText(restaurantModel.businessName);

        if (isNetworkConnectionAvailable(this, -1)) {
//            getStatesFromApi(false);
        }
        txtContinue.setOnClickListener(this);

        dateSlected = getCurrentDateTimeInUTC("yyyy/MM/dd");

        Log.d(TAG, "onCreate: " + dateSlected);

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
                FoodCalendarActivity.this,
                LinearLayoutManager.VERTICAL,
                false);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initilizeCalander() {

        Calendar calendar = Calendar.getInstance();
        //Returns current time in millis
        long minimumDate = calendar.getTimeInMillis();
        System.out.println("Time in milliseconds using Calendar: " + minimumDate);

        lbDate.setOnClickListener(this);
        lbToday.setOnClickListener(this);
        calendarView.setOnDateChangeListener(this);
        calendarView.setMinDate(minimumDate);
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

            }
        }

    }

    public void gotoConfirmBookTableActivity() {

        Intent iWeb = new Intent(FoodCalendarActivity.this, ConfirmBookTableActivity.class);
        iWeb.putExtra("RESTAURANTMODEL", restaurantModel);
        iWeb.putExtra("DATESLECTED", dateSlected);
        startActivity(iWeb);

        Log.d(TAG, "gotoConfirmBookTableActivity: " + dateSlected);
    }

    public void gotoTimeSlotActivity() {

        Intent iWeb = new Intent(FoodCalendarActivity.this, TimeSlotActivity.class);
        iWeb.putExtra("RESTAURANTMODEL", restaurantModel);
        iWeb.putExtra("DATESLECTED", dateSlected);
        startActivity(iWeb);

        Log.d(TAG, "gotoConfirmBookTableActivity: " + dateSlected);
    }


    @Override
    public void onClick(View v) {
        long time = System.currentTimeMillis();
//        String todayD = getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
        switch (v.getId()) {
            case R.id.lbDate:
                break;
            case R.id.lbToday:
                calendarView.setDate(time);
                break;
            case R.id.txtContinue:
                gotoTimeSlotActivity();
                break;
        }
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

        int monthAdd = month + 1;


        isDateSelected = true;
        dateSlected = "" + year + "/" + monthAdd + "/" + dayOfMonth;

//        txtYearandmonth.setText(" " + dayOfMonth + " " + "" + monthAdd + " " + year);

        Log.d(TAG, "onSelectedDayChange: " + dateSlected);
    }
}
