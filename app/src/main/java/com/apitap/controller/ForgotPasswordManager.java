package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.preferences.ATPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by apple on 10/08/16.
 */
public class ForgotPasswordManager {
    private static final String TAG = LoginManager.class.getSimpleName();

    public void getForgotPassword(Context context, String params) {
        new ExecuteApi(context).execute(params);
    }


    private class ExecuteApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_default_api---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                JSONObject jsonObject = new JSONObject(s);
                ATPreferences.putString(mContext, Constants.KEY_USER_DEFAULT, jsonObject.has("_192") ? jsonObject.getString("_192") : "");
                JSONArray jArray = jsonObject.getJSONArray("RESULT");
                JSONObject jobj = jArray.getJSONObject(0);
                Toast.makeText(mContext, jobj.getString("_44"), Toast.LENGTH_SHORT).show();
                ATPreferences.putString(mContext, Constants.KEY_USER_PIN, jsonObject.has("_39") ? jsonObject.getString("_39") : "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
