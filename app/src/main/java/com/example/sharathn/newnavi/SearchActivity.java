package com.example.sharathn.newnavi;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class SearchActivity extends NavigationTenant {
    ListView list;
    TextView ver;
    TextView name;
    TextView api;
    Button Btngetdata;
    TextView phonenumber;
    TextView descript;
    TextView pric;

    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    //URL to get JSON Array
    private static String url = "http://52.39.4.163:3000/api/tenant/getAllfavouritePlace";
    //JSON Node Names
    private static final String TAG_OS = "list";
    private static final String TAG_VER = "place";
    private static final String TAG_NAME = "name";
    private static final String TAG_API = "email";
    private static final String TAG_PHONENO = "phone";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_PRICE = "price";
    JSONArray android = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        oslist = new ArrayList<HashMap<String, String>>();

        new JSONParse().execute();
    }


    private class JSONParse extends AsyncTask<String, String, JSONObject>

    {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ver = (TextView) findViewById(R.id.vers);
            name = (TextView) findViewById(R.id.name);
            api = (TextView) findViewById(R.id.api);
            phonenumber=(TextView) findViewById(R.id.phone);
            descript=(TextView) findViewById(R.id.desc);
            pric=(TextView) findViewById(R.id.price);
            pDialog = new ProgressDialog(SearchActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected JSONObject doInBackground(String... args)
        {
            String email = Utility.getValueFromSP(SearchActivity.this, "email");
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url, email);
            return json;
        }


        @Override
        protected void onPostExecute(JSONObject json)
        {
            pDialog.dismiss();
            try {
                // Getting JSON Array from URL
                android = json.getJSONArray(TAG_OS);
                for(int i = 0; i < android.length(); i++)
                {
                    JSONObject c = android.getJSONObject(i);


                    // Storing  JSON item in a Variable
                    String firstname = c.getJSONObject("place").getString("name").toString();
                    String lastname = c.getJSONObject("place").getString("email").toString();
                    String price=c.getJSONObject("place").getString("price").toString();
                    String descr=c.getJSONObject("place").getString("description").toString();
                    String ph=c.getJSONObject("place").getString("phone").toString();





                    // Adding value HashMap key => value


                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_NAME, firstname);
                    map.put(TAG_API, lastname);
                    map.put(TAG_PRICE, price);
                    map.put(TAG_PHONENO, ph);

                    oslist.add(map);
                    list=(ListView)findViewById(R.id.list);
                    ListAdapter adapter = new SimpleAdapter(SearchActivity.this, oslist,
                            R.layout.tenant_search_list,
                            new String[] { TAG_NAME, TAG_API }, new int[] {
                            R.id.name, R.id.api});
                    list.setAdapter(adapter);


//landlorddetailactivity replaced by tenantdetailactivity

                    list.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id)
                        {
                            Intent j = new Intent(SearchActivity.this, TenantFavouriteDetailActivity.class);
                            try {
                                System.out.println(android.getJSONObject(position).getJSONObject("place").getInt("place_id"));
                                j.putExtra("id",android.getJSONObject(position).getJSONObject("place").getInt("place_id"));
                            }

                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                            startActivity(j);
                            Toast.makeText(SearchActivity.this, "You Clicked at "+oslist.get(+position).get("firstname"), Toast.LENGTH_SHORT).show();
                        }
                    });



                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}
