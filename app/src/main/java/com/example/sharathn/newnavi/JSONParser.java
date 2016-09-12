package com.example.sharathn.newnavi;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.Toast;

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JSONParser() {

    }

    public JSONObject getJSONFromUrl(String url, String email) {

        // Making HTTP request
        try {
            // defaultHttpClient
//            DefaultHttpClient httpClient = new DefaultHttpClient();
//            HttpPost httpPost = new HttpPost(url);

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);


            httpget.setHeader("Content-type", "application/json");
//            String email = Utility.getValueFromSP(SearchActivity.this, "email");
            httpget.setHeader("token", email);
//           httpget.setHeader("token", "sharu300@gmail.com");
//            JSONObject jsonObj = new JSONObject();
//            JSONObject location = new JSONObject();
//            JSONObject priceRange= new JSONObject();
//            JSONObject locationInfo = new JSONObject();
//            JSONObject propertyType=new JSONObject();
//
//
//            locationInfo.accumulate("city", "San Jose");
//            locationInfo.accumulate("zip", 95113);
//            jsonObj.accumulate("location",locationInfo);
//
//
//            jsonObj.accumulate("propertyType","");
//
//
//
//
//            JSONObject priceRangeInfo = new JSONObject();
//            priceRangeInfo.accumulate("min", 100);
//            priceRangeInfo.accumulate("max", 6000);
//            jsonObj.accumulate("priceRange",priceRangeInfo);
//
//
////            JSONArray keywords =new JSONArray();
////            keywords.put("Library");
//////            keywords.put("is");
//////            keywords.put("This");
////
////                jsonObj.put("keywords", keywords);
//
//            String json=jsonObj.toString();
//            StringEntity se=new StringEntity(json);
//            httpPost.setEntity(se);
            //httpPost.setHeader("Content-type","application/json");

            System.out.println("----------------------------------before post"+"-------------------------");


            HttpResponse httpResponse = httpClient.execute(httpget);
            int statuscode=httpResponse.getStatusLine().getStatusCode();
            System.out.println("----------------------------------"+statuscode+"-------------------------");
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
//        }catch (JSONException e) {
//            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            System.out.println("----------------------------------before tostring json obj"+"-------------------------");
            json = sb.toString();
            System.out.println(json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String



        return jObj;

    }
}











