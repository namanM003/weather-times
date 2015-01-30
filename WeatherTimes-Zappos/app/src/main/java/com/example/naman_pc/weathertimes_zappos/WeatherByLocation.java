package com.example.naman_pc.weathertimes_zappos;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Naman-PC on 1/30/2015.
 */
public class WeatherByLocation implements Runnable {
    private static String API_GET_WEATHER = "http://api.openweathermap.org/data/2.5/weather?lat=%.4f&lon=%.4f&units=metric";
    static URL url;
    static JSONObject data;

    public void run() {
        try {
            //System.out.println(url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connect.addRequestProperty("x-api-key","com.example.naman_pc.weathertimes_zappos");
            //connect.setRequestMethod("GET");
            connection.setReadTimeout(5*1000);
            //connection.connect();
            connection.setDoInput(true);
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer ret_str = new StringBuffer(1024);
            String inputValues = "";
            while ((inputValues = br.readLine()) != null) {
                ret_str.append(inputValues).append("\n");
            }
            br.close();
            //System.out.println(ret_str.toString());

            data = new JSONObject(ret_str.toString());
            if (data.getInt("cod") != 200) {
                //data=null;
                //TextView text=(TextView)context.findViewById(R.id.textview);
                //text.setText("ERROR IN COD");
                Log.e("tag", "ERROR IN CONNECTION");
                data = null;
            }
            //return;
        } catch (Exception e) {
            e.printStackTrace();
            data = null;
        }

    }


    static public JSONObject getWeather(double lat,double longi){
        try {
            //str="Stony Brook";

            url = new URL(String.format(API_GET_WEATHER, lat,longi));
            //System.out.println(url);
            Thread t=new Thread(new WeatherByLocation());
            t.start();
            while(t.isAlive()){}
            if(data==null)
                System.out.println("NULL DATA");
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
