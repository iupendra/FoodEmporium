package com.foodemporium.utilities;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Upendranath on 6/20/2017 Working on InstancyLearning.
 */

public interface IResult {


    void notifySuccess(String requestType, JSONObject response);

    void notifyError(String requestType, VolleyError error);

    void notifySuccess(String requestType, String response);

}
