package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.AdsBean;
import com.apitap.model.bean.AdsDetailBean;
import com.apitap.model.bean.ImagesBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by apple on 10/08/16.
 */
public class HomeManager {

    private static final String TAG = HomeManager.class.getSimpleName();
    public HashMap<Integer, AdsBean> ads = new HashMap<Integer, AdsBean>();
    public ArrayList<String> listAddresses;
    public HashMap<Integer, ArrayList<ImagesBean>> itemsData = new HashMap<Integer, ArrayList<ImagesBean>>();
    public HashMap<Integer, ArrayList<ImagesBean>> specialData = new HashMap<Integer, ArrayList<ImagesBean>>();
    public ArrayList<AdsDetailBean> arrayAds = new ArrayList<AdsDetailBean>();


    public HashMap<Integer, AdsBean> getHome(Context context, String params) {
        new ExecuteApi(context).execute(params);
        return ads;
    }

    private class ExecuteApi extends AsyncTask<String, String, String> {
        Context mContext;
        HashMap<Integer, AdsBean> url_maps = new HashMap<Integer, AdsBean>();


        ExecuteApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_Search_Item---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.v(TAG, s);
            try {
                listAddresses = new ArrayList<>();
                listAddresses.add("Use Current Location");
                JSONObject jsonObject = new JSONObject(s);
                ATPreferences.putString(mContext, Constants.KEY_USER_DEFAULT, jsonObject.has("_192") ? jsonObject.getString("_192") : "");
                JSONArray mainArray = jsonObject.getJSONArray("RESULT");

                for (int i = 0; i < mainArray.length(); i++) {
                    JSONObject jobj = mainArray.getJSONObject(i);
                    if (jobj.has("_101")) {
                        if (jobj.getString("_101").equals("010200517")) {//for ads
                            ATPreferences.putString(mContext, Constants.KEY_USER_PIN, jsonObject.has("_39") ? jsonObject.getString("_39") : "");
                            JSONArray imgeArray = jobj.getJSONArray("RESULT");
                            if (imgeArray.length()>0){
                                for (int j = 0; j < imgeArray.length(); j++) {
                                JSONObject imgObj = imgeArray.getJSONObject(j);
                                String url = ATPreferences.readString(mContext, Constants.KEY_IMAGE_URL) + imgObj.getString("_121_170");
                                String videoUrl = imgObj.getString("_121_15");
                                String isSeen = imgObj.getString("_114_9");
                                String merchantName = imgObj.getString("_114_70");
                                String merchantLogo = imgObj.getString("_121_77");
                                String id = imgObj.getString("_121_18");
                                if (videoUrl.equals("")) {
                                    JSONArray array = imgObj.getJSONArray("IR");
                                    if (array.length() > 0) {
                                        for (int k = 0; k < array.length(); k++) {
                                            JSONObject object = array.getJSONObject(k);
                                            String imageUrl = object.getString("_121_170");

                                            String imageName = object.getString("_120_83");

                                            String actualPrice = object.getString("_114_98");
                                            String priceAfterDiscount = object.getString("_122_158");

                                            Log.d("IsSEENAD",isSeen+"");
                                            Log.d("imageUrlq",imageUrl+"");

                                            AdsDetailBean adsDetailBean = new AdsDetailBean();
                                            adsDetailBean.setId(id);
                                            adsDetailBean.setImageUrl(imageUrl);
                                            adsDetailBean.setName(imageName);
                                            adsDetailBean.setSeen(isSeen);
                                            adsDetailBean.setMerchantName(merchantName);
                                            adsDetailBean.setActualPrice(actualPrice);
                                            adsDetailBean.setPriceAfterDiscount(priceAfterDiscount);
                                            arrayAds.add(adsDetailBean);

                                        }
                                    }
                                }

                                AdsBean adsBean = new AdsBean();
                                adsBean.setImageUrl(url);
                                adsBean.setVideoUrl(videoUrl);
                                adsBean.setSeen(isSeen);
                                adsBean.setMerchantName(merchantName);
                                adsBean.setMerchantLogo(merchantLogo);
                                adsBean.setArrayList(arrayAds);

                                url_maps.put(j, adsBean);
                                ads = url_maps;
                            }}else{
                                ads.clear();
                                AdsBean bean = new AdsBean();
                                AdsDetailBean beans = new AdsDetailBean();
                            }
                            Log.e("size of arrayads", arrayAds.size() + "");
                            ATPreferences.putString(mContext, Constants.KEY_STOP, "true");
                        } else if (jobj.getString("_101").equals("010400478")) { //for items
                            JSONArray imgeArray = jobj.getJSONArray("RESULT");
                            Log.d("ImageArrayLeng",imgeArray.length()+"");
                            if (imgeArray.length()>0){
                                itemsData = new HashMap<>();
                            for (int j = 0; j < imgeArray.length(); j++) {
                                JSONObject imgObj = imgeArray.getJSONObject(j);
                                String categoryName = imgObj.getString("_120_45");
                                String Businesstype = imgObj.getString("_120_83");
                                JSONArray pcArr = imgObj.getJSONArray("PC");
                                ArrayList<ImagesBean> urlArr = new ArrayList<>();

                                for (int k = 0; k < pcArr.length(); k++) {
                                    JSONObject object = pcArr.getJSONObject(k);
                                    ImagesBean bean = new ImagesBean();
                                    bean.setImageUrls(ATPreferences.readString(mContext, Constants.KEY_IMAGE_URL) + object.getString("_121_170"));
                                    bean.setProductId(object.getString("_114_144"));
                                    bean.setProdcutType(object.getString("_114_112"));
                                    bean.setCategoryName(categoryName);
                                    bean.setBusiness_type(Businesstype);
                                    bean.setIsFavorite(object.getString("_121_80"));
                                    bean.setSellerName(object.getString("_120_83"));
                                    bean.setIsSeen(object.getString("_114_9"));
                                    bean.setActualPrice(object.getString("_114_98"));
                                    bean.setPriceAfterDiscount(object.getString("_122_158"));
                                    bean.setDescription(object.getString("_120_157"));
                                    Log.d("seeprices", Utils.hexToASCII(object.getString("_120_83"))+"  "+object.getString("_114_98")+"  "+object.getString("_122_158"));

                                    urlArr.add(bean);
                                }
                                itemsData.put(j, urlArr);
                            }
                            }else{
                                itemsData.clear();
                                ImagesBean bean = new ImagesBean();
                            }
                        } else if (jobj.getString("_101").equals("010400479")) { //for specials
                            JSONArray imgeArray = jobj.getJSONArray("RESULT");
                            if (imgeArray.length()>0){
                            specialData = new HashMap<>();
                            for (int j = 0; j < imgeArray.length(); j++) {
                                JSONObject imgObj = imgeArray.getJSONObject(j);
                                String categoryName = imgObj.getString("_120_45");
                                String businessType = imgObj.getString("_120_83");
                                JSONArray pcArr = imgObj.getJSONArray("PC");
                                ArrayList<ImagesBean> urlArr = new ArrayList<>();
                                for (int k = 0; k < pcArr.length(); k++) {
                                    JSONObject object = pcArr.getJSONObject(k);
                                    ImagesBean bean = new ImagesBean();
                                    bean.setImageUrls(ATPreferences.readString(mContext, Constants.KEY_IMAGE_URL) + object.getString("_121_170"));
                                    bean.setProductId(object.getString("_114_144"));
                                    bean.setProdcutType(object.getString("_114_112"));
                                    bean.setCategoryName(categoryName);
                                    bean.setIsFavorite(object.getString("_121_80"));
                                    bean.setSellerName(object.getString("_120_83"));
                                    Log.d("sellerNames",object.getString("_120_83"));
                                    bean.setIsSeen(object.getString("_114_9"));
                                    bean.setBusiness_type(businessType);
                                    Log.d("setIsSeenspe", object.getString("_114_9"));
                                    bean.setActualPrice(object.getString("_114_98"));
                                    bean.setPriceAfterDiscount(object.getString("_122_162"));
                                    bean.setDescription(object.getString("_120_157"));
                                    urlArr.add(bean);
                                    Log.d("SpecialsMange", object + "");
                                }

                                specialData.put(j, urlArr);
                            }}else {
                                specialData.clear();
                                ImagesBean bean = new ImagesBean();
                            }
                        }
                        else if (jobj.getString("_101").equals("010100055")){

                            JSONArray imgeArray = jobj.getJSONArray("RESULT");
                            for (int j = 0; j < imgeArray.length(); j++) {
                                JSONObject resObj = imgeArray.getJSONObject(j);

                                JSONArray addressArray = resObj.getJSONArray("AD");
                                for(int k=0; k < addressArray.length(); k++) {
                                    JSONObject addressObj = addressArray.getJSONObject(k);
                                    String addresses = addressObj.getString("_114_53");
                                    Log.e("Addresses: ", ""+addresses);
                                    if (!addresses.isEmpty())
                                        listAddresses.add(addresses);
                                }

                            }
                            EventBus.getDefault().post(new Event(Constants.ADDRESS_SUCCESS, ""));

                        }

                    }
                }

                EventBus.getDefault().post(new Event(Constants.ALL_IMAGES_SUCCESS, ""));
            } catch (JSONException e) {
                e.printStackTrace();
                EventBus.getDefault().post(new Event(Constants.GET_SERVER_ERROR, ""));
            }
        }
    }
}
