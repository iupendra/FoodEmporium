package com.foodemporium;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.foodemporium.models.AreaModel;
import com.foodemporium.models.StateModel;
import com.foodemporium.utilities.ApiConstants;
import com.foodemporium.utilities.IResult;
import com.foodemporium.utilities.VollyService;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.foodemporium.utilities.Utilities.isValidEmail;
import static com.foodemporium.utilities.Utilities.isValidString;

public class ConfirmDetailsEnterActivity extends AppCompatActivity implements View.OnClickListener, IResult {


    @BindView(R.id.editEmail)
    TextInputEditText editEmail;

    @BindView(R.id.editFname)
    TextInputEditText editFname;

    @BindView(R.id.editMobile)
    TextInputEditText editMobile;

    @BindView(R.id.editSurname)
    TextInputEditText editSurname;


    @BindView(R.id.editPin)
    TextInputEditText editPin;

    @BindView(R.id.btnGo)
    Button btnGo;

    String TAG = ConfirmDetailsEnterActivity.class.getSimpleName();

    VollyService vollyService;
    SVProgressHUD svProgressHUD;
    IResult result = this;

    String parameterString = "";

    String stateID = "", cityID = "";

    StateModel stateModel;

    AreaModel areaModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmdetailsactivity);
        ButterKnife.bind(this);

        // Hide ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        btnGo.setOnClickListener(this);

        parameterString = getIntent().getStringExtra("PARAMETERSTRING");

        stateModel = (StateModel) getIntent().getSerializableExtra("STATEMODEL");

        areaModel = (AreaModel) getIntent().getSerializableExtra("AREAMODEL");

        vollyService = new VollyService(result, this);
        svProgressHUD = new SVProgressHUD(this);

        try {
            updateUI();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void updateUI() throws JSONException {


        JSONObject jsonObject = new JSONObject(parameterString);

        editFname.setText(jsonObject.optString("FirstName"));

        editMobile.setText(jsonObject.optString("MobileNumber"));

        editSurname.setText(jsonObject.optString("LastName"));

        editEmail.setText(jsonObject.optString("Email"));

        stateID = jsonObject.optString("StateId");

        cityID = jsonObject.optString("CityId");

    }

    public void signUpMethod() {


        String strEmail = editEmail.getText().toString();

        String strFname = editFname.getText().toString();

        String strSurName = editSurname.getText().toString();

        String strMobile = editMobile.getText().toString();

        String strPin = editPin.getText().toString();

        String urlStr = ApiConstants.MEMBERREGISTER;

        JSONObject parameters = new JSONObject();


        if (!isValidString(strFname)) {

            Toast.makeText(this, "Enter Firstname", Toast.LENGTH_LONG).show();

        } else if (!isValidString(strSurName)) {

            Toast.makeText(this, "Enter surname", Toast.LENGTH_LONG).show();

        } else if (!isValidString(strMobile)) {

            Toast.makeText(this, "Enter Mobile", Toast.LENGTH_LONG).show();

        } else if (!isValidEmail(strEmail)) {

            Toast.makeText(this, "Email is Invalid", Toast.LENGTH_LONG).show();

        } else if (!isValidString(strPin) && strPin.length() < 3) {

            Toast.makeText(this, "Enter Password  is Invalid", Toast.LENGTH_LONG).show();

        } else {

            try {

                parameters.put("FirstName", strFname);
                parameters.put("LastName", strSurName);
                parameters.put("Email", strEmail);
                parameters.put("StateId", stateID);
                parameters.put("PostCode", "500090");
                parameters.put("CityId", cityID);
                parameters.put("MobileNumber", strMobile);
                parameters.put("Pin", strPin);

            } catch (JSONException e) {
                e.printStackTrace();
                svProgressHUD.dismiss();
            }

            svProgressHUD.showWithStatus("Registering");

            String parameterString = parameters.toString();

            vollyService.getStringResponseFromPostMethod(parameterString, "MEMBERREGISTER", urlStr);
        }
    }


    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        signUpMethod();
    }

    @Override
    public void notifySuccess(String requestType, JSONObject response) {
        svProgressHUD.dismiss();
        Log.d(TAG, "notifySuccess: " + response);

    }

    @Override
    public void notifyError(String requestType, VolleyError error) {
        svProgressHUD.dismiss();
        Log.d(TAG, "notifyError: ");
    }

    @Override
    public void notifySuccess(String requestType, String response) {
        Log.d(TAG, "notifySuccess: " + response);
        svProgressHUD.dismiss();
        if (requestType.equalsIgnoreCase("MEMBERREGISTER")) {
            if (isValidString(response)) {
                try {
                    gotoNextActvity(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void gotoNextActvity(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);

        int cstID = jsonObject.optInt("statusCode");

        if (cstID > 0) {
            Toast.makeText(this, "" + jsonObject.optString("statusMessage"), Toast.LENGTH_SHORT).show();
            Intent iLogin = new Intent(ConfirmDetailsEnterActivity.this, LoginActivity.class);
            iLogin.putExtra("AREAMODEL", areaModel);
            iLogin.putExtra("STATEMODEL", stateModel);
            startActivity(iLogin);
        } else {

            Toast.makeText(this, "" + jsonObject.optString("statusMessage"), Toast.LENGTH_SHORT).show();

        }

    }

}