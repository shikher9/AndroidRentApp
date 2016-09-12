package com.example.sharathn.newnavi;

/**
 * Created by SHARATH N on 5/11/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class LandlordPost extends NavigationLandlord implements View.OnClickListener {

    private static int RESULT_LOAD_IMAGE = 1;
    Button newsearch;
    EditText tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11, tv12, tv13;
    String email = Utility.getValueFromSP(LandlordPost.this, "email");

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landlord_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Spinner spinner;
        spinner = (Spinner) findViewById(R.id.spinner);
        java.util.ArrayList<String> strings = new java.util.ArrayList<>();
        strings.add("Mobile");
        strings.add("Home");
        strings.add("Work");

        newsearch = (Button) findViewById(R.id.newsearch);

        tv1 = (EditText) findViewById(R.id.editText1);

        tv2 = (EditText) findViewById(R.id.editText2);

        tv3 = (EditText) findViewById(R.id.editText3);

        tv4 = (EditText) findViewById(R.id.editText4);

        tv5 = (EditText) findViewById(R.id.editText5);

        tv6 = (EditText) findViewById(R.id.editText6);

        tv7 = (EditText) findViewById(R.id.editText7);

        tv8 = (EditText) findViewById(R.id.editText8);

        tv9 = (EditText) findViewById(R.id.editText9);

        tv10 = (EditText) findViewById(R.id.editText10);

        tv11 = (EditText) findViewById(R.id.editText11);

        tv12 = (EditText) findViewById(R.id.editText12);

        tv13 = (EditText) findViewById(R.id.editText13);

        newsearch = (Button) findViewById(R.id.newsearch);


        newsearch.setOnClickListener(this);
//        ImageView login;
//        login = (ImageView) findViewById(R.id.camera);
//        login.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//
//
//                Intent i = new Intent(
//                        Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                startActivityForResult(i, RESULT_LOAD_IMAGE);
//            }
//        });
    }


    public String POST(String url, Landlord landlord) {
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            JSONObject jsonObject = new JSONObject();
            JSONObject place = new JSONObject();
            JSONObject address = new JSONObject();

            address.accumulate("street", landlord.getStreet());
            address.accumulate("city", landlord.getCity());
            address.accumulate("state", landlord.getState());
            address.accumulate("zip", landlord.getZipcode());
            place.accumulate("address", address);
            place.accumulate("rooms", landlord.getRooms());
            place.accumulate("bathrooms", landlord.getBathrooms());
            place.accumulate("area", landlord.getSqft());
            place.accumulate("price", landlord.getRent());
            place.accumulate("phone", landlord.getContact());
            place.accumulate("name", landlord.getAddress());
            place.accumulate("email", landlord.getEmail());
            place.accumulate("description", landlord.getDescription());

            jsonObject.accumulate("place", place);

            json = jsonObject.toString();

            System.out.println("############################## $#######################");

            System.out.println(jsonObject);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("token", email);

            HttpResponse httpResponse = httpclient.execute(httpPost);

            inputStream = httpResponse.getEntity().getContent();

            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;


    }


    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    @Override
    public void onClick(View v) {


        String address = tv1.getText().toString();
        String street = tv2.getText().toString();
        String city = tv3.getText().toString();
        String state = tv4.getText().toString();
        String zipcode = tv5.getText().toString();
        String rooms = tv6.getText().toString();
        String bathrooms = tv7.getText().toString();
        String sqft = tv8.getText().toString();
        String rent = tv9.getText().toString();
        String contact = tv10.getText().toString();
        String email = tv11.getText().toString();
        String description = tv12.getText().toString();
        String deposit = tv13.getText().toString();


        switch (v.getId()) {
            case R.id.newsearch:
                // call AsynTask to perform network operation on separate thread
                new HttpAsyncTask().execute("http://52.39.4.163:3000/api/landlord/addPlace");
                break;
        }

    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override

        protected String doInBackground(String... urls) {

            Landlord landlord = new Landlord();

            landlord.setAddress(tv1.getText().toString());
            landlord.setStreet(tv2.getText().toString());
            landlord.setCity(tv3.getText().toString());
            landlord.setState(tv4.getText().toString());
            landlord.setZipcode(tv5.getText().toString());
            landlord.setRooms(tv6.getText().toString());
            landlord.setBathrooms(tv7.getText().toString());
            landlord.setSqft(tv8.getText().toString());
            landlord.setRent(tv9.getText().toString());
            landlord.setContact(tv10.getText().toString());
            landlord.setEmail(tv11.getText().toString());
            landlord.setDescription(tv12.getText().toString());
            landlord.setDeposit(tv13.getText().toString());
            return POST(urls[0], landlord);

        }

        protected void onPostExecute(JSONObject jsonObject) {
            //System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%########"+json);

            try {
                Toast.makeText(LandlordPost.this, "successfully posted", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}









