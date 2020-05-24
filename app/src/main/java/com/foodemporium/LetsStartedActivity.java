package com.foodemporium;

import android.content.Intent;
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
import com.foodemporium.adapters.SelectStateAdapter;
import com.foodemporium.intrface.RecyclerTouchListener;
import com.foodemporium.intrface.RecyclerViewClickListener;
import com.foodemporium.models.AreaModel;
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

public class LetsStartedActivity extends AppCompatActivity implements View.OnClickListener {


    String TAG = SelectAreaActivity.class.getSimpleName();

    @BindView(R.id.lbletsstart)
    TextView lbletsstart;

    @BindView(R.id.lbDescription)
    TextView lbDescription;

    @BindView(R.id.btnStart)
    Button btnStart;

    @BindView(R.id.btnsignIn)
    Button btnSign;

    StateModel stateModel;

    AreaModel areaModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.letsstartedactivity);
        // Hide ActionBar

        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        stateModel = (StateModel) getIntent().getSerializableExtra("STATEMODEL");

        areaModel = (AreaModel) getIntent().getSerializableExtra("AREAMODEL");

        if (isNetworkConnectionAvailable(this, -1)) {


        }

        btnStart.setOnClickListener(this);
        btnSign.setOnClickListener(this);
    }


    public void gotoSelectAreaActivity(StateModel stateModel) {

        Intent iWeb = new Intent(LetsStartedActivity.this, DetailsEnterActivity.class);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnStart:
                Intent intentDetails = new Intent(LetsStartedActivity.this, DetailsEnterActivity.class);
                intentDetails.putExtra("AREAMODEL", areaModel);
                intentDetails.putExtra("STATEMODEL", stateModel);
                startActivity(intentDetails);
                break;
            case R.id.btnsignIn:
                Intent iLogin = new Intent(LetsStartedActivity.this, LoginActivity.class);
                iLogin.putExtra("AREAMODEL", areaModel);
                iLogin.putExtra("STATEMODEL", stateModel);
                startActivity(iLogin);
                break;
        }
    }
}
