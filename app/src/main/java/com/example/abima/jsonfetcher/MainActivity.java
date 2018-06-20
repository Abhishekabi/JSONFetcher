package com.example.abima.jsonfetcher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String myURL = "http://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b1b15e88fa797225412429c1c50c122a1";
        Button button = (Button) findViewById(R.id.button);
        final TextView temp = (TextView) findViewById(R.id.temperature);
        final TextView desc = (TextView) findViewById(R.id.description);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    //To get the temperature
                                    String jsonObj = response.getString("main");
                                    JSONObject jo = new JSONObject(jsonObj);
                                    double temperature = Double.parseDouble(jo.getString("temp")) - 273.15;
                                    temp.setText(String.format("%.2f", temperature) + " deg celcious");

                                    //To get the Weather description
                                    String jsonObjdes = response.getString("weather");
                                    JSONArray jsonArray = new JSONArray(jsonObjdes);
                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject jsobj = jsonArray.getJSONObject(i);
                                        String description = jsobj.getString("description");
                                        desc.setText(description);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("TAG", "onErrorResponse: " + error);
                                Toast.makeText(MainActivity.this,"You are OFFLINE ! Connect to internet",Toast.LENGTH_SHORT).show();
                            }
                        });
                MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);

            }
        });

    }
}
