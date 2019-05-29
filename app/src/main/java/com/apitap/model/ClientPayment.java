package com.apitap.model;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Base64;

import com.apitap.model.bean.DtoDefaultValues;
import com.apitap.model.customclasses.Formatter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

@SuppressLint("NewApi")
public class ClientPayment {
//        public static final String BASE_URL =
//            "https://10.0.2.4:4143/ServerApi/ApiService/CreateToken/";// stage

    public static final String BASE_URL =
            "https://209.46.35.217:4143/ServerApi/ApiService/CreateToken?";// prod
//    public static final String BASE_URL =
//            "https://66.179.57.58:8081/NmcServerS/nmc-server/post/";// production

    public static String Caller(String parametersToCall) {
        String result = "";

        if (DtoDefaultValues.getType().equals("HTTP")) {
            result = CallerHttp(parametersToCall);
        } else if (DtoDefaultValues.getType().equals("HTTPS")) {
//			result = ConnectionHttps.doPost("http://66.179.57.58:8081/NmcServerS/nmc-server/post/"
//					, parametersToCall);
            result = ConnectionHttpsPayment.doGet(BASE_URL,parametersToCall);
        //    result = ConnectionHttpsPayment.doPost(BASE_URL, parametersToCall);
        }
        return (result);
    }

    public static String CallerHttp(String parametersToCall) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String json = "";
        try {
            String url = BASE_URL;
            //String url="https://100.43.205.74:8095/NmcServerS/nmc-server/post/";
//        	String url = "http://" + ip + ":" + port
//        			+ "/NmcServerS/nmc-server/post";//esto no deberia estar quemado!!!!!!!!!!!!!!!!!!!
            String encodedText = new String(Base64.encodeToString(parametersToCall.getBytes(Charset.forName("UTF-8")), Base64.DEFAULT));
            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            int timeoutConnection = 300000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            // in milliseconds which is the timeout for waiting for data.
            int timeoutSocket = 300000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(url);
            //set data to StringEntity
            StringEntity se = new StringEntity(encodedText);
            //set httpPost Entity
            httpPost.setEntity(se);
            //Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "text/plain");
            httpPost.setHeader("Content-type", "text/plain");
            //Execute POST request to the given URL
            HttpResponse response = httpclient.execute(httpPost);
            //receive response as inputStream
            json = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            json = "Error: " + writer.toString();
        }

        return json;
    }

    private static void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }
        } };

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map<String, Object> ProcessResult(String json) {
        JSONObject obj;
        Map<String, Object> map = new HashMap();
        try {
            obj = new JSONObject(json);
            Iterator keys = obj.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                map.put(key, obj.get(key));
            }
        } catch (JSONException e) {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
        }
        return (map);
    }

    public static String getParam192() {
        return (java.util.UUID.randomUUID().toString().substring(0, 32));
    }

    public static String getTimeStamp() {
        Date date = new Date();
        return (Formatter.formatNewTimeStamp(date));
    }

    public static String getDate() {
        Date date = new Date();
        return (Formatter.formatDate(date));
    }

    public static String getTime() {
        Date date = new Date();
        return (Formatter.formatTime(date));
    }
}