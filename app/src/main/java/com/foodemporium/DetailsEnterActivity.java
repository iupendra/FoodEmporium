package com.foodemporium;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.foodemporium.models.AreaModel;
import com.foodemporium.models.StateModel;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.foodemporium.utilities.Utilities.isValidEmail;
import static com.foodemporium.utilities.Utilities.isValidString;


public class DetailsEnterActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.editEmail)
    EditText editEmail;

    @BindView(R.id.editFname)
    EditText editFname;

    @BindView(R.id.editMobile)
    EditText editMobile;

    @BindView(R.id.editSurname)
    EditText editSurname;

    @BindView(R.id.btnGo)
    Button btnGo;

    StateModel stateModel;

    AreaModel areaModel;

    String TAG = DetailsEnterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailsenteractivity);
        ButterKnife.bind(this);

        // Hide ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        btnGo.setOnClickListener(this);

        stateModel = (StateModel) getIntent().getSerializableExtra("STATEMODEL");

        areaModel = (AreaModel) getIntent().getSerializableExtra("AREAMODEL");


    }


    public void gotoNextConfirmDetails() {

        String strEmail = editEmail.getText().toString();

        String strFname = editFname.getText().toString();

        String strSurName = editSurname.getText().toString();

        String strMobile = editMobile.getText().toString();

        if (!isValidString(strFname)) {

            Toast.makeText(this, "Enter Firstname", Toast.LENGTH_LONG).show();

        } else if (!isValidString(strSurName)) {

            Toast.makeText(this, "Enter surname", Toast.LENGTH_LONG).show();

        } else if (!isValidString(strMobile)) {

            Toast.makeText(this, "Enter Mobile", Toast.LENGTH_LONG).show();

        } else if (!isValidEmail(strEmail)) {

            Toast.makeText(this, "Email is Invalid", Toast.LENGTH_LONG).show();

        } else {

            JSONObject parameters = new JSONObject();

            try {

                parameters.put("FirstName", strFname);
                parameters.put("LastName", strSurName);
                parameters.put("Email", strEmail);
                parameters.put("StateId", stateModel.stateId);
                parameters.put("CityId", areaModel.cityId);
                parameters.put("MobileNumber", strMobile);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            String parameterString = parameters.toString();

            Intent iWeb = new Intent(DetailsEnterActivity.this, ConfirmDetailsEnterActivity.class);
//            iWeb.putExtra("AREAMODEL", areaModel);
//            iWeb.putExtra("STATEMODEL", stateModel);
            iWeb.putExtra("PARAMETERSTRING", parameterString);
            startActivity(iWeb);
        }

    }


    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        gotoNextConfirmDetails();
    }
}