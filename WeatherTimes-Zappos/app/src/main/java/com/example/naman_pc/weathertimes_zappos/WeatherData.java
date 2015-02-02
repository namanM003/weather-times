package com.example.naman_pc.weathertimes_zappos;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import android.content.Context;


/**
 * Created by Naman-PC on 1/27/2015.
 */
public class WeatherData implements Runnable {
    private static String API_GET_WEATHER = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
    static URL url;
    static JSONObject data;

    public void run() {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connect.setRequestMethod("GET");
            connection.setReadTimeout(5*1000);      //Wait for a maximum of 5 minutes to get data
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
                Log.e("tag","ERROR IN CONNECTION");
                data = null;
            }
            //return;
        } catch (Exception e) {
            e.printStackTrace();
            data = null;
        }

    }

    static public JSONObject getData(String str) {
        try {
            //str="Stony Brook";
            url = new URL(String.format(API_GET_WEATHER, str));
            Thread t=new Thread(new WeatherData());
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

