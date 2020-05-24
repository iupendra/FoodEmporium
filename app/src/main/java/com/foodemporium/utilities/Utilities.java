package com.foodemporium.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.foodemporium.models.TimeModel;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Utilities {

    public static boolean isNetworkConnectionAvailable(Context ctx, int networkType) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo;
        if (networkType == -1) {
            netInfo = cm.getActiveNetworkInfo();
        } else {
            netInfo = cm.getNetworkInfo(networkType);
        }

        if (netInfo != null && netInfo.isConnectedOrConnecting()
                && netInfo.isAvailable()) {
            return true;
        } else {
            return false;

        }
    }


    public static boolean isValidString(String str) {
        try {
            if (str == null || str.equals("") || str.equals("null") || str.equals("undefined") || str.equals("null\n")) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    public static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }

    public static String getCurrentDateTime(String format) {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = null;
        if (isValidString(format)) {
            df = new SimpleDateFormat(format);
        } else {
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        return df.format(c.getTime());
    }

    public static String getCurrentDateTimeInUTC(String format) {

        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date myDate = new Date();
        Log.d("TIME", "getCurrentDateTimeInNormal: " + myDate);
        c.setTime(myDate);
        Date dateTimeInUtc = c.getTime();

        Log.d("TIME", "getCurrentDateTimeInUTC: " + dateTimeInUtc);
        SimpleDateFormat df = null;
        if (isValidString(format)) {
            df = new SimpleDateFormat(format);
        } else {
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        return df.format(dateTimeInUtc);
    }


    public static Date getCurrentDateTimeInDate(String dateStr) {

        Date date = null;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = format.parse(dateStr);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }



}
