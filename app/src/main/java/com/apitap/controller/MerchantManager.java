package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.bean.GetDeliveryMerchantBean;
import com.apitap.model.bean.LocationBean;
import com.apitap.model.bean.LocationListBean;
import com.apitap.model.bean.MerchantDetailBean;
import com.apitap.model.bean.MerchantLocationBean;
import com.apitap.model.customclasses.Event;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sahil on 10/08/16.
 */
public class MerchantManager {

    private static final String TAG = MerchantManager.class.getSimpleName();
    public MerchantDetailBean merchantDetailBean;
    public LocationListBean locationListBean;
    public MerchantLocationBean merchantLocationBean;
    public String name, addresslineone, addresslinetwo, phonenumber, latmap, longmap, distance;
    public ArrayList<LocationBean> beanlis;
    public ArrayList<GetDeliveryMerchantBean> getDeliveryMerchantBeanArrayList = new ArrayList<GetDeliveryMerchantBean>();
    public ArrayList<String> latlist = new ArrayList<String>();
    public ArrayList<String> longlist = new ArrayList<String>();

    public void getMerchantDetail(Context context, String params, int key) {
        new ExecuteApi(context, key).execute(params);
    }

    public void getMerchantLocation(Context context, String params, int key) {
        new ExecuteApi(context, key).execute(params);
    }

    public void getMerchantDistance(Context context, String params, int key) {
        new ExecuteApi(context, key).execute(params);
    }

    public void writeReview(Context context, String params, int key) {
        new ExecuteApi(context, key).execute(params);
    }

    public void merchantDelivery(Context context, String params, int key) {
        new ExecuteApi(context, key).execute(params);
    }

    private class ExecuteApi extends AsyncTask<String, String, String> {
        private final int key;
        Context mContext;

        ExecuteApi(Context context, int key) {
            mContext = context;
            this.key = key;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_merchantItem---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d(TAG, s);

                if (key == Constants.GET_MERCHANT_SUCCESS) {
                    merchantDetailBean = new Gson().fromJson(s, MerchantDetailBean.class);

                    if (merchantDetailBean.getRESULT().get(0).getStatus().equals("Transaction Approved")) {
                        EventBus.getDefault().post(new Event(key, ""));
                    } else {
                        EventBus.getDefault().post(new Event(-1, ""));
                    }
                } else if (key == Constants.GET_MERCHANT_LOCATION_SUCCESS) {
                    int maxLogSize = 2000;
                    for (int i = 0; i <= s.length() / maxLogSize; i++) {
                        int start = i * maxLogSize;
                        int end = (i + 1) * maxLogSize;
                        end = end > s.length() ? s.length() : end;
                        Log.v("mainsArraysLocations", s.substring(start, end));
                    }
                    merchantLocationBean = new Gson().fromJson(s, MerchantLocationBean.class);

                    if (merchantLocationBean.getRESULT().get(0).getStatus().equals("Transaction Approved")) {
                        beanlis = new ArrayList<LocationBean>();

                        JSONObject obj = new JSONObject(s);
                        JSONArray array = obj.getJSONArray("RESULT");

                        JSONObject objone = array.getJSONObject(0);
                        JSONArray arrayone = objone.getJSONArray("RESULT");
                        for (int j = 0; j < arrayone.length(); j++) {
                            JSONObject objtwo = arrayone.getJSONObject(j);
                            JSONObject objthre = objtwo.getJSONObject("AD");
                            JSONObject objCo = objthre.getJSONObject("CO");
                            JSONObject objPh = objtwo.getJSONObject("PH");
                            JSONObject objLc = objthre.getJSONObject("ZP");
                            name = objCo.getString("_47_18");

                            phonenumber = /*objPh.getString("_48_28");*/objPh.has("_48_28") ? objPh.getString("_48_28") : "";
                            //         Log.d("countrynamekkk", phonenumber + "");
                            addresslineone = objthre.getString("_114_12");
                            addresslinetwo = objthre.getString("_114_13");
                            latmap = objLc.getString("_120_38");
                            longmap = objLc.getString("_120_39");
                            distance = objLc.getString("_122_107");
                            String locationName = objtwo.getString("_114_70");

                            latlist.add(latmap);
                            longlist.add(longmap);
                            LocationBean bean = new LocationBean(name, addresslineone, addresslinetwo, phonenumber, latmap, longmap, locationName, distance);
                            beanlis.add(bean);

                        }
                        EventBus.getDefault().post(new Event(Constants.GET_MERCHANT_LOCATION_SUCCESS, ""));
                    } else {
                        EventBus.getDefault().post(new Event(-1, ""));
                    }
                } else if (key == Constants.WRITE_REVIEW_SUCCESS) {
                    merchantLocationBean = new Gson().fromJson(s, MerchantLocationBean.class);

                    if (merchantLocationBean.getRESULT().get(0).getStatus().equals("Transaction Approved")) {
                        EventBus.getDefault().post(new Event(key, ""));
                    } else {
                        EventBus.getDefault().post(new Event(-1, ""));
                    }
                } else if (key == Constants.GET_Delivery_Merchant) {

                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("RESULT");
                    Log.d("jsonArrayd", jsonArray1 + "");
                    ArrayList<GetDeliveryMerchantBean> arrayAds = new ArrayList<GetDeliveryMerchantBean>();
                    for (int j = 0; j < jsonArray1.length(); j++) {
                        GetDeliveryMerchantBean detailsBean = new GetDeliveryMerchantBean();
                        JSONObject objtwo = jsonArray1.getJSONObject(j);
                        String id = objtwo.getString("_122_109");
                        detailsBean.setId(id);
                        String options = objtwo.getString("_122_133");
                        detailsBean.setDelviery_options(options);
                        String price = objtwo.getString("_122_161");
                        String pickup = objtwo.getString("_122_39");
                        detailsBean.setDelivery_price(price);
                        detailsBean.setPickup(pickup);
                        arrayAds.add(detailsBean);

                    }
                    getDeliveryMerchantBeanArrayList = arrayAds;
                    EventBus.getDefault().post(new Event(Constants.GET_Delivery_Merchant, ""));
                }
                if (key == Constants.GET_MERCHANT_DISTANCE_SUCCESS) {
                    locationListBean = new Gson().fromJson(s, LocationListBean.class);
                    if (locationListBean.getRESULT().get(0).get44().equals("Transaction Approved")) {
                        EventBus.getDefault().post(new Event(Constants.GET_MERCHANT_DISTANCE_SUCCESS, ""));
                    }
                }
            } catch (Exception e) {
                EventBus.getDefault().post(new Event(-1, ""));
                e.printStackTrace();
            }
        }
    }

    private class ExecuteApiCheckin extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiCheckin(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_checkin---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, s);

        }
    }

}
