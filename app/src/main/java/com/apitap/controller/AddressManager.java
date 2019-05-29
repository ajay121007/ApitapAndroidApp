package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.bean.getaddress.GetAddressProp;
import com.apitap.model.customclasses.Event_Add_Address;
import com.apitap.model.customclasses.Event_Address;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by apple on 10/08/16.
 */
public class AddressManager {

    private static final String TAG = AddressManager.class.getSimpleName();


    public void getAddresses(Context context, String params, int key) {
        new ExecuteApi(context, key).execute(params);
    }

    public void addAddresse(Context context, String params, int key) {
        new ExecuteApi(context, key).execute(params);
    }

    private class ExecuteApi extends AsyncTask<String, String, String> {
        private int key;
        Context mContext;

        ExecuteApi(Context context, int key) {
            mContext = context;
            this.key = key;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_Address Item---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d(TAG, s);
                GetAddressProp prop = new Gson().fromJson(s, GetAddressProp.class);
                if (key == Constants.GET_ADDRESS_SUCCESS) {


                    Event_Address event = new Event_Address();
                    event.setKey(key);
                    if (prop.getRESULT().get(0).getStatus().equals("Transaction Approved")) {
                        event.setResponse(prop.getRESULT().get(0).getRESULT().get(0).getAddresses());
                    } else {
                        event.setResponse(null);
                    }
                    EventBus.getDefault().post(event);
                } else if (key == Constants.ADD_ADDRESS_SUCCESS) {
                    Event_Add_Address event = new Event_Add_Address();
                    event.setKey(key);
                    if (prop.getRESULT().get(0).getStatus().equals("Transaction Approved")) {
                        event.setSuccess(true);
                    } else {
                        event.setSuccess(false);
                    }
                    EventBus.getDefault().post(event);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
