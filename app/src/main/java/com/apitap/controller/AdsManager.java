package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.AdsDetailWithMerchant;
import com.apitap.model.bean.AdsListBean;
import com.apitap.model.bean.AdsListBean2;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sahil on 10/08/16.
 */
public class AdsManager {

    private static final String TAG = AdsManager.class.getSimpleName();
    public AdsListBean adsListBean;
    public AdsListBean2 adsListBean2;
    private ArrayList<String> arrayList = new ArrayList<>();
    public static ArrayList<AdsDetailWithMerchant> url_maps = new ArrayList<AdsDetailWithMerchant>();


    public void getAllAds(Context context, String params, int key) {
        new ExecuteApi(context, key).execute(params);
    }

    public void getBusinessAds(Context context, String params, int key) {
        new ExecuteAdApi(context, key).execute(params);
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
            Log.d(TAG, "response_ads_Item---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int maxLogSize = 2000;
            for (int i = 0; i <= s.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > s.length() ? s.length() : end;
                Log.v("Adsss", s.substring(start, end));
            }
            ATPreferences.putString(mContext,Constants.BUSINESS_ADS,"no");
            try {
                ArrayList<AdsDetailWithMerchant> imageArray = new ArrayList<>();
                ArrayList<String> matchList = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("RESULT");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    AdsDetailWithMerchant detailsBean = new AdsDetailWithMerchant();
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);

                    String adName = jsonObject2.getString("_120_83");
                    String imageUrl = jsonObject2.getString("_121_170");
                    String merchantName = jsonObject2.getString("_114_70");
                    String videoName = jsonObject2.getString("_121_15");
                    Log.d("adNameNew", adName + "  " + videoName);
                    String isSeen = jsonObject2.getString("_114_9");
                    String id = jsonObject2.getString("_121_18");
                    String merchantId = jsonObject2.getString("_53");
                    String business_type = jsonObject2.getString("_120_45");
                    String desc = jsonObject2.getString("_120_157");
                    JSONArray jsonArray2 = jsonObject2.getJSONArray("IR");
                    detailsBean.setName(Utils.hexToASCII(adName));
                    detailsBean.setId(id);
                    detailsBean.setMerchantId(merchantId);
                    detailsBean.setVideo(videoName);
                    detailsBean.setBusinssType(business_type);
                    detailsBean.setMerchantname(merchantName);
                    detailsBean.setSeen(isSeen);
                    detailsBean.setDesc(Utils.hexToASCII(desc));
                    detailsBean.setImageUrl(imageUrl);

                    imageArray.add(detailsBean);

                }

                url_maps = imageArray;

                if (key == Constants.ADS_LISTING_SUCCESS) {
                    Log.d(TAG, s);
                    adsListBean = new Gson().fromJson(s, AdsListBean.class);
                    if (adsListBean.getRESULT().get(0).getStatus().equals("Transaction Approved")) {
                        EventBus.getDefault().post(new Event(key, ""));
                    } else {
                        EventBus.getDefault().post(new Event(-1, ""));
                    }

                }
                if (key==Constants.NOTIFICATION_ARRIVED){
                    EventBus.getDefault().post(new Event(key, ""));
                }
            } catch (Exception e) {
                EventBus.getDefault().post(new Event(-1, ""));
                e.printStackTrace();
            }
        }
    }


    private class ExecuteAdApi extends AsyncTask<String, String, String> {
        private int key;
        Context mContext;

        ExecuteAdApi(Context context, int key) {
            mContext = context;
            this.key = key;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_adsbusiness_Item---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int maxLogSize = 2000;
            for (int i = 0; i <= s.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > s.length() ? s.length() : end;
                Log.v("Adsss", s.substring(start, end));
            }
            arrayList = new ArrayList<>();
            try {
                ArrayList<AdsDetailWithMerchant> imageArray = new ArrayList<>();
                ArrayList<String> matchList = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("RESULT");
                for (int i = 0; i < jsonArray1.length(); i++) {

                    JSONObject jsonObject21 = jsonArray1.getJSONObject(i);
                    String business_type = jsonObject21.getString("_120_83");
                    JSONArray jsonArray2 = jsonObject21.getJSONArray("AD");
                    if (jsonArray2.length() > 0) {
                        for (int j = 0; j < jsonArray2.length(); j++) {
                            AdsDetailWithMerchant detailsBean = new AdsDetailWithMerchant();
                            JSONObject jsonObject2 = jsonArray2.getJSONObject(j);
                            String adName = jsonObject2.getString("_120_83");
                            String imageUrl = jsonObject2.getString("_121_170");
                            String merchantName = jsonObject2.getString("_114_70");
                            String videoName = jsonObject2.getString("_121_15");

                             String isSeen = jsonObject2.getString("_114_9");
                            //String isSeen = "true";
                            String id = jsonObject2.getString("_121_18");
                            String merchantId = jsonObject2.getString("_53");

                            String desc = jsonObject2.getString("_120_157");
                            JSONArray jsonArray3 = jsonObject2.getJSONArray("IR");

                            if (!arrayList.contains(adName)) {
                                arrayList.add(adName);
                                detailsBean.setName(adName);
                                Log.d("adNameNew", adName + "  " + videoName);
                                detailsBean.setId(id);
                                detailsBean.setMerchantId(merchantId);
                                detailsBean.setVideo(videoName);
                                detailsBean.setBusinssType(business_type);
                                detailsBean.setMerchantname(merchantName);
                                detailsBean.setSeen(isSeen);
                                detailsBean.setDesc(desc);
                                detailsBean.setImageUrl(imageUrl);


                                imageArray.add(detailsBean);
                            }
                        }

                    }
                }

                url_maps = imageArray;

                if (key == Constants.ADS_LISTING_SUCCESS) {
                    Log.d(TAG, s);
                    ATPreferences.putString(mContext,Constants.BUSINESS_ADS,"yes");
                    adsListBean2 = new Gson().fromJson(s, AdsListBean2.class);
                    if (adsListBean.getRESULT().get(0).getStatus().equals("Transaction Approved")) {
                        EventBus.getDefault().post(new Event(key, ""));
                    } else {
                        EventBus.getDefault().post(new Event(-1, ""));
                    }

                }
            } catch (Exception e) {
                EventBus.getDefault().post(new Event(-1, ""));
                e.printStackTrace();
            }
        }
    }


}
