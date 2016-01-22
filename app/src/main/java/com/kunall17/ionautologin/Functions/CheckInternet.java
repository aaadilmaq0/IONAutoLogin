package com.kunall17.ionautologin.Functions;

import android.content.Context;
import android.os.AsyncTask;

import com.kunall17.ionautologin.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.net.SocketTimeoutException;


/**
 * Created by kunall17 on 12/24/15.
 */

public class CheckInternet extends AsyncTask<Boolean, Boolean, Boolean> {
    OkHttpClient client;
    differentFunctions dfListener;
    Context context;

    public CheckInternet(differentFunctions dfListener, Context context) {
        this.dfListener = dfListener;
        this.context = context;
    }


    @Override
    protected void onPreExecute() {
        client = new OkHttpClient();
        dfListener.updateStatus(context.getResources().getString(R.string.status_checkingInternet));
    }

    @Override
    protected Boolean doInBackground(Boolean... booleans) {
        try {
            System.out.println("Here is the complete shit-");
            String url = "https://www.google.co.in/";
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();

//            SocketTimeoutException: timeout
            if (url == response.request().urlString() && response.code() == 200) {
                return true;
            }
        } catch (SocketTimeoutException ste) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        dfListener.updateStatus(context.getResources().getString(R.string.status_idle));

        if (aBoolean) {
            dfListener.LoginDone(LoginConstants.LOGIN_NET_WORKING);
        } else {
            dfListener.LoginDone(LoginConstants.LOGIN_NET_NOTWORKING);
        }
    }

}