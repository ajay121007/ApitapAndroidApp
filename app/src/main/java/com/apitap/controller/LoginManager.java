package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ashok-kumar on 26/7/16.
 */

public class LoginManager {
    private static final String TAG = LoginManager.class.getSimpleName();
    boolean isLogin;

    public void getLogin(Context context, String params) {
        isLogin = false;
        new ExecuteApi(context).execute(params);
    }

    public void doLogin(Context context, String params) {
        isLogin = true;
        new ExecuteApi(context).execute(params);
    }

    public void registerFCM(Context context, String params) {
        isLogin = false;
        new ExecuteFCMApi(context).execute(params);
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
            Log.d("ResponseLogin",s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (!isLogin)
                    ATPreferences.putString(mContext, Constants.KEY_USER_DEFAULT, jsonObject.has("_192") ? jsonObject.getString("_192") : "");
                JSONArray jArray = jsonObject.getJSONArray("RESULT");
                JSONObject jobj = jArray.getJSONObject(0);
                if (!isLogin)
                    ATPreferences.putString(mContext, Constants.KEY_USER_PIN, jsonObject.has("_39") ? jsonObject.getString("_39") : "");

                JSONArray imageURLArray = jobj.getJSONArray("RESULT");
                JSONObject imageURLobj = imageURLArray.getJSONObject(0);

                if (!isLogin) {
                    for (int i = 0; i < imageURLArray.length(); i++) {
                        JSONObject object = imageURLArray.getJSONObject(i);
                        if (object.getString("_127_11").equals("IMAGE_URL"))
                            ATPreferences.putString(mContext, Constants.KEY_IMAGE_URL, object.has("_127_12") ? object.getString("_127_12") : "");
                        if (object.getString("_127_11").equals("VIDEO_URL"))
                            ATPreferences.putString(mContext, Constants.KEY_VIDEO_URL, object.has("_127_12") ? object.getString("_127_12") : "");
                    }

                }
                if (jobj.getString("_44").equals("Transaction Approved")) {
                    if (isLogin) {
                        ATPreferences.putString(mContext, Constants.KEY_USERID, imageURLobj.getString("_53"));
                        EventBus.getDefault().post(new Event(Constants.LOGIN_SUCCESS, ""));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private class ExecuteFCMApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteFCMApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_fcm_api---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("ResponseFCM",s+"");
        }
    }

}
