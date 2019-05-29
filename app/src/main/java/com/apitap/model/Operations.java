package com.apitap.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import com.apitap.BuildConfig;
import com.apitap.model.bean.DtoDefaultValues;
import com.apitap.model.bean.ShoppingCompBean;
import com.apitap.model.preferences.ATPreferences;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

@SuppressLint("NewApi")
public class Operations {

    private static String TAG = Operations.class.getSimpleName();
    private static Map<String, Object> map;
    private static Map<String, Object> map2;

    public static String makeJsonDefaultParams(Activity context) {
        DtoDefaultValues.setParam192("8f09eaddb545ff7c94b3c7106eede715");
        String dataPlana = "{\"101\":\"010100027\",\"PARAM\":{\"127.14\":\"MOBILE\"}}";
        String parametersToCall = "{\"192\":\"" + Constants.KEY_DEFAULT
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

//        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
//                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
//                + "en" + "\",\"57\":\""
//                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
//                + "0.0" + "\",\"120.39\":\""
//                + "0.0" + "\",\"OPTLST\":["
//                + dataPlana + "]}";


        Log.d(TAG, "parameters_default_api---" + parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonValidateUser(Activity context, String email) {
        String dataPlana = "{\"101\":\"050400009\",\"PARAM\":{\"114.7\":\""
                + email + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        Log.v(TAG, "parameters_signup---" + parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonCounter(Activity context, String prodctId) {
        String dataPlana = "{\"101\":\"020400598\",\"PARAM\":{\"114.144\":\""
                + prodctId + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        Log.v(TAG, "parameters_signup---" + parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonUserSignup(Activity context, String email, String fName, String lName, String gender) {
        String dataPlana = "{\"101\":\"030300120\",\"PARAM\":{\"114.7\":\""
                + email + "\",\"114.3\":\""
                + fName + "\",\"114.5\":\""
                + lName + "\",\"114.11\":\""
                + gender + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        Log.v(TAG, "parameters_signup---" + parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonUserLogin(Activity context, String email, String password) {
        String dataPlana = "{\"101\":\"050300010\",\"PARAM\":{\"114.7\":\""
                + email + "\",\"52\":\""
                + Utils.convertStringToHex(password) + "\",\"57\":\"" + Utils.getDeviceId(context) + "\"}}";


        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";
        Log.d("ResponseLoginParams", parametersToCall);
        return parametersToCall;
    }


    public static String makeJsonUserForgotPassword(Activity context, String email) {
        String dataPlana = "{\"101\":\"020300279\",\"PARAM\":{\"114.7\":\""
                + email + "\"}}";


        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }


    public static String makeJsonUserGuestLogin(Activity context) {
        String dataPlana = "{\"101\":\"010100517\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\"}}";


        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }


    public static String makeJsonGetAdFavourites(Activity context) {
        String dataPlana = "{\"101\":\"010100496\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\"}}";


        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }


    public static String makeJsonSearchStore(Activity context) {
        String dataPlana = "{\"101\":\"010400645\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.127\":\"" + "\"}}";


        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }


    public static String makeJsonGetItems(Activity context, String productId, String consumerId, String itemOrSpecial) {
        String dataPlana = "{\"101\":\"010100008\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + consumerId + "\" ,\"114.144\":\"" + productId + "\"}}";
        String dataPlana1 = "{\"101\":\"030400471\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + consumerId + "\" ,\"114.144\":\"" + productId + "\"}}";
        String dataPlana2 = "{\"101\":\"020400598\",\"PARAM\":{\"114.144\":\""
                + productId + "\"}}";
        String dataPlana3 = "{\"101\":\"010100012\",\"PARAM\":{\"53\":\""
                + consumerId + "\" ,\"114.144\":\"" + productId + "\"}}";
        String dataPlana4 = "{\"101\":\"010400599\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + consumerId + "\" ,\"114.144\":\"" + productId + "\"}}";
        String dataPlan5 = "{\"101\":\"010400221\",\"PARAM\":{\"53\":\"" +
                consumerId + "\" ,\"127.10\":\"001\",\"114.112\":\"" + itemOrSpecial + "\",\"114.127\":\"\"}}";


        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "," + dataPlana1 + "," + dataPlana2 + "," + dataPlana3 + "," + dataPlana4 + "," + dataPlan5 + "]}";

        Log.d(TAG, "parameters_makeJsonGetItems" + parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonGetRelatedItems(Activity context, String productId) {
        String dataPlana = "{\"101\":\"010100019\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.144\":\"" + productId + "\"}}";

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";
        Log.d("makeJsonGetRelatedItems", parametersToCall + "");
        return parametersToCall;
    }

    public static String makeJsonSpecialItemapplied(Activity context, String productId) {
        String dataPlana = "{\"101\":\"010100488\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.144\":\"" + productId + "\"}}";

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }

    public static String makeSaveCart(Activity context, String cartId) {
        String dataPlana = "{\"101\":\"020400199\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"121.30\":\"" + cartId + "\"}}";

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }


    public static String makeJsonSpecialItemrequired(Activity context, String productId) {
        String dataPlana = "{\"101\":\"010100489\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.144\":\"" + productId + "\"}}";
        String dataPlana1 = "{\"101\":\"010100488\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.144\":\"" + productId + "\"}}";

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                //      + dataPlana +","+ dataPlana1+ "]}";
                + dataPlana1 + "]}";

        Log.d("SpecialRelated", parametersToCall + "");
        return parametersToCall;
    }


    public static String makeJsonGetItemsByCategoryViews(Activity context, String productId) {
        String dataPlana = "{\"101\":\"010400599\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.144\":\"" + productId + "\"}}";

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }

    public static String makeJsonAdwatched(Activity context, String adId) {
        String dataPlana = "{\"101\":\"030400639\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"121.18\":\"" + adId + "\"}}";

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }


    public static String makeSearchAds(Activity context, String searchAd) {
        String dataPlana = "{\"101\":\"010400676\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.127\":\"" + searchAd + "\"}}";

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }


    public static String makeJsonGetAds(Activity context, String MerchantId, String Sort) {
        //For Ads
        String special_sort = Sort;
        if (Sort.equals(Constants.PriceHightoLow) || Sort.equals(Constants.LowtoPriceHigh)) {
            special_sort = Constants.Alphabetical;
        }
        JSONObject obj1 = null;
        try {

            obj1 = new JSONObject();
            obj1.put("101", "010200517");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));

            obj1.put("EXPECTED", "ALL");
            if (!MerchantId.isEmpty()) {
                JSONArray arr1 = new JSONArray();
                JSONObject obj2 = new JSONObject();
                obj2.put("114.179", MerchantId);
                obj2.put("operator", "eq");
                arr1.put(obj2);
                obj1.put("PARAM", obj_param);
                obj1.put("FILTER", arr1);
            } else {
                obj1.put("PARAM", obj_param);
            }


        } catch (Exception e) {

        }

        String dataPlana = obj1.toString();/*"{\"101\":\"010100517\"," + "\"EXPECTED\":\"ALL\","+"\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\",\"114.179\":\"" + MerchantId  +"\"}}";*/

        //For Items
        String dataPlanItem = "{\"101\":\"010400478\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"127.60\":\"" + Sort + "\" ,\"121.141\":\"" + Client.getDateTimeStamp() + "\",\"127.89\":\"" + Client.getWeekDay() + "\",\"114.179\":\"" + MerchantId + "\"}}";
        //For Specials
        String dataPlanSpecial = "{\"101\":\"010400479\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"127.60\":\"" + special_sort + "\" ,\"121.141\":\"" + Client.getDateTimeStamp() + "\",\"127.89\":\"" + Client.getWeekDay() + "\",\"114.179\":\"" + MerchantId + "\"}}";

        String dataPlana2 = "{\"101\":\"010100055\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\"}}";

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "," + dataPlanItem + "," + dataPlana2 + "," + dataPlanSpecial + "]}";
        Log.d("AdssData", parametersToCall + "");
        return parametersToCall;
    }

    public static String makeJsonGetBusinessDetails(Activity context, String merchantId) {
        String dataPlana = "{\"101\":\"010100020\",\"PARAM\":{\"53\":\""
                + merchantId + "\"}}";
        return dataPlana;
    }

    /*public static String makeJsonGetItems(Activity context, String productId, String productType) {
        String dataPlana = "{\"101\":\"010100008\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + "00011010000000000004" + "\" ,\"114.144\":\"" + productId + "\" ,\"114.112\":\"" + productType + "\"}}";


        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + *//*Util.GetDeviceId()*//*Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }*/


    public static String makeJsonGet(Activity context) {
        String dataPlana = "{\"101\":\"010100003\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\"}}";


        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }

    public static String makeJsonAddToFavorite(Activity context, String productId) {
        String dataPlana = "{\"101\":\"030400218\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.144\":\"" + productId + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";
        Log.d("parameters_add_fav", parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonAdToFavorite(Activity context, String productId) {
        String dataPlana = "{\"101\":\"030400497\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"121.18\":\"" + productId + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";
        Log.d("AdToFavorite__params", parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonMerchantAddToFavorite(Activity context, String MerchantId) {
        String dataPlana = "{\"101\":\"030400095\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"127.56\":\"" + MerchantId + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }

    public static String makeJsonGetOptions(Activity context, String productId) {
        String dataPlana = "{\"101\":\"010100012\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.144\":\"" + Utils.getElevenDigitId(productId) + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";
        return parametersToCall;
    }

    public static String makeJsonGetOptions2(Activity context, String optionId) {
        String dataPlana = "{\"101\":\"010100013\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"122.111\":\"" + optionId + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        Log.d("paramsGetOptions2", parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonGetFavouriteMerchant(Activity context) {
        String dataPlana = "{\"101\":\"010100303\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }

    public static String makeJsonGetShoppingCart(Activity context) {
        String dataPlana = "{\"101\":\"010100200\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        Log.d("getCartOp", parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetShoppingCartItem(Activity context, ShoppingCompBean shopping, String ifDeleted) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100201");
            JSONObject obj_param = new JSONObject();
            if (!ifDeleted.isEmpty())
                obj_param.put("122.31", ifDeleted);
            else
                obj_param.put("122.31", shopping.getShoppingCartId());
            obj1.put("PARAM", obj_param);

            JSONArray arr1 = new JSONArray();
            JSONObject obj2 = new JSONObject();

            obj2.put("114.143", shopping.getShoppingCartStatus());
            obj2.put("operator", "eq");
            arr1.put(obj2);
            obj1.put("FILTER", arr1);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_shoppingcartdetail_api---" + parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonPayShoppingCart(Activity context, String merchant_Id, String shopping_id, String amount,
                                                 String gratuity, String shipping_iD, String delivery_Id, String card_token,
                                                 String card_amout, String tax, ShoppingCompBean shoppingCompBean, String choiceId,
                                                 String choicePrice, JSONArray jsonArray, String specialInstructions) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONArray arrkt = new JSONArray();
            JSONArray arram = new JSONArray();
            JSONArray arrtx = new JSONArray();
            JSONArray arrchoice = new JSONArray();
            JSONObject obj1 = new JSONObject();
            JSONObject objkt = new JSONObject();
            JSONObject objam = new JSONObject();
            JSONObject objtx = new JSONObject();
            JSONObject objchoice = new JSONObject();
            obj1.put("101", "030300023");
            JSONObject obj_param = new JSONObject();

            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.179", merchant_Id);
            obj_param.put("122.29", Utils.getElevenDigitId(shoppingCompBean.getShoppingCartId()));
            obj_param.put("13", Client.getTime());
            obj_param.put("55.3", amount);
            obj_param.put("120.109", gratuity);
            obj_param.put("114.115", shipping_iD);
            obj_param.put("122.109", delivery_Id);
            obj_param.put("121.55", specialInstructions);
            obj_param.put("120.38", "0.0");
            obj_param.put("120.39", "0.0");


            objkt.put("48.15", card_token);
            arrkt.put(objkt);
            obj_param.put("KT", arrkt);

            objam.put("48.30", card_amout);
            arram.put(objam);
            obj_param.put("AM", arram);

            objtx.put("121.97", tax);
            arrtx.put(objtx);
            obj_param.put("TX", arrtx);

            objchoice.put("121.30", Utils.getElevenDigitId(shoppingCompBean.getShoppingCartId()));
            objchoice.put("121.104", choiceId);
            objchoice.put("114.98", choicePrice);
            objchoice.put("114.144", "00000000003");
            arrchoice.put(objchoice);
            obj_param.put("CH", jsonArray);


            obj1.put("PARAM", obj_param);

         /*   JSONArray arr1 = new JSONArray();
            JSONObject obj2 = new JSONObject();
            obj2.put("114.143", shopping.getShoppingCartStatus());
            obj2.put("operator", "eq");
            arr1.put(obj2);
            obj1.put("FILTER", arr1);*/

            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "payShopping_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetFavourite(Activity context, String word) {

        JSONObject objMain = null;
        JSONObject objMain2 = null;

        try {
            objMain2 = new JSONObject();
            objMain2.put("101", "010400681");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            objMain2.put("PARAM", obj_param);
        } catch (Exception w) {
        }

        try {
            objMain = new JSONObject();
            objMain.put("101", "010100303");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            objMain.put("PARAM", obj_param);

        } catch (Exception w) {

        }
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400221");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("127.10", "001");
            obj_param.put("114.112", "21");
            obj_param.put("114.127", word);
            obj1.put("PARAM", obj_param);
            JSONObject obj2 = new JSONObject();
            obj2.put("101", "010400221");
            JSONObject obj_param2 = new JSONObject();
            obj_param2.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param2.put("127.10", "001");
            obj_param2.put("114.112", "23");
            obj_param2.put("114.127", word);
            obj2.put("PARAM", obj_param2);

            arr.put(obj2);
            arr.put(obj1);
            arr.put(objMain);
            arr.put(objMain2);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_favourite_api---" + parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonGetMerchantFavourite(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400094");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("127.10", "001");
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_favourite_api---" + parametersToCall);

        return parametersToCall;
    }
    public static String makeJsonRemoveMerchantFavourite(Activity context,String merchantID) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "040400096");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("127.56",merchantID );
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_remove_favourite_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String sendMessage(Activity context, String merchantId, String type, String subject, String message, String invoiceId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030300192");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.179", merchantId);
            obj_param.put("120.16", type);
            obj_param.put("122.128", subject);
            obj_param.put("120.157", message);
            obj_param.put("121.75", invoiceId);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_sendmsg_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String sendMessageProduct(Activity context, String productId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030300192");
            JSONObject obj_param = new JSONObject();
//            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.144", productId);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_sendproductmsg_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String getMessageProduct(Activity context, String productId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400265");
            JSONObject obj_param = new JSONObject();
            obj_param.put("114.144", productId);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getproductmsg_api---" + parametersToCall);

        return parametersToCall;
    }

 public static String addshoppingList(Activity context, String itemName) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030400427");
            JSONObject obj_param = new JSONObject();
            obj_param.put("120.157", itemName);
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "addshoppingList---" + parametersToCall);

        return parametersToCall;
    }

    public static String editshoppingList(Activity context, String itemName, String List_Id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "020400428");
            JSONObject obj_param = new JSONObject();
            obj_param.put("120.157", itemName);
            obj_param.put("122.31", List_Id);
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "editshoppingList---" + parametersToCall);

        return parametersToCall;
    }
    public static String deleteShoppingList(Activity context,  String List_Id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "020400429");
            JSONObject obj_param = new JSONObject();
            obj_param.put("122.31", List_Id);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "deleteShoppingList---" + parametersToCall);

        return parametersToCall;
    }
    public static String getShoppingAssistantList(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100430");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "getShoppingList---" + parametersToCall);

        return parametersToCall;
    }

    public static String addItemToShoppingList(Activity context,String Shopping_Id,String product_name,String item_id,boolean b) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030400413");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("122.31", Shopping_Id);
            obj_param.put("120.83",  product_name);
            if (b)
                obj_param.put("122.17",  Utils.getElevenDigitId(item_id));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "addItemToShoppingList---" + parametersToCall);

        return parametersToCall;
    }
    public static String removeItemToShoppingList(Activity context,String Shopping_Id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "020400414");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("122.17", Utils.getElevenDigitId(Shopping_Id));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "removeItemToShoppingList---" + parametersToCall);

        return parametersToCall;
    }


    public static String sendMessageInvoice(Activity context, String invoiceId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100209");
            JSONObject obj_param = new JSONObject();
//            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("121.75", invoiceId);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getproductmsg_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String sendMessageReply(Activity context, String parentId, String msgId, String merchantId,
                                          String type, String subject, String message, String invoiceId, String Id, String productId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030300192");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.179", merchantId);


            obj_param.put("122.128", subject);
            obj_param.put("120.157", message);
            if (msgId.equals("2"))
                obj_param.put("114.144", productId);
            else {
                obj_param.put("121.75", invoiceId);
            }
            if (msgId.equals("1") || msgId.equals("2")) {
                obj_param.put("114.9", "false");
                obj_param.put("120.16", "92");
                if (parentId.equals("0"))
                    obj_param.put("114.150", Utils.getElevenDigitId(Id));
                else
                    obj_param.put("114.150", Utils.getElevenDigitId(parentId));

            } else {
                obj_param.put("114.150", Utils.getElevenDigitId(parentId));
                obj_param.put("120.16", type);
            }
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_replymsg_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeProductSeen(Activity context, String productId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030400471");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.144", productId);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_seen_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonRemoveFavourite(Activity context, String id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "040400219");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.144", id);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_remove_favourite_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetAddress(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100055");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_address_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonAddAddress(boolean isNew, Activity context, String type, String line1, String line2,
                                            String mob, String country, String state, String city, String pin, String id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            if (isNew)
                obj1.put("101", "030400056");
            else
                obj1.put("101", "020400133");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.53", type);
            obj_param.put("114.12", line1);
            obj_param.put("114.13", line2);
            obj_param.put("114.18", mob);
            obj_param.put("122.87", country);
            obj_param.put("120.13", state);
            obj_param.put("114.14", city);
            obj_param.put("122.107", pin);
            if (isNew) {
                obj_param.put("120.38", "");
                obj_param.put("120.39", "");
                obj_param.put("121.45", "00000000000");
            } else {
                obj_param.put("114.115", id);
            }
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_add_address_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetCountry(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100232");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getcountry_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetFCM(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030400008");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("57", Utils.getDeviceId(context));
            obj_param.put("122.12", FirebaseInstanceId.getInstance().getToken());
            obj_param.put("48.6", Utils.getDeviceName());
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_fcm_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetState(Activity context, String countrycode) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100233");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("122.87", countrycode);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getstate_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetCity(Activity context, String statecode) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100234");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("120.13", statecode);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getcity_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetPINCode(Activity context, String citycode) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100235");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.14", citycode);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getpincode_api---" + parametersToCall);


        return parametersToCall;
    }

    public static String makeJsonGetMerchantDetail(Activity context, String id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", ATPreferences.readString(context, Constants.KEY_USER_DEFAULT));
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            JSONObject obj_param = new JSONObject();
            JSONObject object2= new JSONObject();
            JSONObject obj_param2 = new JSONObject();

            object2.put("101", "030400683");
            obj_param2.put("114.179", id);
            obj_param2.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            object2.put("PARAM", obj_param2);

            obj1.put("101", "010100020");
            obj_param.put("53", id);
            obj_param.put("114.179", ATPreferences.readString(context, Constants.KEY_USERID));
            obj1.put("EXPECTED", "114.9");
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            arr.put(object2);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getmerchantdetail_api---" + parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonGetMerchantDelivery(Activity context, String id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", ATPreferences.readString(context, Constants.KEY_USER_DEFAULT));
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100015");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", id);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getmerchantdelivery_api---" + parametersToCall);

        return parametersToCall;
    }


    public static String GetMerchantRating(Activity context, String id, String MerchantId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            JSONObject obj_param = new JSONObject();
            obj1.put("101", "010100104");
            JSONArray arr1 = new JSONArray();
            JSONObject obj2 = new JSONObject();
            obj2.put("53", MerchantId);
            obj2.put("operator", "eq");
            arr1.put(obj2);
            obj1.put("FILTER", arr1);
            obj_param.put("127.57", id);
            obj1.put("EXPECTED", "ALL");
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getmerchantrating_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String AddMerchantRatingFilter(Activity context, String id, String MerchantId, String Rating, String desc) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            JSONObject obj_param = new JSONObject();
            obj1.put("101", "030400103");
            JSONArray arr1 = new JSONArray();
            JSONObject obj2 = new JSONObject();
            obj2.put("53", MerchantId);
            obj2.put("operator", "eq");
            arr1.put(obj2);
            obj1.put("FILTER", arr1);
            obj_param.put("127.57", id);
            obj_param.put("122.129", Rating);
            obj_param.put("120.83", desc);
            obj_param.put("120.157", desc);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_addmerchantrating_api---" + parametersToCall);

        return parametersToCall;
    }


    public static String AddMerchantRating(Activity context, String id, String MerchantId, String Rating, String desc) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030400103");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", id);
            obj_param.put("127.57", MerchantId);
            obj_param.put("122.129", Rating);
            obj_param.put("120.83", desc);
            obj_param.put("120.157", desc);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_addmerchantrating_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetMerchantLocation(Activity context, String id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100368");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", id);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getmerchantlocation_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetMerchantDistance(Activity context, String id, String lat, String longitude) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400516");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", id);
            obj_param.put("120.38", lat);
            obj_param.put("120.39", longitude);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getmerchantlocation_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetWriteReview(Activity context, String merchant_id, String title, String desc, String rating) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100368");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("127.57", merchant_id);
            obj_param.put("120.83", title);
            obj_param.put("120.157", desc);
            obj_param.put("122.129", rating);

            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getmerchantlocation_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetInvoiceHistory(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100206");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));

            obj1.put("PARAM", obj_param);
            obj1.put("EXPECTED", "ALL");
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_GetInvoiceHistory_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetDeleteCard(Activity context, String cardId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030400046");
            JSONObject obj_param = new JSONObject();
            obj_param.put("122.34", "");

            obj1.put("PARAM", obj_param);
            obj1.put("EXPECTED", "ALL");
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_GetInvoiceHistory_api---" + parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonGetInvoiceDetail(Activity context, String invoiceId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100114");
            JSONObject obj_param = new JSONObject();
            obj_param.put("121.75", invoiceId);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_GetInvoiceDetail_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonAllMessages(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100209");
            JSONObject obj_param = new JSONObject();
            obj_param.put("114.9", "true");
            obj1.put("PARAM", obj_param);

            JSONArray arr1 = new JSONArray();
            JSONObject obj2 = new JSONObject();
            obj2.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj2.put("operator", "eq");
            JSONObject obj3 = new JSONObject();
            // obj3.put("120.16", "92");
            obj3.put("operator", "eq");
            arr1.put(obj2);
            arr1.put(obj3);
            obj1.put("FILTER", arr1);
            obj1.put("EXPECTED", "ALL");

            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_all_message_api---" + parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonGetMessagesInvoice(Activity context, String mmessageId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100209");
            JSONObject obj_param = new JSONObject();
            obj_param.put("114.9", "false");
            obj1.put("PARAM", obj_param);

            JSONArray arr1 = new JSONArray();
            JSONObject obj2 = new JSONObject();
            obj2.put("121.75", mmessageId);
            obj2.put("operator", "eq");
            arr1.put(obj2);
            obj1.put("FILTER", arr1);
            obj1.put("EXPECTED", "ALL");

            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_all_message_api---" + parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonGetMessagesDetail(Activity context, String mmessageId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100209");
            JSONObject obj_param = new JSONObject();
            obj_param.put("114.9", "false");
            obj1.put("PARAM", obj_param);

            JSONArray arr1 = new JSONArray();
            JSONObject obj2 = new JSONObject();
            obj2.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj2.put("operator", "eq");
            JSONObject obj3 = new JSONObject();
            obj3.put("120.16", "92");
            obj3.put("operator", "eq");
            JSONObject obj4 = new JSONObject();
            obj4.put("114.150", mmessageId);
            obj4.put("operator", "eq");
            arr1.put(obj2);
            arr1.put(obj3);
            arr1.put(obj4);
            obj1.put("FILTER", arr1);
            obj1.put("EXPECTED", "ALL");

            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_all_message_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetAdsListing(Activity context, String merchantId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010200517");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));

            obj1.put("EXPECTED", "ALL");
            if (!merchantId.isEmpty()) {
                JSONArray arr1 = new JSONArray();
                JSONObject obj2 = new JSONObject();
                obj2.put("114.179", merchantId);
                obj2.put("operator", "eq");
                arr1.put(obj2);
                obj1.put("FILTER", arr1);
            }
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_GetAds_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonReorder(Activity context, String process_status, String invoiceId, String refund_status_Id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030400628");
            JSONObject obj_param = new JSONObject();
            //obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("122.31", invoiceId);
            /*obj_param.put("121.75", invoiceId);
            obj_param.put("121.10", refund_status_Id);
            obj_param.put("124.118",process_status);
            obj_param.put("114.146", Client.getDateTimeStamp());*/
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_makeJsonreturnOrder---" + parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonreturnOrder(Activity context, String process_status, String invoiceId, String refund_status_Id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "020300332");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("121.75", invoiceId);
            obj_param.put("121.10", refund_status_Id);
            obj_param.put("124.118", process_status);
            obj_param.put("114.146", Client.getDateTimeStamp());
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_makeJsonreturnOrder---" + parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonGetStoresWithItems(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100557");

            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.179", ATPreferences.readString(context, Constants.KEY_USERID));

            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getallstores---" + parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonAddCreditCard(Activity context, String type_id, String number, String name, String holderName, String cvv,
                                               String date_expires, String address_id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030400044");

            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("2", number);
            obj_param.put("48.6", name);
            obj_param.put("48.2", holderName);
            obj_param.put("48.4", cvv);
            obj_param.put("118.5", date_expires);
            obj_param.put("114.115", address_id);
            obj_param.put("120.7", type_id);
            obj_param.put("121.70", "00000035001");

            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getaddcards---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonSearchItem(Activity context, String address) {
        String dataPlana = "{\"101\":\"010400472\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.127\":\"" + address + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }

    public static String makeJsonAdsByBusiness(Activity context) {
        String dataPlana2 = "{\"101\":\"010400677\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.127\":\"" + "" + "\"}}";

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana2 + "]}";

        return parametersToCall;
    }

    public static String makeJsonSearchProduct(Activity context, String key) {
        String dataPlana = "{\"101\":\"010400478\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.127\":\"" + key + "\" ,\"121.141\":\"" + Client.getDateTimeStamp() + "\",\"127.89\":\"" + Client.getWeekDay() + "\"}}";

        String dataPlana1 = "{\"101\":\"010400479\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.127\":\"" + key + "\" ,\"121.141\":\"" + Client.getDateTimeStamp() + "\",\"127.89\":\"" + Client.getWeekDay() + "\"}}";

        String dataPlana2 = "{\"101\":\"010400676\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.127\":\"" + key + "\"}}";


        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "," + dataPlana1 + "," + dataPlana2 + "]}";

        Log.d("makeJsonSearchProduct", parametersToCall);

        return parametersToCall;
    }
    public static String makeJsonSearchForShopAsst(Activity context, String key) {
        String dataPlana = "{\"101\":\"010400478\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.127\":\"" + key + "\" ,\"121.141\":\"" + Client.getDateTimeStamp() + "\",\"127.89\":\"" + Client.getWeekDay() + "\"}}";

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        Log.d("makeJsonSearchProduct", parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonSearchAds(Activity context, String ads) {
        String dataPlana = "{\"101\":\"010400549\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.127\":\"" + ads + "\" ,\"121.141\":\"" + Client.getDateTimeStamp() + "\",\"127.89\":\"" + Client.getWeekDay() + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        Log.d("makeJsonSearchProduct", parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonSearchSpecial(Activity context, String key) {
        String dataPlana = "{\"101\":\"010400479\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.127\":\"" + key + "\" ,\"121.141\":\"" + Client.getDateTimeStamp() + "\",\"127.89\":\"" + Client.getWeekDay() + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        Log.d("makeJsonSearchProduct", parametersToCall);

        return parametersToCall;
    }


    public static String getJsonCard(Activity context) {
        String dataPlana = "{\"101\":\"010100017\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }


    public static String makeJsonSearchNearAddress(Activity context) {
        String dataPlana = "{\"101\":\"010100055\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }

    public static String makeJsonSearchFavorites(Activity context, String keyword) {
        String dataPlana = "{\"101\":\"010400221\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"127.10\":\"" + "001" + "\" ,\"114.112\":\"" + "21" + "\" ,\"114.127\":\"" + keyword + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";
        return parametersToCall;
    }

//    public static String makeJsonAddToCartItems(Activity context, String quantity, String productId, String merchantID, String Option_Id,String Option_Id2) {
//        String dataPlana = "{\"101\":\"030400198\",\"PARAM\":{\"53\":\""
//                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.121\":\""
//                + quantity + "\",\"114.144\":\""
//                + productId + "\" ,\"114.179\":\""
//                + merchantID + "\" ,\"121.104\":\""
//                + Option_Id + "\"}}";
//        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
//                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
//                + "en" + "\",\"57\":\""
//                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
//                + "0.0" + "\",\"120.39\":\""
//                + "0.0" + "\",\"OPTLST\":["
//                + dataPlana + "]}";
//        Log.d("ParamsAddtocart", parametersToCall);
//        return parametersToCall;
//    }

    public static String makeJsonAddToCartItems(Activity context, String quantity, String productId, String merchantID, String Option_Id, String Option_Id2) {
        String ChoiceArray = "";
        if (!Option_Id2.isEmpty()) {
            ChoiceArray = "{\"121.104\":\"" + Option_Id + "\"},{\"121.104\":\"" + Option_Id2 + "\"}";
        } else {
            ChoiceArray = "{\"121.104\":\"" + Option_Id + "\"}";
        }

        String dataPlana = "{\"101\":\"030400198\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.121\":\""
                + quantity + "\",\"114.144\":\""
                + productId + "\" ,\"114.179\":\""
                + merchantID + "\" ,\"CH\":["
                + ChoiceArray + "]}}";

        // ,"CH":[{"121.104":"00000000030"},{"121.104":"00000000031"}]}}]}
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";
        Log.d("ParamsAddtocart", parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonSearchNearBy(Activity context, String keyword, String lat, String longs) {
        String dataPlana = "{\"101\":\"010400228\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"127.10\":\"" + "001" + "\" ,\"120.38\":\"" + lat + "\" ,\"120.39\":\"" + longs + "\" ,\"114.127\":\"" + keyword + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + lat + "\",\"120.39\":\""
                + longs + "\",\"OPTLST\":["
                + dataPlana + "]}";
        return parametersToCall;
    }

    public static String makeJsonDeleteItemFromCart(Activity context, String id) {
        String dataPlana = "{\"101\":\"020400203\",\"PARAM\":{\"121.30\":\""
                + Utils.getElevenDigitId(id) + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";
        Log.d("removeApi", parametersToCall + "");
        return parametersToCall;
    }

    public static String makeJsonDeleteStoreFromCart(Activity context, String id) {
        String dataPlana = "{\"101\":\"020400202\",\"PARAM\":{\"122.31\":\""
                + Utils.getElevenDigitId(id) + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";
        return parametersToCall;
    }

    public static String makeJsonGetInvoiceItems(Activity context, String id) {
        String dataPlana = "{\"101\":\"010100114\",\"PARAM\":{\"121.75\":\""
                + id + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";
        return parametersToCall;
    }
//
//   public static String makeJsonReturnItems(Activity context, String invoiceId ) {
//        String dataPlana = "{\"101\":\"020300332\",\"PARAM\":{\"53\":\""
//                + "00011010000000000002" + "\" ,\"121.75\":\"" + invoiceId + "\"}}";
//        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
//                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
//                + "en" + "\",\"57\":\""
//                + //*Util.GetDeviceId()*//*Utils.getDeviceId(context) + "\",\"120.38\":\""
//                + "0.0" + "\",\"120.39\":\""
//                + "0.0" + "\",\"OPTLST\":["
//                + dataPlana + "]}";
//
//        return parametersToCall;
//    }


    public static String makePaymentJson(Activity context) {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("addressId=804").append("&cardNumber=5454545454545454").append("&cardType=62").append("&cvv=233").append("&expireMonth=12").append("&expireYear=2016").append("&firstName=aaaa").append("&lastName=aaaaaa").append("&nickname=aaaaaa").append("&nmcId=196");

            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


}