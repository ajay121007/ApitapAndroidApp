package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;

import com.apitap.model.bean.Favdetailsbean;
import com.apitap.model.bean.ImagesBean;
import com.apitap.model.bean.MerchantDetailBean;
import com.apitap.model.bean.MerchantLocationBean;
import com.apitap.model.bean.MerchantStore;
import com.apitap.model.bean.Storebean;
import com.apitap.model.customclasses.Event;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sahil on 10/08/16.
 */
public class MerchantStoresManager {

    private static final String TAG = MerchantStoresManager.class.getSimpleName();
    public MerchantStore merchantStoresBean;
    public ArrayList<Storebean> storebeanArrayList = new ArrayList<Storebean>();
    public HashMap<Integer, ArrayList<Storebean>> itemsData = new HashMap<Integer, ArrayList<Storebean>>();


    public void getMerchantStoreDetail(Context context, String params) {
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
           Log.d(TAG, "response_merchantStoreItem---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "" + s);
            try {
                Log.d(TAG, "" + s);
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("RESULT");


                for (int i =0;i<jsonArray1.length();i++){

                    ArrayList<Storebean> arrayAds = new ArrayList<>();
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                    String category = jsonObject2.getString("_120_45");
                    String title = jsonObject2.getString("_120_83");
                    JSONArray jsonArray2 = jsonObject2.getJSONArray("ME");
                    for (int j =0;j<jsonArray2.length();j++) {
                        Storebean favdetailsbean = new Storebean();
                        JSONObject jsonObject3 = jsonArray2.getJSONObject(j);
                        String id = jsonObject3.getString("_53");
                        String img = jsonObject3.getString("_121_170");
                        String name = jsonObject3.getString("_114_70");
                        Log.d("Imggs", id);
                        favdetailsbean.setId(id);
                        favdetailsbean.setName(name);
                        favdetailsbean.setTitle(title);
                        favdetailsbean.setImageUrl(img);
                        favdetailsbean.setCategoryName(category);
                        arrayAds.add(favdetailsbean);

                    }
                    itemsData.put(i, arrayAds);

                }

                for (int k = 0 ; k<itemsData.size();k++){
                    Log.d("atamaoss",itemsData.get(k).get(0).getId());
                }
               // storebeanArrayList =arrayAds;



                merchantStoresBean = new Gson().fromJson(s, MerchantStore.class);
                if (merchantStoresBean.getRESULT().get(0).get44().equals("Transaction Approved")) {
                    EventBus.getDefault().post(new Event(Constants.STORES_SUCCESS, ""));
                } else {
                    EventBus.getDefault().post(new Event(Constants.STORES_FAILURE, ""));
                }
            } catch (Exception e) {
                EventBus.getDefault().post(new Event(-1, ""));
                e.printStackTrace();
            }
        }
    }
}
