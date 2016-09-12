package com.example.sharathn.newnavi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NavigationLandlord extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView list;
    TextView ver;
    TextView name;
    TextView api;
    Button Btngetdata;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

    //URL to get JSON Array
    private static String url = "http://52.39.4.163:3000/api/landlord/getPlaceList";

    //JSON Node Names
    private static final String TAG_OS = "list";
    private static final String TAG_VER = "place";
    private static final String TAG_NAME = "name";
    private static final String TAG_API = "email";

    JSONArray android = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_landlord);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        oslist = new ArrayList<HashMap<String, String>>();


        Btngetdata = (Button) findViewById(R.id.getdata);


     //   new JSONParse().execute();

    }


    @Override

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera)

        {

//            Intent j = new Intent(NavigationLandlord.this, LandlordPost.class);
//
//
//            startActivity(j);

            Intent j = new Intent(NavigationLandlord.this, AddUpdatePlaceActivity.class);
            j.putExtra("update",false);
            startActivity(j);

        } else if (id == R.id.nav_gallery) {


            Intent n = new Intent(NavigationLandlord.this, LandlordSearch.class);

            startActivity(n);


        } else if (id == R.id.nav_slideshow) {

            Intent i = new Intent(NavigationLandlord.this, NavigationTenant.class);

            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ver = (TextView) findViewById(R.id.vers);
            name = (TextView) findViewById(R.id.name);
            api = (TextView) findViewById(R.id.api);
            pDialog = new ProgressDialog(NavigationLandlord.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected JSONObject doInBackground(String... args) {
            String email = Utility.getValueFromSP(NavigationLandlord.this, "email");

            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url, email);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {

            pDialog.dismiss();
            try {
                // Getting JSON Array from URL
                android = json.getJSONArray(TAG_OS);
                for (int i = 0; i < android.length(); i++) {
                    JSONObject c = android.getJSONObject(i);

                    // Storing  JSON item in a Variable
                    String firstname = c.getJSONObject("place").getString("name").toString();
                    String lastname = c.getJSONObject("place").getString("email").toString();
                    String x = "";
                    x += firstname + lastname;


                    HashMap<String, String> map = new HashMap<String, String>();

                    //map.put(TAG_VER, name);
                    map.put(TAG_NAME, firstname);
                    map.put(TAG_API, lastname);

                    oslist.add(map);
                    list = (ListView) findViewById(R.id.list);

                    ListAdapter adapter = new SimpleAdapter(NavigationLandlord.this, oslist,
                            R.layout.tenant_search_list,
                            new String[]{TAG_NAME, TAG_API}, new int[]{
                            R.id.name, R.id.api});

                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener()

                    {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                            Intent j = new Intent(NavigationLandlord.this, LandlordDetailActivity.class);

                            try {
                                System.out.println(android.getJSONObject(position).getJSONObject("place").getInt("place_id"));
                                j.putExtra("id", android.getJSONObject(position).getJSONObject("place").getInt("place_id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            startActivity(j);

                            Toast.makeText(NavigationLandlord.this, "You Clicked at " + oslist.get(+position).get("firstname"), Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}