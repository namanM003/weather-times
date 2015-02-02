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

    TextView textView;

    public void getWeather(View view){
        //This method is responsible to search location as specified in text box and display weather of that location

        textView = (TextView)this.findViewById(R.id.textview);
        String text="You are trying to find by search";
        ConnectivityManager cm=(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo[]=cm.getAllNetworkInfo();   //To check whether we are having connection to any network
        boolean wifi=false,mobile_net=false;            //We will check connection to either Wifi or Mobile Network
        for(int i=0;i<netinfo.length;i++){
            if(netinfo[i].getTypeName().equalsIgnoreCase("WIFI"))
            {
                if(netinfo[i].isConnected()){           //If we have Wifi Connected set @param:wifi=true
                    wifi=true;
                }
            }
            if(netinfo[i].getTypeName().equalsIgnoreCase("MOBILE")){
                if(netinfo[i].isConnected()){       //If we have Mobile net set @param:mobile_net=true;
                    mobile_net=true;
                }
            }

        }
        if(!wifi && !mobile_net){
            this.textView.setText("Network not available!!");       //If no network connection available set text network not available
            return;
        }
        this.textView.setText(text);
        EditText getdata= (EditText)findViewById(R.id.edit_message);
        String city=getdata.getText().toString();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getdata.getWindowToken(), 0);
        if(city.trim().equals("")){     //validate input by trimming whitespaces before and after city name provided
            Toast.makeText(this,"Field cannot be left blank!! Enter City Name",Toast.LENGTH_LONG).show();
            return;
        }
        //Context con=getApplicationContext();
        JSONObject data=WeatherData.getData(city);
        if(data == null){   //If we dont get city field then either city is not available or network timed out.
            Toast.makeText(this,"City Not Found",Toast.LENGTH_LONG).show();
        }
        else{
            try {
                JSONObject main = data.getJSONObject("main");
                JSONObject description=data.getJSONArray("weather").getJSONObject(0);
                ///Setting Up Current Temperature and Description of weather in TextView////
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
            isGPSEnabled = locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER); //Set @param: isGpsEnabled
            if(!isGPSEnabled){
               // textView=(TextView) this.findViewById(R.id.textview);
                this.textView.setText("GPS not enabled!!"); //If GPS is not available set TextView as GPS not enabled in textView
                return;
            }
            String locationProvider=LocationManager.GPS_PROVIDER;
            double lat,longitude;
            Location location = locationmanager.getLastKnownLocation(locationProvider);     //Get Last Known Location of the user via GPS
            if (location == null) {
              //  textView = (TextView) this.findViewById(R.id.textview);
                String text = "Unable to get location";
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


///////////////////////////////////////////////////API USED////////////////////////////////////////////////////////////
/**********************************************Open Weather API******************************************************/