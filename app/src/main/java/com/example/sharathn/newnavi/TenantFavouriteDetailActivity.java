package com.example.sharathn.newnavi;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SHARATH N on 5/15/2016.
 */




import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SHARATH N on 5/12/2016.
 */
public class TenantFavouriteDetailActivity extends AppCompatActivity {

    ListView list;
    TextView ver;
    TextView name;
    TextView api;
    ImageButton Btngetdata;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

    String placejsonstring;
    JSONArray android = null;


    //URL to get JSON Arr
    private static String url = "";

    //JSON Node Names
    private static final String TAG_OS = "list";
    private static final String TAG_VER = "place";
    private static final String TAG_NAME = "name";
    private static final String TAG_API = "email";
    public int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenant_detail_activity);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        Toast.makeText(this, Integer.toString(id), Toast.LENGTH_LONG).show();
        System.out.println(id);

        url = "http://52.39.4.163:3000/api/landlord/getPlace/" + id;


        oslist = new ArrayList<HashMap<String, String>>();

//
        new JSONParse().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tenant_favourite_detail_activitty, menu);
        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.tenantDetailActivityFavouriteMenu:

                item.setChecked(!item.isChecked());
                item.setIcon(item.isChecked() ? R.mipmap.ic_action_action_favorite_outline: R.drawable.newheart);

                new JSONParseDel().execute();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public static String GET() {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL


            HttpGet httpget = new HttpGet(url);

            System.out.println("############################## $#######################");


            httpget.setHeader("Content-type", "application/json");

            httpget.setHeader("token", "sharu300@gmail.com");

            HttpResponse httpResponse = httpclient.execute(httpget);

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


    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ver = (TextView) findViewById(R.id.vers);
            name = (TextView) findViewById(R.id.name);
            api = (TextView) findViewById(R.id.api);
            pDialog = new ProgressDialog(TenantFavouriteDetailActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected JSONObject doInBackground(String... args) {
            String email = Utility.getValueFromSP(TenantFavouriteDetailActivity.this, "email");
            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url, email);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            //System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%########"+json);

            placejsonstring = json.toString();


            pDialog.dismiss();
            try {
                TextView tv = (TextView) findViewById(R.id.placeNameTextView);
                System.out.println(json.getJSONObject("place").getString("name"));
                tv.setText(json.getJSONObject("place").getString("name"));


                TextView tv1 = (TextView) findViewById(R.id.countTextView);
                tv1.setText(json.getJSONObject("place").getString("page_visits_count"));


                TextView tv2 = (TextView) findViewById(R.id.roomsTextView);
                tv2.setText(json.getJSONObject("place").getString("rooms"));

                TextView tv3 = (TextView) findViewById(R.id.streetTextView);
                tv3.setText(json.getJSONObject("place").getJSONObject("address").getString("street-level"));


                TextView tv4 = (TextView) findViewById(R.id.cityTextView);
                tv4.setText(json.getJSONObject("place").getJSONObject("address").getString("city-name"));


                TextView tv5 = (TextView) findViewById(R.id.stateTextView);
                tv5.setText(json.getJSONObject("place").getJSONObject("address").getString("state"));


                TextView tv6 = (TextView) findViewById(R.id.bathroomsTextView);
                tv6.setText(json.getJSONObject("place").getString("bathrooms"));

                TextView tv7 = (TextView) findViewById(R.id.descriptionTextView);
                tv7.setText(json.getJSONObject("place").getString("description"));

                TextView tv8 = (TextView) findViewById(R.id.priceTextView);
                tv8.setText(json.getJSONObject("place").getString("price") + "$");

                TextView tv9 = (TextView) findViewById(R.id.phoneTextView);
                tv9.setText(json.getJSONObject("place").getString("phone"));

                TextView tv10 = (TextView) findViewById(R.id.zipTextView);
                tv10.setText(json.getJSONObject("place").getJSONObject("address").getString("zip-code"));

                TextView t11 = (TextView) findViewById(R.id.emailTextView);
                t11.setText(json.getJSONObject("place").getString("email"));

                TextView t12 = (TextView) findViewById(R.id.propertyTypeTextView);
                t12.setText(json.getJSONObject("place").getString("propertytype"));


            } catch (JSONException e) {
                System.out.println("_++++++++++++++++++inside catch block of detailactivity================");
                e.printStackTrace();
            }


        }

    }


    public void addtofav(View view) {


//        String url = "http://52.39.4.163:3000/api/tenant/favouritePlace/" + id;
        new JSONParse1().execute();


    }


    private class JSONParse1 extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ver = (TextView) findViewById(R.id.vers);
            name = (TextView) findViewById(R.id.name);
            api = (TextView) findViewById(R.id.api);
            pDialog = new ProgressDialog(TenantFavouriteDetailActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected JSONObject doInBackground(String... args) {
            String email = Utility.getValueFromSP(TenantFavouriteDetailActivity.this, "email");
            JSONPostParser jPParser = new JSONPostParser();
            System.out.print("email from favourite------" +email);
            String url = "http://52.39.4.163:3000/api/tenant/favouritePlace/" + id;
            // Getting JSON from URL
            JSONObject jsonObj = new JSONObject();
            String jsonBody = jsonObj.toString();

            JSONObject json = jPParser.getJSONFromUrl(url, jsonBody, email);

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                Toast.makeText(TenantFavouriteDetailActivity.this, "added to favourite", Toast.LENGTH_LONG).show();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private class JSONParseDel extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ver = (TextView) findViewById(R.id.vers);
            name = (TextView) findViewById(R.id.name);
            api = (TextView) findViewById(R.id.api);
            pDialog = new ProgressDialog(TenantFavouriteDetailActivity.this);
            pDialog.setMessage("Deleting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected JSONObject doInBackground(String... args) {
            String email = Utility.getValueFromSP(TenantFavouriteDetailActivity.this, "email");
            JSONDeleteParser jPParser = new JSONDeleteParser();
            String url = "http://52.39.4.163:3000/api/tenant/favouritePlace/" + id;
            // Getting JSON from URL
            JSONObject json = jPParser.getJSONFromUrl(url, email);
            //Intent intent=new Intent(TenantFavouriteDetailActivity.this,)
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                Toast.makeText(TenantFavouriteDetailActivity.this, "deleted from favourite", Toast.LENGTH_LONG).show();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
















