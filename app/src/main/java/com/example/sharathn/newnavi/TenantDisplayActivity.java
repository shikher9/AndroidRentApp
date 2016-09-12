package com.example.sharathn.newnavi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SHARATH N on 5/14/2016.
 */
public class TenantDisplayActivity extends FinalTenantSearch {
    private static final String TAG_OS = "list";
    private static final String TAG_VER = "place";
    private static final String TAG_NAME = "name";
    private static final String TAG_API = "email";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_PRICE = "price";
    private static final String TAG_PHONENO = "phone";
    private static final String TAG_DESC = "description";

    JSONArray android = null;
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    ListView list;
    TextView ver;
    TextView name;
    TextView api;

    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_favourite);
        Intent intent = getIntent();
        String result = intent.getStringExtra("result");
        System.out.println("INSIDE ADD FAV ACTIVITY<<<<INTENT IS PASSSED" + result);
        //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        oslist = new ArrayList<HashMap<String, String>>();

        JSONObject resjsonObj = null;
        try {
            resjsonObj = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        new JSONParse().execute();
        try {
            // Getting JSON Array from URL
            android = resjsonObj.getJSONArray(TAG_OS);
            for (int i = 0; i < android.length(); i++) {
                JSONObject c = android.getJSONObject(i);

                // Storing  JSON item in a Variable
                String name = c.getJSONObject("place").getString("name").toString();
                String email = c.getJSONObject("place").getString("email").toString();
//                String price=c.getJSONObject("place").getString("price").toString();
//                String des=c.getJSONObject("place").getString("des").toString();
//                String phon=c.getJSONObject("place").getString("phone").toString();
                String x = "";
                //  x+=firstname+lastname;

                HashMap<String, String> map = new HashMap<String, String>();

                map.put(TAG_VER, name);
                map.put(TAG_NAME, name);
                map.put(TAG_API, email);
//                map.put(TAG_PRICE,price);
//                map.put(TAG_PHONENO,phon);
//                map.put(TAG_DESC,des);

                oslist.add(map);
                list = (ListView) findViewById(R.id.listview);

                ListAdapter adapter = new SimpleAdapter(TenantDisplayActivity.this, oslist,
                        R.layout.tenant_search_list,
                        new String[]{TAG_NAME, TAG_API}, new int[]{
                        R.id.name, R.id.api});


                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener()

                {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        Intent j = new Intent(TenantDisplayActivity.this, TenantDetailActivity.class);

                        try {
                            System.out.println(android.getJSONObject(position).getJSONObject("place").getInt("place_id"));
                            j.putExtra("id", android.getJSONObject(position).getJSONObject("place").getInt("place_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(j);

                        Toast.makeText(TenantDisplayActivity.this, "You Clicked at " + oslist.get(+position).get("firstname"), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}