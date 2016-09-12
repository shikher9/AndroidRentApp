package com.example.sharathn.newnavi;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
import java.util.ArrayList;
import java.util.HashMap;


import android.app.Activity;
import android.widget.Toast;

public class FinalTenantSearch extends NavigationTenant {


    private static final String TAG_OS = "list";
    private static final String TAG_VER = "place";
    private static final String TAG_NAME = "name";
    private static final String TAG_API = "email";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_PRICE = "price";
    private static final String TAG_PHONENO = "phone";
    private static final String TAG_DESC = "description";
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

    Button newsearch;
    public int flag;
    EditText edit1, edit2, edit3, edit4, edit5, edit6;
    ListView list;
    public String url = "";
    public String email = "Sharu300@gmail.com";

    //String email = Utility.getValueFromSP(FinalTenantSearch.this, "email");
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_tenant_search);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        java.util.ArrayList<String> strings = new java.util.ArrayList<>();
        strings.add("Mobile");
        strings.add("Home");
        strings.add("Work");

        edit1 = (EditText) findViewById(R.id.keyword);
        edit2 = (EditText) findViewById(R.id.city);
        edit3 = (EditText) findViewById(R.id.minprice);
        edit4 = (EditText) findViewById(R.id.maxprice);
        edit5 = (EditText) findViewById(R.id.zip);
        edit6 = (EditText) findViewById(R.id.edit6);
        edit2.setText("San Jose");
        newsearch = (Button) findViewById(R.id.searchtenant);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public String POST(String url, Tenant tenant) {
        System.out.println("inside post");
        InputStream inputStream = null;
        String result = "";
        try {


            HttpClient httpclient = new DefaultHttpClient();


            HttpPost httpPost = new HttpPost(url);

            String json = "";

            JSONObject jsonObject = new JSONObject();

            JSONObject priceRange = new JSONObject();

            JSONObject location = new JSONObject();

            location.accumulate("city", tenant.getCity());

            location.accumulate("zip", tenant.getZipcode());

            jsonObject.accumulate("propertyType", tenant.getPropertytype());

            Log.e("res", "min max" + tenant.getMin() + " : " + tenant.getMax());
            priceRange.accumulate("min", tenant.getMin());
            priceRange.accumulate("max", tenant.getMax());
            jsonObject.accumulate("location", location);
            jsonObject.accumulate("priceRange", priceRange);
            json = jsonObject.toString();
            System.out.println("############################## $#######################");
            System.out.println(jsonObject);
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("token", email);
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();


            if (inputStream != null) {
                System.out.println("Printing the result");
                result = convertInputStreamToString(inputStream);
                Log.e("res", result);
            } else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {

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


    public void onClick(View v) {
        if ((edit2.getText().toString().equals(""))) {

            String keyword = edit1.getText().toString();


            String city = edit2.getText().toString();

            if (edit2.getText().toString().length() == 0)
                edit2.setError("City name is required");

            String zipcode = edit5.getText().toString();

            String max = edit4.getText().toString();

            String min = edit3.getText().toString();

            String prop = edit6.getText().toString();

        }
        else {
            new HttpAsyncTask().execute("http://52.39.4.163:3000/api/tenant/search");
        }

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override

        protected String doInBackground(String... urls) {


            Tenant tenant = new Tenant();
            tenant.setCity(edit2.getText().toString());
            tenant.setDescription(edit1.getText().toString());
            Log.e("res", "min :" + edit3.getText().toString());
            tenant.setMin(edit3.getText().toString());
            Log.e("res", "max :" + edit4.getText().toString());
            tenant.setMax(edit4.getText().toString());
            tenant.setZipcode(edit5.getText().toString());
            tenant.setPropertytype(edit6.getText().toString());

            return POST(urls[0], tenant);

        }

        protected void onPostExecute(String result) {
            try {

                Intent intent = new Intent(FinalTenantSearch.this, TenantDisplayActivity.class);
                intent.putExtra("result", result);
                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}

