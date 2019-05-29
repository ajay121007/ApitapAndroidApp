package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.AdsBean;
import com.apitap.model.bean.AdsDetailBean;
import com.apitap.model.bean.AdsDetailWithMerchant;
import com.apitap.model.bean.AdsListBean;
import com.apitap.model.bean.ImagesBean;
import com.apitap.model.bean.MessageListBean;
import com.apitap.model.bean.SearchBean;
import com.apitap.model.bean.SearchItemBean;
import com.apitap.model.bean.SearchSpecialBean;
import com.apitap.model.bean.ShopAsstSearchBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sahil on 10/08/16.
 */
public class SearchItemsManager {

    private static final String TAG = SearchItemsManager.class.getSimpleName();
    public SearchItemBean messageListBean;
    public SearchSpecialBean searchSpecialBean;
    public ShopAsstSearchBean asstSearchBean;
    public HashMap<Integer, AdsBean> ads = new HashMap<Integer, AdsBean>();
    public ArrayList<AdsDetailBean> arrayAds = new ArrayList<AdsDetailBean>();
    public AdsListBean adsListBean;
    public HashMap<Integer, ArrayList<SearchBean>> itemsData = new HashMap<Integer, ArrayList<SearchBean>>();
    public static ArrayList<AdsDetailWithMerchant> url_maps_new = new ArrayList<AdsDetailWithMerchant>();
    public HashMap<Integer, AdsBean> url_maps = new HashMap<Integer, AdsBean>();
    public static ArrayList<AdsDetailWithMerchant> url_maps1 = new ArrayList<AdsDetailWithMerchant>();

    public void getAllSearchItems(Context context, String params) {
        new ExecuteApi(context).execute(params);
    }

    public void getAllSearchProduct(Context context, String params) {
        new ExecuteApiProducts(context).execute(params);
    }

    public void getSearchShoppingAsst(Context context, String params) {
        new ExecuteSearchShopAsstApi(context).execute(params);
    }

    private class ExecuteApi extends AsyncTask<String, String, String> {
        private int key;
        Context mContext;

        ExecuteApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "search_message_Item---" + response);

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
                Log.v("searchOne", s.substring(start, end));
            }
            try {
                JSONObject jsonObject2 = null;
                Log.d(TAG, s);

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("RESULT");
                Log.d("JsonSizes", jsonArray1.length() + "");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    jsonObject2 = jsonArray1.getJSONObject(i);
                    Log.d("Helos", "Json" + jsonObject2.length());
                }
                assert jsonObject2 != null;
                if (jsonObject2.length() != 0) {
                    messageListBean = new Gson().fromJson(s, SearchItemBean.class);

                    if (messageListBean.getResult().get(0).getStatus().equals("Transaction Approved")) {
                        {
                            EventBus.getDefault().post(new Event(Constants.SEARCH_ITEM_SUCCESS, ""));
                        }
                    } else {
                        EventBus.getDefault().post(new Event(-1, ""));
                    }
                } else {
                    EventBus.getDefault().post(new Event(Constants.SEARCH_ITEM_SUCCESS_Empty, ""));
                }
            } catch (Exception e) {
                EventBus.getDefault().post(new Event(-1, ""));
                e.printStackTrace();
            }
        }
    }

    private class ExecuteApiProducts extends AsyncTask<String, String, String> {
        private int key;
        Context mContext;

        ExecuteApiProducts(Context context) {
            mContext = context;
        }


        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_search_Item2---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                JSONObject jsonObjectMain = new JSONObject(s);
                JSONArray mainArray = jsonObjectMain.getJSONArray("RESULT");
                for (int z = 0; z < mainArray.length(); z++) {
                    JSONObject jobj = mainArray.getJSONObject(z);
                    if (jobj.has("_101")) {
                        if (jobj.getString("_101").equals("010400478")) {
                            JSONArray imgeArray = jobj.getJSONArray("RESULT");
                            itemsData = new HashMap<>();
                            JSONObject imgObj1 = imgeArray.getJSONObject(0);
                            if (imgObj1.has("_120_45")) {
                                for (int j = 0; j < imgeArray.length(); j++) {
                                    JSONObject imgObj = imgeArray.getJSONObject(j);
                                    String categoryName = imgObj.getString("_120_45");
                                    JSONArray pcArr = imgObj.getJSONArray("PC");
                                    ArrayList<SearchBean> urlArr = new ArrayList<>();

                                    for (int k = 0; k < pcArr.length(); k++) {
                                        JSONObject object = pcArr.getJSONObject(k);
                                        SearchBean bean = new SearchBean();
                                        bean.setImageUrls(ATPreferences.readString(mContext, Constants.KEY_IMAGE_URL) + object.getString("_121_170"));
                                        bean.setProductId(object.getString("_114_144"));
                                        bean.setProdcutType(object.getString("_114_112"));
                                        bean.setCategoryName(categoryName);
                                        bean.setIsFavorite(object.getString("_121_80"));
                                        bean.setSellerName(object.getString("_120_83"));
                                        bean.setIsSeen("false");
                                        Log.d("setIsSeens", object.getString("_114_9"));
                                        bean.setActualPrice(object.getString("_114_98"));
                                        bean.setPriceAfterDiscount(object.getString("_122_158"));
                                        bean.setDescription(object.getString("_120_157"));
                                        urlArr.add(bean);
                                    }
                                    itemsData.put(j, urlArr);
                                }
                            }
                        } else if (jobj.getString("_101").equals("010400479")) {
                            JSONArray imgeArray = jobj.getJSONArray("RESULT");
                            // for (int i=0;i<jsonArray1.length();i++){
                            JSONObject jsonObject2 = imgeArray.getJSONObject(0);
                            if (jsonObject2.has("_114_93")) {
                                searchSpecialBean = new Gson().fromJson(jobj.toString(), SearchSpecialBean.class);
                                EventBus.getDefault().post(new Event(Constants.SEARCH_ITEM_SUCCESS_List2, ""));
                            }else {
                                EventBus.getDefault().post(new Event(Constants.SEARCH_ITEM_SUCCESS_List2_Empty, ""));
                            }

                        } else if (jobj.getString("_101").equals("010400676")) {
                            ArrayList<AdsDetailWithMerchant> imageArray = new ArrayList<>();
                            Log.d("beanfavmerchant", jobj.toString());
                            JSONArray imgeArrayMain = jobj.getJSONArray("RESULT");
                            JSONObject jsonObjec = imgeArrayMain.getJSONObject(0);
                            if (jsonObjec.has("_120_83")) {
                                for (int i = 0; i < imgeArrayMain.length(); i++) {
                                    AdsDetailWithMerchant detailsBean = new AdsDetailWithMerchant();
                                    JSONObject jsonObject2 = imgeArrayMain.getJSONObject(i);
                                    String adName = jsonObject2.getString("_120_83");
                                    String videoName = jsonObject2.getString("_121_15");
                                    String id = jsonObject2.getString("_121_18");
                                    String desc = jsonObject2.getString("_120_157");
                                    String seen = jsonObject2.getString("_114_9");
                                    String merchantName = jsonObject2.getString("_114_70");
                                    String merchantId = jsonObject2.getString("_53");
                                    detailsBean.setName(adName);
                                    detailsBean.setId(id);
                                    detailsBean.setMerchantname(merchantName);
                                    detailsBean.setMerchantId(merchantId);
                                    detailsBean.setSeen(seen);
                                    detailsBean.setVideo(videoName);
                                    detailsBean.setDesc(desc);
                                    imageArray.add(detailsBean);
                                }
                                url_maps1 = imageArray;
                                for (int j = 0; j < imgeArrayMain.length(); j++) {
                                    JSONObject imgObj = imgeArrayMain.getJSONObject(j);
                                    String url = ATPreferences.readString(mContext, Constants.KEY_IMAGE_URL) + imgObj.getString("_121_170");
                                    String videoUrl = imgObj.getString("_121_15");
                                    String id = "101";//imgObj.getString("_121_18");
                                    String imageUrl1 = imgObj.getString("_121_170");
                                    String imageName = imgObj.getString("_120_83");
                                    //  String actualPrice = imgObj.getString("_114_98");
                                    //String priceAfterDiscount = imgObj.getString("_122_158");
                                    String seen = "false";//imgObj.getString("_114_9");
                                    String merchantName = imgObj.getString("_114_70");
                                    String merchantId = imgObj.getString("_53");

                                    Log.d("imageUrlq", imageUrl1 + "");

                                    AdsDetailBean adsDetailBean = new AdsDetailBean();
                                    adsDetailBean.setId(id);
                                    adsDetailBean.setImageUrl(imageUrl1);
                                    adsDetailBean.setName(imageName);
                                    adsDetailBean.setSeen(seen);
                                    adsDetailBean.setMerchantName(merchantName);
                                    //adsDetailBean.setActualPrice(actualPrice);
                                    //adsDetailBean.setPriceAfterDiscount(priceAfterDiscount);
                                    arrayAds.add(adsDetailBean);

                                    AdsBean adsBean = new AdsBean();
                                    adsBean.setImageUrl(url);
                                    adsBean.setSeen(seen);
                                    adsBean.setVideoUrl(videoUrl);
                                    adsBean.setArrayList(arrayAds);

                                    url_maps.put(j, adsBean);
                                    ads = url_maps;
                                }
                                EventBus.getDefault().post(new Event(Constants.ADS_LISTING_SUCCESS, ""));
                            }else {
                                EventBus.getDefault().post(new Event(Constants.ADS_LISTING_EMPTY, ""));

                            }
                        }
                    }
                }

            } catch (Exception e) {
                Log.d("exception Here", e.getMessage());
                EventBus.getDefault().post(new Event(-1, ""));
            }
        }
    }


    private class ExecuteSearchShopAsstApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteSearchShopAsstApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_asst_Search---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("RESULT");
                // for (int i=0;i<jsonArray1.length();i++){
                JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                if (jsonObject2.has("_114_93")) {
                    asstSearchBean = new Gson().fromJson(s, ShopAsstSearchBean.class);
                    EventBus.getDefault().post(new Event(Constants.SEARCH_ITEM_SUCCESS, ""));
                }else {
                    EventBus.getDefault().post(new Event(Constants.SEARCH_ITEM_SUCCESS_Empty, ""));
                }
                //}

            } catch (JSONException e) {
                e.printStackTrace();
                EventBus.getDefault().post(new Event(Constants.SEARCH_ITEM_SUCCESS_Empty, ""));
            }


        }
    }

}
