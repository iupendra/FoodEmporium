package com.foodemporium;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.foodemporium.adapters.AreaAdapter;
import com.foodemporium.adapters.SelectStateAdapter;
import com.foodemporium.intrface.RecyclerTouchListener;
import com.foodemporium.intrface.RecyclerViewClickListener;
import com.foodemporium.models.AreaModel;
import com.foodemporium.models.HomeModel;
import com.foodemporium.models.StateModel;
import com.foodemporium.utilities.ApiConstants;
import com.foodemporium.utilities.IResult;
import com.foodemporium.utilities.PreferencesManager;
import com.foodemporium.utilities.StaticValues;
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

public class SelectAreaActivity extends AppCompatActivity implements IResult {

    VollyService vollyService;
    SVProgressHUD svProgressHUD;
    IResult result = this;
    List<AreaModel> areaModelList;

    String TAG = SelectAreaActivity.class.getSimpleName();

    @BindView(R.id.staterecyclerView)
    RecyclerView staterecyclerView;

    AreaAdapter areaAdapter;

    @BindView(R.id.lbSelectLabel)
    TextView lbSelectLabel;

    @BindView(R.id.lbWelcome)
    TextView lbWelcome;

    StateModel stateModel;

    PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectstateactivity);
        // Hide ActionBar

        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        PreferencesManager.initializeInstance(this);
        preferencesManager = PreferencesManager.getInstance();

        stateModel = new StateModel();

        vollyService = new VollyService(result, this);
        svProgressHUD = new SVProgressHUD(this);
        lbWelcome.setVisibility(View.GONE);
        lbSelectLabel.setText("Select your area");
        initilizeReyclerView();

        stateModel = (StateModel) getIntent().getSerializableExtra("STATEMODEL");

        if (isNetworkConnectionAvailable(this, -1)) {

            getStatesFromApi(false);

        }
    }

    public void getStatesFromApi(Boolean isRefreshed) {
        if (!isRefreshed) {
            svProgressHUD.showWithStatus("Loading");
        }

        String paramsString = ApiConstants.GETCITYLIST + stateModel.stateId;

        vollyService.getStringResponseVolley("GETCITYLIST", paramsString);

    }

    public void initilizeReyclerView() {

        LinearLayoutManager verticalLayout
                = new LinearLayoutManager(
                SelectAreaActivity.this,
                LinearLayoutManager.VERTICAL,
                false);

        areaModelList = new ArrayList<>();
        staterecyclerView.setLayoutManager(verticalLayout);
        areaAdapter = new AreaAdapter(areaModelList, SelectAreaActivity.this);
        staterecyclerView.setAdapter(areaAdapter);

        staterecyclerView.addOnItemTouchListener(new RecyclerTouchListener(SelectAreaActivity.this, staterecyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                Log.d(TAG, "onClick: " + position);

                if (areaModelList.size() > 1) {

                    if (isNetworkConnectionAvailable(SelectAreaActivity.this, -1)) {

                        gotoLetsStarted(areaModelList.get(position));
                    } else {
                        Toast.makeText(SelectAreaActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
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
        if (requestType.equalsIgnoreCase("GETCITYLIST")) {
            if (isValidString(response)) {
                try {

                    areaModelList = new ArrayList<>();
                    areaModelList = serializeTheData(response);

                    if (areaModelList != null && areaModelList.size() > 0) {
                        areaAdapter.reloadAllContent(areaModelList);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<AreaModel> serializeTheData(String response) throws JSONException {

        JSONArray jsonArray = new JSONArray(response);

        ArrayList<AreaModel> areaModels = new ArrayList<>();

        for (int k = 0; k < jsonArray.length(); k++) {
            Gson gson = new Gson();
            JSONObject jsonObject1 = (JSONObject) jsonArray.get(k);
            AreaModel areaModel = gson.fromJson(jsonObject1.toString(), AreaModel.class);
            areaModels.add(areaModel);
        }

        return areaModels;
    }

    public void gotoLetsStarted(AreaModel areaModel) {


        String userID = preferencesManager.getStringValue(StaticValues.KEY_USERID);

        if (isValidString(userID)) {
            Intent iWeb = new Intent(SelectAreaActivity.this, HomeActivity.class);
            iWeb.putExtra("AREAMODEL", areaModel);
            iWeb.putExtra("STATEMODEL", stateModel);
            startActivity(iWeb);
        } else {
            Intent iWeb = new Intent(SelectAreaActivity.this, LetsStartedActivity.class);
            iWeb.putExtra("AREAMODEL", areaModel);
            iWeb.putExtra("STATEMODEL", stateModel);
            startActivity(iWeb);
        }
        finish();

    }


}
