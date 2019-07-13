package vrushabh.com.weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    Button button;
    EditText city;
    TextView result,temp;
    //https://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=931e88cccd331c4e1ae31910f72b839b
    String  baseURL ="https://api.openweathermap.org/data/2.5/weather?q=";
    String API ="&appid=931e88cccd331c4e1ae31910f72b839b";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button =findViewById(R.id.button);
        city =findViewById(R.id.getCity);
        result =findViewById(R.id.result);
        temp =findViewById(R.id.temp);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (city.getText().toString().isEmpty()) {

                    city.setError("Invalid City");
                }
                else{


                final String myURL = baseURL + city.getText().toString() + API;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Log.i("JSON", "JSON: " + jsonObject);

                                try {
                                    String info = jsonObject.getString("weather");
                                    Log.i("INFO", "INFO: " + info);

                                    JSONArray ar = new JSONArray(info);

                                    for (int i = 0; i < ar.length(); i++) {
                                        JSONObject parObj = ar.getJSONObject(i);
                                        String myWeather = parObj.getString("main");
                                        result.setText(myWeather);
                                        Log.i("ID", "ID: " + parObj.getString("id"));
                                        Log.i("MAIN", "MAIN: " + parObj.getString("main"));
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    String main =jsonObject.getString("main");
                                    JSONObject ma =new JSONObject(main);
                                    Double temperature =ma.getDouble("temp");
                                    temperature = temperature -273.15;
                                    DecimalFormat numberFormat = new DecimalFormat("#.00");
                                    String humidity =ma.getString("humidity");
                                    temp.setText("Temperature ="+numberFormat.format(temperature).toString()+"\u2103"+"\n"+"Humidity ="+humidity);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

//                                try {
//                                    String coor = jsonObject.getString("coord");
//                                    Log.i("COOR", "COOR: " + coor);
//                                    JSONObject co = new JSONObject(coor);
//
//                                    String lon = co.getString("lon");
//                                    String lat = co.getString("lat");
//
//                                    Log.i("LON", "LON: " + lon);
//                                    Log.i("LAT", "LAT: " + lat);
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }


                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Log.i("Error", "Something went wrong" + volleyError);

                            }
                        }


                );
                MySingleton.getInstance(MainActivity.this).addToRequestQue(jsonObjectRequest);

            }



            }
        }




        );


    }
}
