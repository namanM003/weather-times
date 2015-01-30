package com.example.naman_pc.weathertimes_zappos;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.browse.MediaBrowser;
import android.view.inputmethod.InputMethodManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.conn.ClientConnectionManager;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {//implements LocationListener {
   // private LocationManager locationmanager;
    //Location location;
    //double longitude,latitude;
    TextView textView;
   // private LocationListener locationlistener;
   // private String location;

    public void getWeather(View view){
        //This method is responsible to search location as specified in text box and display weather of that location

        textView = (TextView)this.findViewById(R.id.textview);
        String text="You are trying to find by search";
        ConnectivityManager cm=(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo[]=cm.getAllNetworkInfo();
        boolean wifi=false,mobile_net=false;
        for(int i=0;i<netinfo.length;i++){
            if(netinfo[i].getTypeName().equalsIgnoreCase("WIFI"))
            {
                if(netinfo[i].isConnected()){
                    wifi=true;
                }
            }
            if(netinfo[i].getTypeName().equalsIgnoreCase("MOBILE")){
                if(netinfo[i].isConnected()){
                    mobile_net=true;
                }
            }

        }
        if(!wifi && !mobile_net){
            this.textView.setText("Network not available!!");
            return;
        }
        this.textView.setText(text);
        EditText getdata= (EditText)findViewById(R.id.edit_message);
        String city=getdata.getText().toString();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getdata.getWindowToken(), 0);
        if(city.trim().equals("")){
            Toast.makeText(this,"Field cannot be left blank!! Enter City Name",Toast.LENGTH_LONG).show();
            return;
        }
        //Context con=getApplicationContext();
        JSONObject data=WeatherData.getData(city);
        if(data == null){
            Toast.makeText(this,"City Not Found",Toast.LENGTH_LONG).show();
        }
        else{
            try {
                JSONObject main = data.getJSONObject("main");
                JSONObject description=data.getJSONArray("weather").getJSONObject(0);
                textView.setText(String.format("Current Temperature is\n %.2f",main.getDouble("temp"))+" ℃"+"\n"+description.getString("description").toUpperCase());
            }
            catch(Exception e){
                text="Unable to fetch data!!!";
                this.textView.setText(text);

            }

        }
    }

    public void getLocation(View view){
        //This method is responsible to search current location of the user and display weather of that location
        try {
            textView = (TextView)this.findViewById(R.id.textview);
            LocationManager locationmanager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled;
            isGPSEnabled = locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(!isGPSEnabled){
               // textView=(TextView) this.findViewById(R.id.textview);
                this.textView.setText("GPS not enabled!!");
                return;
            }
            String locationProvider=LocationManager.GPS_PROVIDER;
            double lat,longitude;
            Location location = locationmanager.getLastKnownLocation(locationProvider);
            if (location == null) {
              //  textView = (TextView) this.findViewById(R.id.textview);
                String text = "Unable to find location";
                this.textView.setText(text);
                return;

            }
            lat=location.getLatitude();
            longitude=location.getLongitude();
            JSONObject json=WeatherByLocation.getWeather(lat,longitude);
            if(json==null){
                this.textView.setText("Unable to get details!");
                return;
            }
            else{
                try {
                    JSONObject main = json.getJSONObject("main");
                    JSONObject description=json.getJSONArray("weather").getJSONObject(0);
                    textView.setText(String.format("Current Temperature is\n %.2f",main.getDouble("temp"))+" ℃"+"\n"+description.getString("description").toUpperCase());
                }
                catch(Exception e){
                   String text="Unable to fetch data!!!";
                    this.textView.setText(text);

                }

            }



        }
        catch(Exception e){
            textView = (TextView) this.findViewById(R.id.textview);
            String text="Location ERROR";
            this.textView.setText(text);
            e.printStackTrace();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // locationmanager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
       // locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationlistener);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
