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
import com.foodemporium.adapters.SelectStateAdapter;
import com.foodemporium.intrface.RecyclerTouchListener;
import com.foodemporium.intrface.RecyclerViewClickListener;
import com.foodemporium.models.StateModel;
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

public class SelectStateActivity extends AppCompatActivity implements IResult {

    VollyService vollyService;
    SVProgressHUD svProgressHUD;
    IResult result = this;
    List<StateModel> stateModelList;

    String TAG = SelectAreaActivity.class.getSimpleName();

    @BindView(R.id.staterecyclerView)
    RecyclerView staterecyclerView;

    @BindView(R.id.lbSelectLabel)
    TextView lbSelectLabel;

    @BindView(R.id.lbWelcome)
    TextView lbWelcome;

    SelectStateAdapter stateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectstateactivity);
        // Hide ActionBar

        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        vollyService = new VollyService(result, this);
        svProgressHUD = new SVProgressHUD(this);

        initilizeReyclerView();
        lbSelectLabel.setText("Select your state");

        if (isNetworkConnectionAvailable(this, -1)) {

            getStatesFromApi(false);
        }
    }

    public void getStatesFromApi(Boolean isRefreshed) {
        if (!isRefreshed) {
//            svProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.BlackCancel);
            svProgressHUD.showWithStatus("Loading");
        }

        String paramsString = ApiConstants.GETSTATELIST;

        vollyService.getStringResponseVolley("GETSTATELIST", paramsString);

    }

    public void initilizeReyclerView() {


        LinearLayoutManager verticalLayout
                = new LinearLayoutManager(
                SelectStateActivity.this,
                LinearLayoutManager.VERTICAL,
                false);

        stateModelList = new ArrayList<>();
        staterecyclerView.setLayoutManager(verticalLayout);
        stateAdapter = new SelectStateAdapter(stateModelList, SelectStateActivity.this);
        staterecyclerView.setAdapter(stateAdapter);

        staterecyclerView.addOnItemTouchListener(new RecyclerTouchListener(SelectStateActivity.this, staterecyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                Log.d(TAG, "onClick: " + position);

                if (stateModelList.size() > 1) {

                    if (isNetworkConnectionAvailable(SelectStateActivity.this, -1)) {

                        gotoSelectAreaActivity(stateModelList.get(position));

                    } else {
                        Toast.makeText(SelectStateActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
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
        Log.d(TAG, "notifyError: "+error.getLocalizedMessage());
        svProgressHUD.dismiss();
    }

    @Override
    public void notifySuccess(String requestType, String response) {
        Log.d(TAG, "notifySuccess: ");
        svProgressHUD.dismiss();
        if (requestType.equalsIgnoreCase("GETSTATELIST")) {
            if (isValidString(response)) {
                try {
                    stateModelList = new ArrayList<>();
                    stateModelList = serializeTheData(response);

                    if (stateModelList != null && stateModelList.size() > 0) {
                        stateAdapter.reloadAllContent(stateModelList);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void gotoSelectAreaActivity(StateModel stateModel) {

        Intent iWeb = new Intent(SelectStateActivity.this, SelectAreaActivity.class);
        iWeb.putExtra("STATEMODEL", stateModel);
        startActivity(iWeb);

    }

    public List<StateModel> serializeTheData(String response) throws JSONException {

        JSONArray jsonArray = new JSONArray(response);

        ArrayList<StateModel> stateModels = new ArrayList<>();

        for (int k = 0; k < jsonArray.length(); k++) {
            Gson gson = new Gson();
            JSONObject jsonObject1 = (JSONObject) jsonArray.get(k);
            StateModel stateModel = gson.fromJson(jsonObject1.toString(), StateModel.class);
            stateModels.add(stateModel);
        }

        return stateModels;
    }

}
