package com.example.sharathn.newnavi;

/**
 * Created by SHARATH N on 5/15/2016.
 */

import android.widget.EditText;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class AddUpdatePlaceActivity extends AppCompatActivity {

    boolean isUpdate = false;
    int placeId;
    String placeJsonString;
    JSONObject placeJsonObject;

    Spinner propertyTypeSpinner;
    LayoutInflater layoutInflater;
    EditText placeNameEditText, streetEditText, cityEditText, stateEditText, zipEditText, priceEditText, roomsEditText, bathroomsEditText, areaEditText, phoneEditText, emailEditText, descriptionEditText;
    String placeName, street, city, state, zip, propertyType, phone, email, description;
    int rooms, bathrooms, area, price;
    String addorupdateUrl;
    String debugTag = "AddUpdatePlaceActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_place);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        //get the intent
        Intent intent = getIntent();

        //get the data associated with intent
        isUpdate = intent.getBooleanExtra("update", false);

        if (isUpdate) {
            placeId = intent.getIntExtra("placeid", -1);

            if (placeId == -1) {
                throw new RuntimeException("Please pass placeID");
            } else {
                getSupportActionBar().setTitle("Update Place");
                placeJsonString = intent.getStringExtra("result");
            }
        } else {
            getSupportActionBar().setTitle("Add Place");
        }


        //set spinner configuration (propertyTypeSpinner)

        //Get property type spinner and set adapter
        String[] spinnerItems = new String[3];
        spinnerItems[0] = "Condo";
        spinnerItems[1] = "Apartment";
        spinnerItems[2] = "Villa";

        propertyTypeSpinner = (Spinner) findViewById(R.id.propertyTypeSpinner);
        propertyTypeSpinner.setPrompt("Condo");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_item_custom, spinnerItems);

        propertyTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView selectedItem = (TextView) view;
                propertyType = selectedItem.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        propertyTypeSpinner.setAdapter(adapter);


        //get other UI elements
        placeNameEditText = (EditText) findViewById(R.id.placeNameEditText);
        streetEditText = (EditText) findViewById(R.id.streetEditText);
        cityEditText = (EditText) findViewById(R.id.cityEditText);
        stateEditText = (EditText) findViewById(R.id.stateEditText);
        zipEditText = (EditText) findViewById(R.id.zipEditText);
        priceEditText = (EditText) findViewById(R.id.priceEditText);
        roomsEditText = (EditText) findViewById(R.id.roomsEditText);
        bathroomsEditText = (EditText) findViewById(R.id.bathroomsEditText);
        areaEditText = (EditText) findViewById(R.id.areaEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);


        //if update, populate edit text with fetched values values

        if (isUpdate) {
            try {
                placeJsonObject = new JSONObject(placeJsonString);

                street = placeJsonObject.getJSONObject("place").getJSONObject("address").getString("street-level");
                city = placeJsonObject.getJSONObject("place").getJSONObject("address").getString("city-name");
                state = placeJsonObject.getJSONObject("place").getJSONObject("address").getString("state");
                zip = placeJsonObject.getJSONObject("place").getJSONObject("address").getString("zip-code");
                placeName = placeJsonObject.getJSONObject("place").getString("name");
                rooms = placeJsonObject.getJSONObject("place").getInt("rooms");
                bathrooms = placeJsonObject.getJSONObject("place").getInt("bathrooms");
                area = placeJsonObject.getJSONObject("place").getInt("area");
                price = placeJsonObject.getJSONObject("place").getInt("price");
                phone = placeJsonObject.getJSONObject("place").getString("phone");
                email = placeJsonObject.getJSONObject("place").getString("email");
                description = placeJsonObject.getJSONObject("place").getString("description");
                //propertyType = placeJsonObject.getJSONObject("place").getString("name");
                JSONArray picUrlArray = placeJsonObject.getJSONObject("place").getJSONArray("imageurllist");


                //after getting all the values, update the UI
                //first update all edit texts and spinner, then pictures

                streetEditText.setText(street);
                cityEditText.setText(city);
                stateEditText.setText(state);
                zipEditText.setText(zip);
                placeNameEditText.setText(placeName);
                roomsEditText.setText(String.valueOf(rooms));
                bathroomsEditText.setText(String.valueOf(bathrooms));
                areaEditText.setText(String.valueOf(area));
                priceEditText.setText(String.valueOf(price));
                phoneEditText.setText(phone);
                emailEditText.setText(email);
                descriptionEditText.setText(description);
                propertyTypeSpinner.setPrompt(propertyType);


            } catch (JSONException e) {
                Log.e(debugTag, "Json string parse error");
                e.printStackTrace();
            }

        }
    }

    /*
        Method to store values from UI to variables.
     */
    private void storeValues() {

        //store values
        placeName = placeNameEditText.getText().toString();
        street = streetEditText.getText().toString();
        city = cityEditText.getText().toString();
        state = stateEditText.getText().toString();
        zip = zipEditText.getText().toString();
        if(priceEditText.getText().toString().isEmpty()) {
            price = 0;
        }
        else {
            price = Integer.parseInt(priceEditText.getText().toString());
        }
        if(roomsEditText.getText().toString().isEmpty()) {
            rooms = 0;
        }
        else {
            rooms = Integer.parseInt(roomsEditText.getText().toString());
        }
        if(bathroomsEditText.getText().toString().isEmpty()) {
            bathrooms = 0;
        }
        else {
            bathrooms = Integer.parseInt(bathroomsEditText.getText().toString());
        }
        if(areaEditText.getText().toString().isEmpty()) {
            area = 0;
        }
        else {
            area = Integer.parseInt(areaEditText.getText().toString());
        }
        phone = phoneEditText.getText().toString();
        email = emailEditText.getText().toString();
        description = descriptionEditText.getText().toString();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_update_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.task_done:
                // will add place or update place
                processRequest();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void processRequest() {

        // set latest test values, for spinner and pictureList, they will
        //already have latest values

        if (isUpdate) {
            addorupdateUrl = "http://52.39.4.163:3000/api/landlord/updatePlace/" + placeId;

            //fetch latest values from different edit texts and update
            storeValues();

            UpdatePlaceAsyncTask updatePlaceAsyncTask = new UpdatePlaceAsyncTask();
            updatePlaceAsyncTask.execute();

        } else {
            addorupdateUrl = "http://52.39.4.163:3000/api/landlord/addPlace";

            //fetch latest values from different edit texts and update
            storeValues();
            if((placeName.isEmpty()) || (street.isEmpty()) || (city.isEmpty()) || (state.isEmpty()) || (zip.isEmpty()) || (price==0) || (rooms==0) || (bathrooms==0) || (area==0)) {
                Toast.makeText(this,"Invalid Input, please enter all inputs to proceed", Toast.LENGTH_LONG).show();
//                System.out.println("Executing async task-----------" + "empty string is not allowed");
            }
            else {
                AddPlaceAsyncTask addPlaceAsyncTask = new AddPlaceAsyncTask();
                addPlaceAsyncTask.execute();
            }

        }
    }


    /*
        Async task for adding a place
     */

    private class AddPlaceAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {


            Log.i(debugTag, "Adding a place");


            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject place = new JSONObject();
                JSONObject address = new JSONObject();
                JSONArray imageUrlArray = new JSONArray();

                address.accumulate("street", street);
                address.accumulate("city", city);
                address.accumulate("state", state);
                address.accumulate("zip", zip);
                place.accumulate("address", address);

                place.accumulate("rooms", rooms);
                place.accumulate("bathrooms", bathrooms);
                place.accumulate("area", area);
                place.accumulate("price", price);
                place.accumulate("phone", phone);
                place.accumulate("name", placeName);
                place.accumulate("email", email);
                place.accumulate("description", description);
                place.accumulate("propertytype", propertyType);


                place.accumulate("imageurllist", imageUrlArray);
                jsonObject.accumulate("place", place);
                String email = Utility.getValueFromSP(AddUpdatePlaceActivity.this, "email");
//                String email = "sharu300@gmail.com";
                Log.i(debugTag, "EMail : " + email);


                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();

                // 2. make POST request to the given URL
                HttpPost httpPost = new HttpPost(addorupdateUrl);

                StringEntity se = new StringEntity(jsonObject.toString());

                httpPost.setEntity(se);
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("token", email);

                HttpResponse httpResponse = httpclient.execute(httpPost);
                InputStream inputStream = httpResponse.getEntity().getContent();
                JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();

                    if (name.equals("result")) {
                        String result = reader.nextString();

                        if (result.equals("true")) {
                            //go to the next activity
                            Intent intent = new Intent(AddUpdatePlaceActivity.this, LandlordSearch.class);
                            startActivity(intent);
                        } else {

                            Log.e(debugTag, "result" + result);

                            // unable to add place
                            Log.e(debugTag, "Unable to add a place");
                        }
                    } else {
                        Log.e(debugTag, "Something wrong in Add Async task execute method");
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(debugTag, "Error while creating Json");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.e(debugTag, "Error while creating string entity from json");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(debugTag, "IO Exception");
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    /*
        Async Task to update place
     */
    private class UpdatePlaceAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject place = new JSONObject();
                JSONObject address = new JSONObject();
                JSONArray imageUrlArray = new JSONArray();

                address.accumulate("street", street);
                address.accumulate("city", city);
                address.accumulate("state", state);
                address.accumulate("zip", zip);
                place.accumulate("address", address);

                place.accumulate("rooms", rooms);
                place.accumulate("bathrooms", bathrooms);
                place.accumulate("area", area);
                place.accumulate("price", price);
                place.accumulate("phone", phone);
                place.accumulate("name", placeName);
                place.accumulate("email", email);
                place.accumulate("description", description);
                place.accumulate("propertytype", propertyType);


                place.accumulate("imageurllist", imageUrlArray);
                jsonObject.accumulate("place", place);
                String email = Utility.getValueFromSP(AddUpdatePlaceActivity.this, "email");
                //String email = "sharu300@gmail.com";


                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();

                // 2. make PUT request to the given URL

                HttpPut httpPut = new HttpPut(addorupdateUrl);

                StringEntity se = new StringEntity(jsonObject.toString());

                httpPut.setEntity(se);
                httpPut.setHeader("Content-type", "application/json");
                httpPut.setHeader("token", email);

                HttpResponse httpResponse = httpclient.execute(httpPut);
                InputStream inputStream = httpResponse.getEntity().getContent();
                JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();

                    if (name.equals("result")) {
                        String result = reader.nextString();

                        if (result.equals("true")) {
                            //go to the next activity
                            Intent intent = new Intent(AddUpdatePlaceActivity.this, LandlordSearch.class);
                            startActivity(intent);
                        } else {
                            // unable to add place
                            Log.e(debugTag, "Unable to update a place");
                        }
                    } else {
                        Log.e(debugTag, "Something wrong in Update Async task execute method");
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(debugTag, "Error while creating Json");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.e(debugTag, "Error while creating string entity from json");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(debugTag, "IO Exception");
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


}



