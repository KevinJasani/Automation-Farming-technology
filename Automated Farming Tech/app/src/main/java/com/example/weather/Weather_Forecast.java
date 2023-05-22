package com.example.weather;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;

public class Weather_Forecast extends AppCompatActivity {

    TextView dt1;
    TextView dt2;
    TextView dt3;
    TextView dt4;
    TextView dt5;
    TextView dt6;
    TextView dt7;

    TextView day1;
    TextView day2;
    TextView day3;
    TextView day4;
    TextView day5;
    TextView day6;
    TextView day7;

    TextView min1;
    TextView min2;
    TextView min3;
    TextView min4;
    TextView min5;
    TextView min6;
    TextView min7;

    TextView max1;
    TextView max2;
    TextView max3;
    TextView max4;
    TextView max5;
    TextView max6;
    TextView max7;

    TextView humi1;
    TextView humi2;
    TextView humi3;
    TextView humi4;
    TextView humi5;
    TextView humi6;
    TextView humi7;


    String S_Lat, S_Lng,url;
    Double Lat, Lng;

    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather__forecast);

        Intent intent = getIntent();
        S_Lat = intent.getStringExtra("Lat");
        S_Lng = intent.getStringExtra("Lng");
        Lat = Double.parseDouble(S_Lat);
        Lng = Double.parseDouble(S_Lng);

        getSupportActionBar().setTitle("Weather Forecast");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dt1=findViewById(R.id.date1);
        dt2=findViewById(R.id.date2);
        dt3=findViewById(R.id.date3);
        dt4=findViewById(R.id.date4);
        dt5=findViewById(R.id.date5);
        dt6=findViewById(R.id.date6);
        dt7=findViewById(R.id.date7);

        day1=findViewById(R.id.temp1);
        day2=findViewById(R.id.temp2);
        day3=findViewById(R.id.temp3);
        day4=findViewById(R.id.temp4);
        day5=findViewById(R.id.temp5);
        day6=findViewById(R.id.temp6);
        day7=findViewById(R.id.temp7);

        min1=findViewById(R.id.min1);
        min2=findViewById(R.id.min2);
        min3=findViewById(R.id.min3);
        min4=findViewById(R.id.min4);
        min5=findViewById(R.id.min5);
        min6=findViewById(R.id.min6);
        min7=findViewById(R.id.min7);

        max1=findViewById(R.id.max1);
        max2=findViewById(R.id.max2);
        max3=findViewById(R.id.max3);
        max4=findViewById(R.id.max4);
        max5=findViewById(R.id.max5);
        max6=findViewById(R.id.max6);
        max7=findViewById(R.id.max7);

        humi1=findViewById(R.id.humi1);
        humi2=findViewById(R.id.humi2);
        humi3=findViewById(R.id.humi3);
        humi4=findViewById(R.id.humi4);
        humi5=findViewById(R.id.humi5);
        humi6=findViewById(R.id.humi6);
        humi7=findViewById(R.id.humi7);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM\nEEE");
        String date1 = simpleDateFormat.format(calendar.getTime());
        dt1.setText(date1);

        calendar.add(calendar.DAY_OF_YEAR,1);
        String date2 = simpleDateFormat.format(calendar.getTime());
        dt2.setText(date2);

        calendar.add(calendar.DAY_OF_YEAR,1);
        String date3 = simpleDateFormat.format(calendar.getTime());
        dt3.setText(date3);

        calendar.add(calendar.DAY_OF_YEAR,1);
        String date4 = simpleDateFormat.format(calendar.getTime());
        dt4.setText(date4);

        calendar.add(calendar.DAY_OF_YEAR,1);
        String date5 = simpleDateFormat.format(calendar.getTime());
        dt5.setText(date5);

        calendar.add(calendar.DAY_OF_YEAR,1);
        String date6 = simpleDateFormat.format(calendar.getTime());
        dt6.setText(date6);

        calendar.add(calendar.DAY_OF_YEAR,1);
        String date7 = simpleDateFormat.format(calendar.getTime());
        dt7.setText(date7);

        mQueue = Volley.newRequestQueue(this);
        url = "https://api.openweathermap.org/data/2.5/onecall?lat="+Lat+"&lon="+Lng+"&exclude=current,minutely,hourly,alerts&appid=8f0fbc05c6f7ffb4e406fe2cb4f30ce7";
        jsonParse(url);

    }
    private void jsonParse(String url) {
        //String url = "https://api.openweathermap.org/data/2.5/onecall?lat=22.565250&lon=72.948150&exclude=current,minutely,hourly,alerts&appid=8f0fbc05c6f7ffb4e406fe2cb4f30ce7";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DecimalFormat df = new DecimalFormat("#.##");
                        try {
                            JSONArray jsonArray = response.getJSONArray("daily");
                            for (int i = 0; i < jsonArray.length()-1; i++) {
                                JSONObject daily = jsonArray.getJSONObject(i);
                                int humi=daily.getInt("humidity");

                                JSONObject temp = daily.getJSONObject("temp");
                                double day= Double.valueOf(df.format(temp.getDouble("day")-273.15));
                                double min = Double.valueOf(df.format(temp.getDouble("min")-273.15));
                                double max = Double.valueOf(df.format(temp.getDouble("max")-273.15));


                                if(i==0){
                                    day1.setText(String.valueOf(day));
                                    min1.setText("Min : "+String.valueOf(min)+"°");
                                    max1.setText("Max : "+String.valueOf(max)+"°");
                                    humi1.setText(String.valueOf(humi)+"%");
                                }
                                else if(i==1){
                                    day2.setText(String.valueOf(day));
                                    min2.setText("Min : "+String.valueOf(min)+"°");
                                    max2.setText("Max : "+String.valueOf(max)+"°");
                                    humi2.setText(String.valueOf(humi)+"%");
                                }
                                else if(i==2){
                                    day3.setText(String.valueOf(day));
                                    min3.setText("Min : "+String.valueOf(min)+"°");
                                    max3.setText("Max : "+String.valueOf(max)+"°");
                                    humi3.setText(String.valueOf(humi)+"%");
                                }
                                else if(i==3){
                                    day4.setText(String.valueOf(day));
                                    min4.setText("Min : "+String.valueOf(min)+"°");
                                    max4.setText("Max : "+String.valueOf(max)+"°");
                                    humi4.setText(String.valueOf(humi)+"%");
                                }
                                else if(i==4){
                                    day5.setText(String.valueOf(day));
                                    min5.setText("Min : "+String.valueOf(min)+"°");
                                    max5.setText("Max : "+String.valueOf(max)+"°");
                                    humi5.setText(String.valueOf(humi)+"%");
                                }
                                else if(i==5){
                                    day6.setText(String.valueOf(day));
                                    min6.setText("Min : "+String.valueOf(min)+"°");
                                    max6.setText("Max : "+String.valueOf(max)+"°");
                                    humi6.setText(String.valueOf(humi)+"%");
                                }
                                else if(i==6){
                                    day7.setText(String.valueOf(day));
                                    min7.setText("Min : "+String.valueOf(min)+"°");
                                    max7.setText("Max : "+String.valueOf(max)+"°");
                                    humi7.setText(String.valueOf(humi)+"%");
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
}
