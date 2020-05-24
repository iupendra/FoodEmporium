package com.foodemporium;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.foodemporium.utilities.ApiConstants;
import com.foodemporium.utilities.IResult;
import com.foodemporium.utilities.PreferencesManager;
import com.foodemporium.utilities.StaticValues;
import com.foodemporium.utilities.VollyService;
import com.google.android.material.textfield.TextInputEditText;


import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.foodemporium.utilities.Utilities.isValidString;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, IResult {

    @BindView(R.id.editMobile)
    TextInputEditText editMobile;

    @BindView(R.id.editPin)
    TextInputEditText editPin;

    @BindView(R.id.btnGo)
    Button btnGo;

    String TAG = LoginActivity.class.getSimpleName();

    VollyService vollyService;
    SVProgressHUD svProgressHUD;
    IResult result = this;

    PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);
        ButterKnife.bind(this);

        // Hide ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        vollyService = new VollyService(result, this);
        svProgressHUD = new SVProgressHUD(this);
        PreferencesManager.initializeInstance(this);
        preferencesManager = PreferencesManager.getInstance();
        btnGo.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {


        String mobileNumber = editMobile.getText().toString();

        String pinNumber = editPin.getText().toString();


        if (!isValidString(mobileNumber) && mobileNumber.length() < 5) {

            Toast.makeText(this, "Enter valid mobile number", Toast.LENGTH_SHORT).show();
        } else if (!isValidString(pinNumber) && mobileNumber.length() < 3) {
            Toast.makeText(this, "Enter pin number", Toast.LENGTH_SHORT).show();

        } else {

            loginMethod(mobileNumber, pinNumber);
//            Intent iLogin = new Intent(LoginActivity.this, HomeActivity.class);
//            startActivity(iLogin);
//            finish();

        }

    }

    public void loginMethod(String mobileNumber, String pinNumber) {

        svProgressHUD.showWithStatus("Sign In");

        String urlStr = ApiConstants.GETMEMBERLOGIN;

        JSONObject parameters = new JSONObject();

        try {

            parameters.put("Username", mobileNumber);
            parameters.put("Password", pinNumber);
            parameters.put("DeviceId", "894758947589");
            parameters.put("AppType", 1);

        } catch (JSONException e) {
            e.printStackTrace();
            svProgressHUD.dismiss();
        }
        String parameterString = parameters.toString();

        vollyService.getStringResponseFromPostMethod(parameterString, "GETMEMBERLOGIN", urlStr);
    }

    @Override
    public void notifySuccess(String requestType, JSONObject response) {
        svProgressHUD.dismiss();
    }

    @Override
    public void notifyError(String requestType, VolleyError error) {
        svProgressHUD.dismiss();
    }

    @Override
    public void notifySuccess(String requestType, String response) {

        if (isValidString(response)) {

            try {
                loginResponseValidate(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {

            Toast.makeText(this, "Invalid login", Toast.LENGTH_SHORT).show();
        }
        svProgressHUD.dismiss();
    }


    public void loginResponseValidate(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);

        JSONObject jsonObject1Child = jsonObject.getJSONObject("result");

        String statusCode = jsonObject1Child.optString("statusCode");

        JSONObject jsonObjectDetails = jsonObject.getJSONObject("details");

        String statusMessage = jsonObject1Child.optString("statusMessage");

        if (statusCode.equalsIgnoreCase("-1")) {

            Toast.makeText(this, " " + statusMessage, Toast.LENGTH_LONG).show();

        } else if (statusCode.equalsIgnoreCase("0")) {


            preferencesManager.setStringValue(jsonObjectDetails.optString("memberId"), StaticValues.KEY_USERID);
            preferencesManager.setStringValue(jsonObjectDetails.optString("email"), StaticValues.KEY_USERNAME);
            preferencesManager.setStringValue(jsonObjectDetails.toString(), StaticValues.KEY_USERJSONOBJ);

            Intent iLogin = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(iLogin);
            finish();

        }


    }

}